package com.calexluke;

// parent class for shape-drawing tools

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public abstract class ShapeTool extends PaintFxTool {

    // coords of the initial point user clicked
    double startX;
    double startY;

    // parameters of the shape to draw, relative to the canvas size
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
    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        ShapeDrawOperation operation = createShapeOperation(graphicsContext);
        // add final operation to array for undo/redo and scaling
        operation.draw(graphicsContext);
        canvas.pushToUndoStack(operation);
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        ShapeDrawOperation operation = createShapeOperation(graphicsContext);

        // draw but don't save to operations array (drawing not final until mouse release)
        // used for interactive draw
        canvas.drawImageOnCanvas();
        canvas.reDraw();
        operation.draw(graphicsContext);
    }

    /**
     * Calculate the parameters used to draw rectangles, squares, ovals, circles, etc.
     * Pass in co-ords from where the user released the mouse
     * These values are relative the current size of canvas, so can be re-drawn at other scales in ShapeDrawOperations
     *
     * @param endX double
     * @param endY double
     * @param graphicsContext GraphicsContext
     */
    protected void calculateRelativeShapeParameters(double endX, double endY, GraphicsContext graphicsContext) {
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

    // overridden in subclasses to create ovals, rectangles, etc
    protected abstract ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext);
}
