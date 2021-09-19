package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class RectTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        calculateScaledShapeParameters(e.getX(), e.getY(), graphicsContext);
        createRectOperation(graphicsContext, operations);
    }

    protected void createRectOperation(GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        // add operation to array for undo/redo and scaling
        RectDrawOperation rectOp = new RectDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        rectOp.draw(graphicsContext);
        operations.add(rectOp);
    }
}
