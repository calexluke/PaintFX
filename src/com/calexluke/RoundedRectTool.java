package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class RoundedRectTool extends RectTool {

    @Override
    protected ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        RectDrawOperation rectOp = new RoundedRectDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return rectOp;
    }

    public String toString() {
        return "Rounded Rectangle tool";
    }
}
