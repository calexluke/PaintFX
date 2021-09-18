package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class CircleTool extends  ShapeTool {
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateShapeParameters(e.getX(), e.getY());
        double sideLength = Math.max(width, height);
        graphicsContext.fillOval(topLeftX, topLeftY, sideLength, sideLength);
        graphicsContext.strokeOval(topLeftX, topLeftY, sideLength, sideLength);
    }
}
