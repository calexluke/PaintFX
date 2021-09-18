package com.calexluke;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

// class for handling drawing and other canvas-related things

public class PaintFxCanvas extends Canvas {

    private GraphicsContext graphicsContext;
    private StateManager stateManager;

    public PaintFxCanvas(double initialWidth, double initialHeight, StateManager stateManager) {
        super(initialWidth, initialHeight);
        this.stateManager = stateManager;
        graphicsContext = this.getGraphicsContext2D();

        // initial values. These will change dynamically as user selects tools
        graphicsContext.setFill(Color.BLACK);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        this.setOnMouseDragged(this::onDrag);
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseReleased(this::onMouseReleased);
    }

    // called when user wants to draw a graphic - ensure color and stroke width are up to date with user preferences
    public void updateGraphicsContext() {
        updateStrokeWidth();
        updateColors();
    }

    private void updateStrokeWidth() {
        switch (stateManager.getSelectedStrokeWidth()) {
            case THIN:
                graphicsContext.setLineWidth(2);
                break;
            case MEDIUM:
                graphicsContext.setLineWidth(5);
                break;
            case WIDE:
                graphicsContext.setLineWidth(10);
                break;
            default:
                graphicsContext.setLineWidth(2);
        }
    }

    private void updateColors() {
        Color strokeColor = stateManager.getStrokeColor();
        Color fillColor = stateManager.getFillColor();
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
    }

    public void clearGraphicsContext() {
        graphicsContext.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    private void onMouseReleased(MouseEvent e) {
        updateGraphicsContext();
        PaintFxTool tool = stateManager.getSelectedTool();
        tool.onMouseReleased(e, graphicsContext);
    }

    private void onDrag(MouseEvent e) {
        updateGraphicsContext();
        PaintFxTool tool = stateManager.getSelectedTool();
        tool.onDrag(e, graphicsContext);
    }

    private void onMousePressed(MouseEvent e) {
        updateGraphicsContext();
        PaintFxTool tool = stateManager.getSelectedTool();
        tool.onMousePressed(e, graphicsContext);
    }
}
