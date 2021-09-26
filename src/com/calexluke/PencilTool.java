package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

// tool for freehand drawing on canvas
// creates line on drag instead of on release

public class PencilTool extends LineTool {

    // store the list of points the hand drawn line goes through
    private ArrayList<Double> xValues;
    private ArrayList<Double> yValues;

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        super.onMousePressed(e, graphicsContext);
        if (xValues != null) {
            xValues.clear();
        } else {
            xValues = new ArrayList<>();
        }

        if (yValues != null) {
            yValues.clear();
        } else {
            yValues = new ArrayList<>();
        }
        xValues.add(startX);
        yValues.add(startY);
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        // add current point to array
        xValues.add(relativeX);
        yValues.add(relativeY);

        // draw the new line segment
        LineDrawOperation segmentOperation = createLineOperation(graphicsContext);
        segmentOperation.draw(graphicsContext);

        // current coords are start of the next segment
        startX = relativeX;
        startY = relativeY;
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        calculateScaledLineParameters(e, graphicsContext);
        PencilDrawOperation operation = createPencilDrawOperation(graphicsContext);
        // add operation to array for undo/redo and scaling
        canvas.pushToUndoStack(operation);
        canvas.drawImageOnCanvas();
        canvas.reDraw();
    }

    protected PencilDrawOperation createPencilDrawOperation(GraphicsContext graphicsContext) {
        Paint color = graphicsContext.getStroke();
        PencilDrawOperation pencilOp = new PencilDrawOperation(xValues, yValues, color, relativeLineWidth);
        return pencilOp;
    }
}


