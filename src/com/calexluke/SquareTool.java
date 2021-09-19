package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class SquareTool extends RectTool {

    protected void createRectOperation(GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        // make side lengths equal whichever is longer
        double sideLength = Math.max(relativeWidth, relativeHeight);

        // add operation to array for undo/redo and scaling
        RectDrawOperation squareOp = new RectDrawOperation(relativeTopLeftX, relativeTopLeftY, sideLength, sideLength,
                relativeLineWidth, strokeColor, fillColor);
        squareOp.draw(graphicsContext);
        operations.add(squareOp);
    }
}
