package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

/**
 * tool for freehand drawing on canvas
 * creates line on drag instead of on release
 */

public class PencilTool extends LineTool {

    /**
     * store the list of points the hand drawn line goes through
     */
    protected ArrayList<Double> xValues;
    protected ArrayList<Double> yValues;

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        super.onMousePressed(e, graphicsContext);

        // start of new pencil drawing, re-initialize array of points
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        xValues.add(startX);
        yValues.add(startY);
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        // add current point to array
        xValues.add(relativeX);
        yValues.add(relativeY);

        // draw the new line segment (using LineTool's operation method for the new segment)
        DrawOperation segmentOperation = super.createDrawOperation(graphicsContext);
        segmentOperation.draw(graphicsContext);

        // current coords are start of the next segment
        startX = relativeX;
        startY = relativeY;
    }

    /**
     * called in LineTool onMouseReleased, to store the entire pencil drawing operation
     * @param graphicsContext
     * @return pencil draw operation
     */
    @Override
    protected DrawOperation createDrawOperation(GraphicsContext graphicsContext) {
        Paint color = graphicsContext.getStroke();
        PencilDrawOperation pencilOp = new PencilDrawOperation(xValues, yValues, color, relativeLineWidth);
        return pencilOp;
    }

    public String toString() {
        return "Pencil tool";
    }
}


