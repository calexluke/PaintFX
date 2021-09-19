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

    public  LineDrawOperation(double startX, double startY, double endX, double endY, Paint strokeColor, double lineWidth) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.strokeColor = strokeColor;
        this.lineWidth = lineWidth;
    }

    public void draw(GraphicsContext graphicsContext) {
        double width = graphicsContext.getCanvas().getWidth();
        double height = graphicsContext.getCanvas().getHeight();

        // get absolute co-ords by multiplying by current dimensions
        double scaledStartX = startX * width;
        double scaledStartY = startY * height;
        double scaledEndX = endX * width;
        double scaledEndY = endY * height;
        double scaledLineWidth = lineWidth * width;

        graphicsContext.setLineWidth(scaledLineWidth);
        graphicsContext.setStroke(strokeColor);
        graphicsContext.beginPath();
        graphicsContext.moveTo(scaledStartX, scaledStartY);
        graphicsContext.lineTo(scaledEndX, scaledEndY);
        graphicsContext.stroke();
    }
}
