package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DashedRectDrawOperation extends RectDrawOperation {

    public DashedRectDrawOperation(double x, double y, double width, double height) {
        super(x, y, width, height, 2, Color.GRAY, Color.BLACK);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.setLineWidth(relativeLineWidth);
        // add dashes to stroke
        graphicsContext.setLineDashes(5);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.strokeRect(scaledX, scaledY, scaledWidth, scaledHeight);
        // remove dashes from future strokes
        graphicsContext.setLineDashes(null);
    }
}
