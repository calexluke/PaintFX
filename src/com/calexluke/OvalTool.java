package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class OvalTool extends ShapeTool {

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateShapeParameters(e.getX(), e.getY());
        graphicsContext.fillOval(topLeftX, topLeftY, width, height);
        graphicsContext.strokeOval(topLeftX, topLeftY, width, height);
    }
}
