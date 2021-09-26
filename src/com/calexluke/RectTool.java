package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class RectTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        RectDrawOperation rectOp = createRectOperation(graphicsContext);
        // add operation to array for undo/redo and scaling
        rectOp.draw(graphicsContext);
        canvas.pushToUndoStack(rectOp);
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        RectDrawOperation rectOp = createRectOperation(graphicsContext);

        // draw but don't save to operations array (drawing not final until mouse release)
        canvas.drawImageOnCanvas();
        canvas.reDraw();
        rectOp.draw(graphicsContext);
    }

    protected RectDrawOperation createRectOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        RectDrawOperation rectOp = new RectDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return rectOp;
    }
}
