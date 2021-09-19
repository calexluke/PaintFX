package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class OvalTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        calculateScaledShapeParameters(e.getX(), e.getY(), graphicsContext);
        createOvalOperation(graphicsContext, operations);
    }

    protected void createOvalOperation(GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        // add operation to array for undo/redo and scaling
        OvalDrawOperation ovalOp = new OvalDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        ovalOp.draw(graphicsContext);
        operations.add(ovalOp);
    }
}
