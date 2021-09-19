package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;

// tool for drawing a straight line on  the canvas

public class LineTool extends PaintFxTool {

    public LineTool() {
        super();
        makesChangesToCanvas = true;
    }

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.beginPath();
        graphicsContext.moveTo(e.getX(), e.getY());
    }

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        graphicsContext.lineTo(e.getX(), e.getY());
        graphicsContext.closePath();
        graphicsContext.stroke();
    }

    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        // line tool currently does nothing when dragging
    }
}

