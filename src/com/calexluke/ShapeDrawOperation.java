package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class ShapeDrawOperation implements DrawOperation {

    double topLeftX;
    double topLeftY;
    double width;
    double height;
    double lineWidth;
    Paint strokeColor;
    Paint fillColor;

    double scaledX;
    double scaledY;
    double scaledWidth;
    double scaledHeight;
    double scaledLineWidth;

    public ShapeDrawOperation(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        topLeftX = x;
        topLeftY = y;
        this.width = width;
        this.height = height;
        this.lineWidth = lineWidth;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }

    public abstract void draw(GraphicsContext graphicsContext);

    protected void scaleParamsToCanvasSize(GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get absolute co-ords by multiplying by current dimensions
        scaledX = topLeftX * canvasWidth;
        scaledY = topLeftY * canvasHeight;
        scaledWidth = width * canvasWidth;
        scaledHeight = height * canvasHeight;
        scaledLineWidth = lineWidth * canvasWidth;
    }
}
