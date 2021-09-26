package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

// tool for drawing a straight line on  the canvas

public class LineTool extends PaintFxTool {

    double startX;
    double startY;

    double relativeX;
    double relativeY;
    double relativeLineWidth;

    public LineTool() {
        super();
        makesChangesToCanvas = true;
    }

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        // get the x/y co-ords scaled to the dimensions of canvas
        startX = e.getX() / graphicsContext.getCanvas().getWidth();
        startY = e.getY() / graphicsContext.getCanvas().getHeight();
    }

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        LineDrawOperation operation = createLineOperation(graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();

        // add operation to array for undo/redo and scaling
        operation.draw(graphicsContext);
        canvas.pushToUndoStack(operation);
    }

    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        LineDrawOperation operation = createLineOperation(graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas)graphicsContext.getCanvas();
        canvas.drawImageOnCanvas();
        canvas.reDraw();
        operation.draw(graphicsContext);
    }

    protected void calculateScaledLineParameters(MouseEvent e, GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get the x/y co-ords and line width scaled to the dimensions of canvas
        relativeX = e.getX() / canvasWidth;
        relativeY = e.getY() / canvasHeight;
        relativeLineWidth = graphicsContext.getLineWidth() / canvasWidth;
    }

    protected LineDrawOperation createLineOperation(GraphicsContext graphicsContext) {
        Paint color = graphicsContext.getStroke();
        LineDrawOperation lineOp = new LineDrawOperation(startX, startY, relativeX, relativeY, color, relativeLineWidth);
        return lineOp;
    }
}

