package com.calexluke;

import javafx.scene.canvas.GraphicsContext;

public class CropDrawOperation extends LassoDrawOperation {

    public CropDrawOperation(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);

        // clear entire graphics context
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        canvas.clearGraphicsContext();

        // redraw cropped image on cleared canvas
        if (imageDrawOperation != null) {
            imageDrawOperation.draw(graphicsContext);
        }
    }
}
