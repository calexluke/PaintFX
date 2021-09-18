package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class SquareTool extends PaintFxTool {

    // coords of the initial point user clicked
    double startX;
    double startY;

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        startX = e.getX();
        startY = e.getY();
    }

    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        double endX = e.getX();
        double endY = e.getY();

        double topLeftX = findTopLeftX(startX, endX);
        double topLeftY = findTopLeftY(startY, endY);
        double xLength = Math.abs(endX - startX);
        double yLength = Math.abs(endY - startY);
        double sideLength = Math.max(xLength, yLength);

        graphicsContext.fillRect(topLeftX, topLeftY, sideLength, sideLength);
    }

    // return X coord of top left of the square depending on mouse position
    private double findTopLeftX(double startX, double endX) {
        double difference = endX - startX;
        return (difference > 0) ? startX : endX;
    }

    // return Y coord of top left of the square depending on mouse position
    private double findTopLeftY(double startY, double endY) {
        double difference = endY - startY;
        return (difference > 0) ? startY : endY;
    }
}
