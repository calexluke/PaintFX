package com.calexluke;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class TextTool extends PaintFxTool {

    TextField textField;
    double textFieldX;
    double textFieldY;
    double relativeX;
    double relativeY;
    double relativeFontSize;
    GraphicsContext graphicsContext;

    public TextTool() {
        super();
        makesChangesToCanvas = true;
    }

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        // if text field exists, render text in its position, and remove it
        // if text field does not exist, create it
        this.graphicsContext = graphicsContext;

        StackPane pane = (StackPane) graphicsContext.getCanvas().getParent();
        if (textField != null) {
            calculateScaledParameters(e, graphicsContext);
            createTextDrawOperation(graphicsContext, operations);
            removeTextFieldsFromPane();
            textField = null;
        } else {
            createNewTextField(e, operations);
        }
    }

    private void calculateScaledParameters(MouseEvent e, GraphicsContext graphicsContext) {
        double canvasWidth = graphicsContext.getCanvas().getWidth();
        double canvasHeight = graphicsContext.getCanvas().getHeight();

        // get the x/y co-ords scaled to the dimensions of canvas
        relativeX = e.getX() / canvasWidth;
        relativeY = e.getY() / canvasHeight;
        relativeFontSize = Constants.DEFAULT_FONT_SIZE / canvasWidth;
    }

    private void createTextDrawOperation(GraphicsContext graphicsContext, ArrayList<DrawOperation> operations) {
        Paint color = graphicsContext.getStroke();
        String text = textField.getText();

        // add operation to array for undo/redo and scaling
        TextDrawOperation textOp = new TextDrawOperation(text, relativeX, relativeY, relativeFontSize, color);
        textOp.draw(graphicsContext);
        operations.add(textOp);
    }

    private void createNewTextField(MouseEvent e, ArrayList<DrawOperation> operations) {
        StackPane pane = (StackPane) graphicsContext.getCanvas().getParent();
        textField = new TextField();

        textFieldX = e.getX();
        textFieldY = e.getY();

        Point2D localPoint = graphicsContext.getCanvas().localToParent(e.getX(), e.getY());
        double centerY = pane.getHeight()/2;

        textField.setTranslateX(localPoint.getX());
        textField.setTranslateY(localPoint.getY() - centerY);

        pane.getChildren().add(textField);
        textField.setPrefSize(200, 20);

        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                calculateScaledParameters(e, graphicsContext);
                createTextDrawOperation(graphicsContext, operations);
                removeTextFieldsFromPane();
                textField = null;
            }
        });
    }

    public void removeTextFieldsFromPane() {
        if (graphicsContext != null) {
            StackPane pane = (StackPane) graphicsContext.getCanvas().getParent();
            ObservableList<Node> children = pane.getChildren();
            if (children.contains(textField)) {
                children.remove(textField);
            }
        }
    }
}
