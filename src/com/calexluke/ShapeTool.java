package com.calexluke;

// parent class for shape-drawing tools

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

public class ShapeTool extends PaintFxTool {

    // coords of the initial point user clicked
    double startX;
    double startY;

    // parameters of the shape to draw
    double relativeTopLeftX;
    double relativeTopLeftY;
    double relativeWidth;
    double relativeHeight;
    double relativeLineWidth;

    public ShapeTool() {
        super();
        makesChangesToCanvas = true;
    }

    // store the coords where the user first clicks
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        startX = e.getX();
        startY = e.getY();
    }

    // calculate the parameters used to draw rectangles, squares, ovals, circles
    // pass in co-ords from where the user released the mouse
    // These values are scaled by the current size of canvas, so can be re-drawn at other scales in ShapeDrawOperations
    protected void calculateScaledShapeParameters(double endX, double endY, GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // calculate where the top left corner of the shape should be
        double xDifference = endX - startX;
        double yDifference = endY - startY;
        double x = (xDifference > 0) ? startX : endX;
        double y = (yDifference > 0) ? startY : endY;
        relativeTopLeftX = x / canvasWidth;
        relativeTopLeftY = y / canvasHeight;

        // calculate side lengths
        relativeWidth = Math.abs(endX - startX) / canvasWidth;
        relativeHeight = Math.abs(endY - startY) / canvasHeight;

        relativeLineWidth = graphicsContext.getLineWidth() / canvasWidth;
    }
}
