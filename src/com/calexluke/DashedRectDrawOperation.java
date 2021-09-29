package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DashedRectDrawOperation extends RectDrawOperation {

    public DashedRectDrawOperation(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        super(x, y, width, height, lineWidth, strokeColor, fillColor);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.setLineWidth(2);
        // add dashes to stroke
        graphicsContext.setLineDashes(5);
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeRect(scaledX, scaledY, scaledWidth, scaledHeight);
        // remove dashes from future strokes
        graphicsContext.setLineDashes(null);
    }
}
