package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;


public class OvalTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        OvalDrawOperation ovalOp = createOvalOperation(graphicsContext);
        // add operation to array for undo/redo and scaling
        ovalOp.draw(graphicsContext);
        canvas.pushToUndoStack(ovalOp);
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateScaledShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        OvalDrawOperation ovalOp = createOvalOperation(graphicsContext);

        // draw but don't save to operations array (drawing not final until mouse release)
        canvas.drawImageOnCanvas();
        canvas.reDraw();
        ovalOp.draw(graphicsContext);
    }

    protected OvalDrawOperation createOvalOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        OvalDrawOperation ovalOp = new OvalDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return ovalOp;
    }
}
