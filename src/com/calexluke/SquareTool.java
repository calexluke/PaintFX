package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class SquareTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateShapeParameters(e.getX(), e.getY());
        double sideLength = Math.max(width, height);
        graphicsContext.fillRect(topLeftX, topLeftY, sideLength, sideLength);
        graphicsContext.strokeRect(topLeftX, topLeftY, sideLength, sideLength);
    }
}
