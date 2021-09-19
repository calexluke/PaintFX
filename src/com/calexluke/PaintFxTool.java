package com.calexluke;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;

// parent class for all the drawing tools.

public class PaintFxTool {

    // used to keep track of unsaved changes. Some tools will override this
    Boolean makesChangesToCanvas = false;

    // Individual tool subclasses will override the onClick methods.
    // Default implementation is to do nothing on click
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) { }
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) { }
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) { }

}
