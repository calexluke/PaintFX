package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

// tool for freehand drawing on canvas
// creates line on drag instead of on release

public class PencilTool extends LineTool {

    public void onDrag(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        PaintFxCanvas canvas = (PaintFxCanvas)graphicsContext.getCanvas();
        calculateScaledLineParameters(e, graphicsContext);
        LineDrawOperation operation = createLineOperation(graphicsContext);
        operation.draw(graphicsContext);
        canvas.pushToUndoStack(operation);

        // current coords are start of the next segment
        startX = relativeX;
        startY = relativeY;
    }
}


