package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

// tool for freehand drawing on canvas

public class PencilTool extends PaintFxTool {

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.beginPath();
        graphicsContext.moveTo(e.getX(), e.getY());
    }

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.closePath();
    }

    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.lineTo(e.getX(), e.getY());
        graphicsContext.stroke();
    }
}


