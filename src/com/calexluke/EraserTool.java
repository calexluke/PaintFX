package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

// Eraser tool is functions identically to pencil tool, but always draws with a thick white line

public class EraserTool extends PencilTool {

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledLineParameters(e, graphicsContext);
        // add current point to array
        xValues.add(relativeX);
        yValues.add(relativeY);

        // draw the new line segment
        DrawOperation segmentOperation = createEraseSegmentOperation(graphicsContext);
        segmentOperation.draw(graphicsContext);

        // current coords are start of the next segment
        startX = relativeX;
        startY = relativeY;
    }

    // called in LineTool onMouseReleased, to store the entire pencil drawing operation
    @Override
    protected DrawOperation createDrawOperation(GraphicsContext graphicsContext) {
        Paint color = Color.WHITE;
        relativeLineWidth = 10 / graphicsContext.getCanvas().getWidth();
        PencilDrawOperation pencilOp = new PencilDrawOperation(xValues, yValues, color, relativeLineWidth);
        return pencilOp;
    }

    protected DrawOperation createEraseSegmentOperation(GraphicsContext graphicsContext) {
        Paint color = Color.WHITE;
        relativeLineWidth = 10 / graphicsContext.getCanvas().getWidth();
        LineDrawOperation lineOp = new LineDrawOperation(startX, startY, relativeX, relativeY, color, relativeLineWidth);
        return lineOp;
    }

    public String toString() {
        return "Eraser tool";
    }

}
