package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;

import java.util.ArrayList;

// tool for drawing a straight line on  the canvas

public class LineTool extends PaintFxTool {

    double startX;
    double startY;

    public LineTool() {
        super();
        makesChangesToCanvas = true;
    }

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        // get the x/y co-ords scaled to the dimensions of canvas
        startX = e.getX() / graphicsContext.getCanvas().getWidth();
        startY = e.getY() / graphicsContext.getCanvas().getHeight();
    }

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        createLineOperation(e, graphicsContext, operations);
    }

    protected void createLineOperation(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get the x/y co-ords and line width scaled to the dimensions of canvas
        double relativeX = e.getX() / canvasWidth;
        double relativeY = e.getY() / canvasHeight;
        double relativeLineWidth = graphicsContext.getLineWidth() / canvasWidth;
        Paint color = graphicsContext.getStroke();

        // add operation to array for undo/redo and scaling
        LineDrawOperation lineOp = new LineDrawOperation(startX, startY, relativeX, relativeY, color, relativeLineWidth);
        lineOp.draw(graphicsContext);
        operations.add(lineOp);

        // current coords are start of the next segment
        startX = relativeX;
        startY = relativeY;
    }
}

