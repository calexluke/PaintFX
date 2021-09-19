package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class LineDrawOperation implements DrawOperation {

    double startX;
    double startY;
    double endX;
    double endY;
    Paint strokeColor;
    double lineWidth;

    double scaledStartX;
    double scaledStartY;
    double scaledEndX;
    double scaledEndY;
    double scaledLineWidth;

    public  LineDrawOperation(double startX, double startY, double endX, double endY, Paint strokeColor, double lineWidth) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.strokeColor = strokeColor;
        this.lineWidth = lineWidth;
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);

        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.beginPath();
        graphicsContext.moveTo(scaledStartX, scaledStartY);
        graphicsContext.lineTo(scaledEndX, scaledEndY);
        graphicsContext.stroke();
    }

    protected void scaleParamsToCanvasSize(GraphicsContext graphicsContext) {
        double width = graphicsContext.getCanvas().getWidth();
        double height = graphicsContext.getCanvas().getHeight();

        // get absolute co-ords by multiplying by current dimensions
        scaledStartX = startX * width;
        scaledStartY = startY * height;
        scaledEndX = endX * width;
        scaledEndY = endY * height;
        scaledLineWidth = lineWidth * width;
    }
}
