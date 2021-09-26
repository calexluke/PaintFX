package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RectTool extends ShapeTool {

    @Override
    protected ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        RectDrawOperation rectOp = new RectDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return rectOp;
    }
}
