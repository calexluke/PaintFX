package com.calexluke;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ColorGrabTool extends PaintFxTool {

    ColorPicker colorPicker;
    StateManager stateManager;

    public ColorGrabTool(ColorPicker picker, StateManager stateManager) {
        super();
        colorPicker = picker;
        this.stateManager = stateManager;
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        // take a snapshot of the canvas/image stackpane
        Canvas canvas = graphicsContext.getCanvas();
        WritableImage snap = canvas.getParent().snapshot(null, null);
        // translate the x/y coords of the mouse event on the canvas to coords on the stackpane
        Point2D localPoint = canvas.localToParent(e.getX(), e.getY());
        int x = (int)localPoint.getX();
        int y = (int)localPoint.getY();

        // read color of pixel at coords
        Color color = snap.getPixelReader().getColor(x, y); //This just gets the color without assigning it.
        colorPicker.setValue(color);
        stateManager.setStrokeColor(color);
    }
}
