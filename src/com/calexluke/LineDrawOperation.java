package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class LineDrawOperation implements DrawOperation {

    /**
     *  parameters as a proportion of canvas size
     */
    double relativeStartX;
    double relativeStartY;
    double relativeEndX;
    double relativeEndY;
    double relativeLineWidth;
    Paint strokeColor;

    /**
     * parameters scaled to actual coordinates based on the current canvas size
     */
    double scaledStartX;
    double scaledStartY;
    double scaledEndX;
    double scaledEndY;
    double scaledLineWidth;

    public LineDrawOperation(double startX, double startY, double endX, double endY, Paint strokeColor, double lineWidth) {
        this.relativeStartX = startX;
        this.relativeStartY = startY;
        this.relativeEndX = endX;
        this.relativeEndY = endY;
        this.strokeColor = strokeColor;
        this.relativeLineWidth = lineWidth;
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
        scaledStartX = relativeStartX * width;
        scaledStartY = relativeStartY * height;
        scaledEndX = relativeEndX * width;
        scaledEndY = relativeEndY * height;
        scaledLineWidth = relativeLineWidth * width;
    }
}
