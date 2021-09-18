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
        graphicsContext.fillOval(50,50,20,20);

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
        updateFillColor();
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

    private void updateFillColor() {
        Color userColor = stateManager.getSelectedColor();
        graphicsContext.setStroke(userColor);
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
        PaintFxTool tool = stateManager.getSelectedTool();
        tool.onMousePressed(e, graphicsContext);
    }
}
