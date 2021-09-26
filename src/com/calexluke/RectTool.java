package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class RectTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledShapeParameters(e.getX(), e.getY(), graphicsContext);
        createRectOperation(graphicsContext);
    }

    protected void createRectOperation(GraphicsContext graphicsContext) {
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        // add operation to array for undo/redo and scaling
        RectDrawOperation rectOp = new RectDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        rectOp.draw(graphicsContext);
        canvas.pushToUndoStack(rectOp);
    }
}
