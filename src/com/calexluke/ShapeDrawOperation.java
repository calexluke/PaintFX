package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class ShapeDrawOperation implements DrawOperation {

    // parameters as a proportion of canvas size
    double relativeTopLeftX;
    double relativeTopLeftY;
    double relativeWidth;
    double relativeHeight;
    double relativeLineWidth;
    Paint strokeColor;
    Paint fillColor;

    // parameters scaled to actual coordinates based on the current canvas size
    double scaledX;
    double scaledY;
    double scaledWidth;
    double scaledHeight;
    double scaledLineWidth;

    public ShapeDrawOperation(double x, double y, double width, double height, double lineWidth, Paint strokeColor, Paint fillColor) {
        relativeTopLeftX = x;
        relativeTopLeftY = y;
        this.relativeWidth = width;
        this.relativeHeight = height;
        this.relativeLineWidth = lineWidth;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
    }

    public abstract void draw(GraphicsContext graphicsContext);

    protected void scaleParamsToCanvasSize(GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get absolute co-ords by multiplying by current dimensions
        scaledX = relativeTopLeftX * canvasWidth;
        scaledY = relativeTopLeftY * canvasHeight;
        scaledWidth = relativeWidth * canvasWidth;
        scaledHeight = relativeHeight * canvasHeight;
        scaledLineWidth = relativeLineWidth * canvasWidth;
    }
}
