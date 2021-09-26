package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class SquareTool extends RectTool {

    @Override
    protected ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        // make both side lengths equal whichever dimension is longer
        double sideLength = Math.max(relativeWidth, relativeHeight);
        SquareDrawOperation squareOp = new SquareDrawOperation(relativeTopLeftX, relativeTopLeftY, sideLength, sideLength,
                relativeLineWidth, strokeColor, fillColor);
        return squareOp;
    }
}
