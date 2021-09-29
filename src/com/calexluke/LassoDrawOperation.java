package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LassoDrawOperation extends RectDrawOperation {

    ImageDrawOperation imageDrawOperation;

    public LassoDrawOperation(double x, double y, double width, double height) {
        super(x, y, width, height, 0, Color.TRANSPARENT, Color.TRANSPARENT);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.clearRect(scaledX, scaledY, scaledWidth, scaledHeight);

        if (imageDrawOperation != null) {
            System.out.println("Image Draw operation was not null!");
            imageDrawOperation.draw(graphicsContext);
        } else {
            System.out.println("Image Draw operation was null!");
        }
    }

    public void setImageDrawOperation(ImageDrawOperation operation) {
        imageDrawOperation = operation;
    }
}
