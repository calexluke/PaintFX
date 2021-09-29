package com.calexluke;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class LassoTool extends  ShapeTool {

    private WritableImage selectedSnapshot;
    private LassoDrawOperation lassoOperation;
    private LassoMode currentMode = LassoMode.SELECTION;

    // distance between mouse click and top left corner of selection rect
    double deltaX;
    double deltaY;

    private enum LassoMode {
        SELECTION,
        DRAGGING
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
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

            // add image draw op to stack
            double relativeWidth = selectedSnapshot.getWidth() / canvas.getWidth();
            double relativeHeight = selectedSnapshot.getHeight() / canvas.getHeight();
            double relativeX = (e.getX() - deltaX) / canvas.getWidth();
            double relativeY = (e.getY() - deltaY) / canvas.getHeight();
            ImageDrawOperation imageDrawOperation = new ImageDrawOperation(selectedSnapshot, relativeX, relativeY, relativeWidth, relativeHeight);
            lassoOperation.setImageDrawOperation(imageDrawOperation);

            canvas.drawImageOnCanvas();
            canvas.reDraw();
            imageDrawOperation.draw(graphicsContext);
            //graphicsContext.drawImage(selectedSnapshot, e.getX(), e.getY(), relativeWidth * canvas.getWidth(), relativeHeight *canvas.getHeight());

            currentMode = LassoMode.SELECTION;

        }



    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();

        if (currentMode == LassoMode.SELECTION) {
            // draw dashed rect
            ShapeDrawOperation operation = createShapeOperation(graphicsContext);
            canvas.drawImageOnCanvas();
            canvas.reDraw();
            operation.draw(graphicsContext);
        } else if (currentMode == LassoMode.DRAGGING) {
            // clear space from selected snapshot
            canvas.drawImageOnCanvas();
            canvas.reDraw();

            // redraw snapshot on canvas in new location
            double localX = e.getX() - deltaX;
            double localY = e.getY() - deltaY;
            graphicsContext.drawImage(selectedSnapshot, localX, localY);
        }


    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {

        if (currentMode == LassoMode.SELECTION) {
            startX = e.getX();
            startY = e.getY();

        } else if (currentMode == LassoMode.DRAGGING) {
            PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
            deltaX = e.getX() - (relativeTopLeftX * canvas.getWidth());
            deltaY = e.getY() - (relativeTopLeftY * canvas.getHeight());

            if (deltaX < 0 || deltaY < 0) {
                // clicked outside of prior selection, select again
                startX = e.getX();
                startY = e.getY();
                currentMode = LassoMode.SELECTION;
            } else {
                // clicked inside selection
                // start drag process
                canvas.pushToUndoStack(lassoOperation);
                canvas.drawImageOnCanvas();
                canvas.reDraw();
            }
        }
    }

    @Override
    protected ShapeDrawOperation createShapeOperation(GraphicsContext graphicsContext) {
        Paint strokeColor = graphicsContext.getStroke();
        Paint fillColor = graphicsContext.getFill();
        RectDrawOperation rectOp = new DashedRectDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight,
                relativeLineWidth, strokeColor, fillColor);
        return rectOp;
    }

    // return the 2d rectangle the user has selected, translated into the parent node's coordinate space
    // used as the viewport for snapshot
    protected Rectangle2D getViewportRect(GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();
        Bounds canvasBoundsInParent = graphicsContext.getCanvas().getBoundsInParent();

        double scaledWidth = relativeWidth * canvasWidth;
        double scaledHeight = relativeHeight * canvasHeight;

        double localTopLeftX = relativeTopLeftX * canvasWidth;
        double localTopLeftY = relativeTopLeftY * canvasHeight;

        // viewport requires coordinates in the parent node's coordinate space
        double topLeftXInParent = localTopLeftX + canvasBoundsInParent.getMinX();
        double topLeftYInParent = localTopLeftY + canvasBoundsInParent.getMinY();

        return new Rectangle2D(topLeftXInParent, topLeftYInParent, scaledWidth, scaledHeight);
    }
}
