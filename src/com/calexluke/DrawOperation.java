package com.calexluke;

import javafx.scene.canvas.GraphicsContext;

// maintain array of canvas drawing operations
// used for canvas resize and undo/redo
// concept from:
// https://stackoverflow.com/questions/40638845/implementing-undo-redo-in-javafx

public interface DrawOperation {
    void draw(GraphicsContext graphicsContext);
}
