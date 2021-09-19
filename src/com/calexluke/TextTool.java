package com.calexluke;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class TextTool extends PaintFxTool {

    TextField textField;
    double textFieldX;
    double textFieldY;
    GraphicsContext graphicsContext;

    public TextTool() {
        super();
        makesChangesToCanvas = true;
    }

    public void onMousePressed(MouseEvent e, GraphicsContext graphicsContext) {
        // if text field exists, render text in its position, and remove it
        // if text field does not exist, create it
        this.graphicsContext = graphicsContext;

        StackPane pane = (StackPane) graphicsContext.getCanvas().getParent();
        if (textField != null) {
            renderText();
        } else {
            createNewTextField(e);
        }
    }

    private void renderText() {
        String text = textField.getText();
        graphicsContext.strokeText(text, textFieldX, textFieldY);
        removeTextFieldsFromPane();
        textField = null;
    }

    private void createNewTextField(MouseEvent e) {
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
            if (keyEvent.getCode() == KeyCode.ENTER) { renderText(); }
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
