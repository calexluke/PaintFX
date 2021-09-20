package com.calexluke;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class TextDrawOperation implements DrawOperation {

    double x;
    double y;
    double fontSize;

    double scaledX;
    double scaledY;
    double scaledFontSize;
    Paint textColor;
    String text;

    public TextDrawOperation(String text, double x, double y, double fontSize, Paint textColor) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
        this.textColor = textColor;
    }

    public void draw(GraphicsContext graphicsContext) {
        scaleParamsToCanvasSize(graphicsContext);
        graphicsContext.setFill(textColor);
        graphicsContext.setFont(new Font(scaledFontSize));
        graphicsContext.fillText(text, scaledX, scaledY);
    }

    private void scaleParamsToCanvasSize(GraphicsContext graphicsContext) {
        double width = graphicsContext.getCanvas().getWidth();
        double height = graphicsContext.getCanvas().getHeight();

        // get absolute co-ords by multiplying by current dimensions
        scaledX = x * width;
        scaledY = y * height;
        scaledFontSize = fontSize * width;
    }
}
