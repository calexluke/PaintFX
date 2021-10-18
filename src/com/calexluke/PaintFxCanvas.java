package com.calexluke;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;

// class for handling drawing and other canvas-related things

public class PaintFxCanvas extends Canvas {

    private GraphicsContext graphicsContext;
    private StateManager stateManager;
    private ImageView imageView;

    /**
     * This array stores each "operation"/drawing event done on the canvas. This can be used for undo/redo, as well as redrawing
     * the canvas at a new scale after zoom in/zoom out
     */
    private ArrayList<DrawOperation> operations = new ArrayList<>();
    private ArrayList<DrawOperation> redoStack = new ArrayList<>();

    public PaintFxCanvas(double initialWidth, double initialHeight, StateManager stateManager) {
        super(initialWidth, initialHeight);
        this.stateManager = stateManager;
        graphicsContext = this.getGraphicsContext2D();

        // initial values. These will change dynamically as user selects tools
        graphicsContext.setFill(Color.BLACK);
        setOnClickListeners();
    }

    public ImageView getImageView() {
        return imageView;
    }
    public StateManager getStateManager() { return stateManager; }
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public boolean isResizable() {
        return true;
    }

    //region Drawing

    /**
     * called when canvas is rescaled to re-generate all the drawings at the new scale.
     */
    public void reDraw() {
        System.out.println("Redrawing operation stack: ");
        for (DrawOperation operation : operations) {
            System.out.println("Draw " + operation);
            operation.draw(graphicsContext);
        }
    }

    public void drawImageOnCanvas() {
        Image image = imageView.getImage();
        clearGraphicsContext();
        graphicsContext.drawImage(image, 0, 0, this.getWidth(), this.getHeight());
    }

    // called when user wants to draw a graphic - ensure color and stroke width are up to date with user preferences
    public void updateGraphicsContext() {
        updateStrokeWidth();
        updateColors();
    }

    public void clearOperationsList() {
        operations.clear();
    }

    public void clearGraphicsContext() {
        graphicsContext.clearRect(0, 0, this.getWidth(), this.getHeight());
    }

    private void updateStrokeWidth() {
        switch (stateManager.getSelectedStrokeWidth()) {
            case THIN:
                graphicsContext.setLineWidth(2);
                break;
            case MEDIUM:
                graphicsContext.setLineWidth(5);
                break;
            case WIDE:
                graphicsContext.setLineWidth(10);
                break;
            default:
                graphicsContext.setLineWidth(2);
        }
    }

    private void updateColors() {
        Color strokeColor = stateManager.getStrokeColor();
        Color fillColor = stateManager.getFillColor();
        graphicsContext.setStroke(strokeColor);
        graphicsContext.setFill(fillColor);
    }

    //endregion

    //region Stacks

    public void undoDrawOperation() {
        DrawOperation opToUndo = popFromUndoStack();
        if (opToUndo != null) {
            pushToRedoStack(opToUndo);
            drawImageOnCanvas();
            reDraw();
        }
    }

    public void redoDrawOperation() {
        DrawOperation opToRedo = popFromRedoStack();
        if (opToRedo != null) {
            pushToUndoStack(opToRedo);
            drawImageOnCanvas();
            reDraw();
        }
    }

    public void pushToUndoStack(DrawOperation operation) {
        operations.add(operation);
    }

    public void pushToRedoStack(DrawOperation operation) {
        redoStack.add(operation);
    }

    public DrawOperation popFromUndoStack() {
        return popDrawOperation(operations);
    }

    public DrawOperation popFromRedoStack() {
        return popDrawOperation(redoStack);
    }

    private DrawOperation popDrawOperation(ArrayList<DrawOperation> stack) {
        if (stack.size() > 0) {
            int lastIndex = stack.size() - 1;
            DrawOperation result = stack.get(lastIndex);
            stack.remove(lastIndex);
            return result;
        } else {
            return null;
        }
    }

    //endregion

    //region OnClick

    private void setOnClickListeners() {
        this.setOnMouseDragged(this::onDrag);
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseReleased(this::onMouseReleased);
    }

    private void onMouseReleased(MouseEvent e) {
        updateGraphicsContext();
        PaintFxTool tool = stateManager.getSelectedTool();
        tool.onMouseReleased(e, graphicsContext);
        if (tool.makesChangesToCanvas) {
            stateManager.setUnsavedChangesAtCurrentIndex(true);
        }
    }

    private void onDrag(MouseEvent e) {
        updateGraphicsContext();
        PaintFxTool tool = stateManager.getSelectedTool();
        tool.onDrag(e, graphicsContext);
        if (tool.makesChangesToCanvas) {
            stateManager.setUnsavedChangesAtCurrentIndex(true);
        }
    }

    private void onMousePressed(MouseEvent e) {
        updateGraphicsContext();
        PaintFxTool tool = stateManager.getSelectedTool();
        tool.onMousePressed(e, graphicsContext);
        if (tool.makesChangesToCanvas) {
            stateManager.setUnsavedChangesAtCurrentIndex(true);
        }
    }

    //endregion

    //region Image size and scale

    public void scaleImage(double newWidth, double newHeight) {
        if (imageView != null) {
            imageView.setFitHeight(newHeight);
            imageView.setFitWidth(newWidth);

            // use the imageView aspect fit properties to determine canvas size
            scaleCanvasToImageSize();
            drawImageOnCanvas();
            reDraw();
        }
    }

    private void scaleCanvasToImageSize() {
        // workaround for aspect ratio issues - need to find the actual width and height of the view. One or the other
        // will be scaled to maintain aspect ratio, so you can't read it directly off of the view object.
        // from https://stackoverflow.com/questions/39408845/how-to-get-width-height-of-displayed-image-in-javafx-imageview
        setWidth(getActualImageViewWidth());
        setHeight(getActualImageViewHeight());
    }

    private double getActualImageViewHeight() {
        double aspectRatio = imageView.getImage().getWidth() / imageView.getImage().getHeight();
        double imageViewHeight = Math.min(imageView.getFitHeight(), imageView.getFitWidth() / aspectRatio);
        return imageViewHeight;
    }

    private double getActualImageViewWidth() {
        double aspectRatio = imageView.getImage().getWidth() / imageView.getImage().getHeight();
        double imageViewWidth = Math.min(imageView.getFitWidth(), imageView.getFitHeight() * aspectRatio);
        return imageViewWidth;
    }

    //endregion
}
