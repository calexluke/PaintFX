package com.calexluke;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CropTool extends LassoTool {

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        // starting coords for selection box
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void onDrag(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        calculateAbsoluteShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();

        // draw dashed rect for selection
        ShapeDrawOperation operation = createShapeOperation(graphicsContext);
        canvas.drawImageOnCanvas();
        canvas.reDraw();
        operation.draw(graphicsContext);
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext graphicsContext) {
        calculateRelativeShapeParameters(e.getX(), e.getY(), graphicsContext);
        calculateAbsoluteShapeParameters(e.getX(), e.getY(), graphicsContext);
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();

        // remove dashed rect to take clean snapshot
        canvas.drawImageOnCanvas();
        canvas.reDraw();

        // take snapshot of canvas inside selected area
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setViewport(getViewportRect(graphicsContext));
        parameters.setFill(Color.TRANSPARENT);
        selectedSnapshot = canvas.snapshot(parameters, null);

        // save the operation of clearing the space left by the snapshot, for later use in dragging mode
        lassoOperation = createLassoDrawOperation();

        // add image draw operation to lasso operation
        ImageDrawOperation imageDrawOperation = createImageDrawOperation(e, graphicsContext);
        lassoOperation.setImageDrawOperation(imageDrawOperation);

        canvas.pushToUndoStack(lassoOperation);
        canvas.drawImageOnCanvas();
        canvas.reDraw();
    }

    // operation to draw the snapshot after clearing the canvas
    protected ImageDrawOperation createImageDrawOperation(MouseEvent e, GraphicsContext graphicsContext) {
        PaintFxCanvas canvas = (PaintFxCanvas) graphicsContext.getCanvas();
        double relativeSnapshotWidth = selectedSnapshot.getWidth() / canvas.getWidth();
        double relativeSnapshotHeight = selectedSnapshot.getHeight() / canvas.getHeight();
        return new ImageDrawOperation(selectedSnapshot, relativeTopLeftX, relativeTopLeftY, relativeSnapshotWidth, relativeSnapshotHeight);
    }


    protected LassoDrawOperation createLassoDrawOperation() {
        return new CropDrawOperation(relativeTopLeftX, relativeTopLeftY, relativeWidth, relativeHeight);
    }

    public String toString() {
        return "Crop tool";
    }
}
