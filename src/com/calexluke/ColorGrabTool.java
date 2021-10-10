package com.calexluke;

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
        // take a snapshot of the canvas
        Canvas canvas = graphicsContext.getCanvas();
        WritableImage snap = canvas.snapshot(null, null);
        // read color of pixel at coords
        Color color = snap.getPixelReader().getColor((int)e.getX(), (int)e.getY());
        colorPicker.setValue(color);
        stateManager.setStrokeColor(color);
    }

    public String toString() {
        return "Color Grab tool";
    }
}
