package com.calexluke;

import javafx.scene.control.ScrollBar;


public class PaintFxScrollBar extends ScrollBar {

    public PaintFxScrollBar() {
        super();
    }

    /**
     *      The scroll bar will translate the image by the scrollbar's value.
     *     Update methods will dynamically update the bounds of the scroll bar based on how far the image is "offscreen."
     *     The lower and upper bounds of the scrollbar will be the amount the image needs to be translated to be back
     *     "onscreen." Pass in the image's actual bounds, as well as the bounds of where it should be to stay in frame.
     * @param actualMin double
     * @param actualMax double
     * @param intendedMin double
     * @param intendedMax double
     */
    public void updateScrollBarBounds(double actualMin, double actualMax, double intendedMin, double intendedMax) {

        // Amount the image needs to be translated to be back in frame
        double newScrollBarMin = -(intendedMin - actualMin);
        double newScrollBarMax = actualMax - intendedMax;
        setScrollBarBounds(newScrollBarMin, newScrollBarMax);
    }

    private void setScrollBarBounds(double min, double max) {
        setMin(min);
        setMax(max);
        setValue(0);

        // scrollbar thumb takes up half of the current range
        double range = max - min;
        setVisibleAmount(range / 2);
    }
}
