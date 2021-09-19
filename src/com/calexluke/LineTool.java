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

    double relativeX;
    double relativeY;
    double relativeLineWidth;

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
        calculateScaledLineParameters(e, graphicsContext);
        createLineOperation(graphicsContext, operations);
    }

    protected void calculateScaledLineParameters(MouseEvent e, GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get the x/y co-ords and line width scaled to the dimensions of canvas
        relativeX = e.getX() / canvasWidth;
        relativeY = e.getY() / canvasHeight;
        relativeLineWidth = graphicsContext.getLineWidth() / canvasWidth;
    }

    protected void createLineOperation(GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
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

