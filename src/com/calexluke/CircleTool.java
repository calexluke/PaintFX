package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class CircleTool extends OvalTool {

    @Override
    protected ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();

        // make diameter equal to whichever dimension is longer
        double diameter = Math.max(relativeWidth, relativeHeight);

        // add operation to array for undo/redo and scaling
        CircleDrawOperation circleOp = new CircleDrawOperation(relativeTopLeftX, relativeTopLeftY, diameter, diameter,
                relativeLineWidth, strokeColor, fillColor);

        return circleOp;
    }

    public String toString() {
        return "Circle tool";
    }
}
