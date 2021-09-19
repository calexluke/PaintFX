package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class CircleTool extends OvalTool {

    protected void createOvalOperation(GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        // make diameter equal wto whichever dimension is longer
        double diameter = Math.max(relativeWidth, relativeHeight);

        // add operation to array for undo/redo and scaling
        OvalDrawOperation circleOp = new OvalDrawOperation(relativeTopLeftX, relativeTopLeftY, diameter, diameter,
                relativeLineWidth, strokeColor, fillColor);
        circleOp.draw(graphicsContext);
        operations.add(circleOp);
    }
}
