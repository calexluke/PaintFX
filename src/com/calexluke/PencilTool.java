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
        calculateScaledLineParameters(e, graphicsContext);
        createLineOperation(graphicsContext, operations);
    }
}


