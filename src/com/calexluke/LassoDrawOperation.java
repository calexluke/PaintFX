package com.calexluke;
import javafx.scene.paint.Color;

/**
 * lasso operation does two things: clears a rectangle under the area the user selected
 * draws the snapshot of what used to be in that space in a new location (image draw operation)
 */

public class LassoDrawOperation extends RectDrawOperation {

    ImageDrawOperation imageDrawOperation;

    public LassoDrawOperation(double x, double y, double width, double height) {
        super(x, y, width, height, 0, Color.TRANSPARENT, Color.TRANSPARENT);
    }

    public void setImageDrawOperation(ImageDrawOperation operation) {
        imageDrawOperation = operation;
    }
}
