package com.calexluke;

// parent class for shape-drawing tools

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class ShapeTool extends PaintFxTool {

    // coords of the initial point user clicked
    double startX;
    double startY;

    // parameters of the shape to draw
    double topLeftX;
    double topLeftY;
    double width;
    double height;

    public ShapeTool() {
        super();
        makesChangesToCanvas = true;
    }

    // store the coords where the user first clicks
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        startX = e.getX();
        startY = e.getY();
    }

    // calculate the parameters used to draw rectangles, squares, ovals, circles
    // pass in co-ords from where the user released the mouse
    protected void calculateShapeParameters(double endX, double endY) {

        // calculate where the top left corner of the shape should be
        double xDifference = endX - startX;
        double yDifference = endY - startY;
        topLeftX = (xDifference > 0) ? startX : endX;
        topLeftY = (yDifference > 0) ? startY : endY;

        // calculate side lengths
        width = Math.abs(endX - startX);
        height = Math.abs(endY - startY);
    }
}
