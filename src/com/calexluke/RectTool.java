package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class RectTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateShapeParameters(e.getX(), e.getY());
        graphicsContext.fillRect(topLeftX, topLeftY, width, height);
        graphicsContext.strokeRect(topLeftX, topLeftY, width, height);
    }
}
