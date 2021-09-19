package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

// tool for freehand drawing on canvas

public class PencilTool extends PaintFxTool {

    public PencilTool() {
        super();
        makesChangesToCanvas = true;
    }

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.moveTo(e.getX(), e.getY());
        graphicsContext.beginPath();
    }

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.stroke();
    }

    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.lineTo(e.getX(), e.getY());
        graphicsContext.stroke();
    }

}


