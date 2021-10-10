package com.calexluke;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

// lasso tool does the following: displays dashed rect around user-selected area,
// takes snapshot of image inside selected area,
// user can drag and drop the snapshot from the selected area
public class LassoTool extends  ShapeTool {

    private WritableImage selectedSnapshot;
    private LassoDrawOperation lassoOperation;
    private LassoMode currentMode = LassoMode.SELECTION;

    // parameters for the selection box
    double topLeftX;
    double topLeftY;
    double width;
    double height;

    // distance between mouse click and top left corner of selection rect
    double deltaX;
    double deltaY;

    private enum LassoMode {
        SELECTION,
        DRAGGING
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {

        if (currentMode == LassoMode.SELECTION) {
            // starting coords for selection box
            startX = e.getX();
            startY = e.getY();

        } else if (currentMode == LassoMode.DRAGGING) {
            // only happens after selection box defined
            PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
            // topleft x/y were calculated when selection box was drawn
            deltaX = e.getX() - topLeftX;
            deltaY = e.getY() - topLeftY;

            if (deltaX < 0 || deltaY < 0) {
                // clicked outside of prior selection, user starts selecting a new area
                startX = e.getX();
                startY = e.getY();
                currentMode = LassoMode.SELECTION;
            } else {
                // clicked inside selection
                // start drag process
                // lasso operation and snapshot were created at the end of selection process
                canvas.pushToUndoStack(lassoOperation);
                canvas.drawImageOnCanvas();
                canvas.reDraw();
                drawTempImage(e, graphicsContext);
            }
        }
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        calculateAbsoluteShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();

        if (currentMode == LassoMode.SELECTION) {
            // draw dashed rect for selection
            ShapeDrawOperation operation = createShapeOperation(graphicsContext);
            canvas.drawImageOnCanvas();
            canvas.reDraw();
            operation.draw(graphicsContext);

        } else if (currentMode == LassoMode.DRAGGING) {
            // clear space behind selected snapshot
            canvas.drawImageOnCanvas();
            canvas.reDraw();

            // redraw snapshot on canvas in new location
            drawTempImage(e, graphicsContext);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        calculateAbsoluteShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();

        if (currentMode == LassoMode.SELECTION) {
            // remove dashed rect to take clean snapshot
            canvas.drawImageOnCanvas();
            canvas.reDraw();

            // take snapshot of canvas inside selected area, save for use in dragging mode
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setViewport(getViewportRect(graphicsContext));
            parameters.setFill(Color.TRANSPARENT);
            selectedSnapshot = canvas.snapshot(parameters, null);

            // save the operation of clearing the space left by the snapshot, for later use in dragging mode
            lassoOperation = new LassoDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);

            // redraw dashed rect
            ShapeDrawOperation dashedRectOperation = createShapeOperation(graphicsContext);
            dashedRectOperation.draw(graphicsContext);

            // toggle mode
            currentMode = LassoMode.DRAGGING;

        } else if (currentMode == LassoMode.DRAGGING) {

            // add image draw operation to lasso operation
            ImageDrawOperation imageDrawOperation = createImageDrawOperation(e, graphicsContext);
            lassoOperation.setImageDrawOperation(imageDrawOperation);

            canvas.drawImageOnCanvas();
            canvas.reDraw();

            // toggle mode
            currentMode = LassoMode.SELECTION;
        }
    }


    // creates the dashed rectangle shape for the selection box
    @Override
    protected ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        RectDrawOperation rectOp = new DashedRectDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return rectOp;
    }

    // operation to draw the snapshot after it has been moved
    private ImageDrawOperation createImageDrawOperation(MouseEvent e, GraphicsContext graphicsContext) {
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        double relativeSnapshotWidth = selectedSnapshot.getWidth() / canvas.getWidth();
        double relativeSnapshotHeight = selectedSnapshot.getHeight() / canvas.getHeight();
        double relativeX = (e.getX() - deltaX) / canvas.getWidth();
        double relativeY = (e.getY() - deltaY) / canvas.getHeight();
        return new ImageDrawOperation(selectedSnapshot, relativeX, relativeY, relativeSnapshotWidth, relativeSnapshotHeight);
    }

    /**
     * Returns the 2d rectangle the user has selected, translated into the parent node's coordinate space.
     * Used as the viewport for snapshot
     * @param graphicsContext GraphicsContext
     * @return the rectangle representing the area the user has selected
     */
    protected Rectangle2D getViewportRect(GraphicsContext graphicsContext) {
        Bounds canvasBoundsInParent = graphicsContext.getCanvas().getBoundsInParent();

        // viewport requires coordinates in the parent node's coordinate space
        double topLeftXInParent = topLeftX + canvasBoundsInParent.getMinX();
        double topLeftYInParent = topLeftY + canvasBoundsInParent.getMinY();

        return new Rectangle2D(topLeftXInParent, topLeftYInParent, width, height);
    }

    /**
     *   Calculate the parameters used to draw rectangles, squares, ovals, circles, etc.
     *   Pass in co-ords from where the user released the mouse.
     * @param endX double, x value where the mouse currently is
     * @param endY double, y value where the mouse currently is
     * @param graphicsContext GraphicsContext
     */
    protected void calculateAbsoluteShapeParameters(double endX, double endY, GraphicsContext graphicsContext) {
        // calculate where the top left corner of the shape should be
        double xDifference = endX - startX;
        double yDifference = endY - startY;
        double x = (xDifference > 0) ? startX : endX;
        double y = (yDifference > 0) ? startY : endY;
        topLeftX = x;
        topLeftY = y;

        // calculate side lengths
        width = Math.abs(endX - startX);
        height = Math.abs(endY - startY);
    }

    /**
     * Draw the selected snapshot on canvas at the current position (during dragging)
     * @param e MouseEvent
     * @param graphicsContext GraphicsContext
     */
    private void drawTempImage(MouseEvent e, GraphicsContext graphicsContext) {
        // redraw snapshot on canvas in new location
        if (selectedSnapshot != null) {
            double localX = e.getX() - deltaX;
            double localY = e.getY() - deltaY;
            graphicsContext.drawImage(selectedSnapshot, localX, localY);
        }
    }
}
