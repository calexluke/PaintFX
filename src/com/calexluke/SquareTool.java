package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class SquareTool extends RectTool {

    protected SquareDrawOperation createRectOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        // make side lengths equal whichever is longer
        double sideLength = Math.max(relativeWidth, relativeHeight);
        SquareDrawOperation squareOp = new SquareDrawOperation(relativeTopLeftX, relativeTopLeftY, sideLength, sideLength,
                relativeLineWidth, strokeColor, fillColor);
        return squareOp;

    }
}
