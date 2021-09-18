package com.calexluke;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PaintFxToolbar extends ToolBar {

    private StateManager stateManager;
    private VBox toolVbox;

    public PaintFxToolbar(StateManager stateManager) {
        super();
        this.stateManager = stateManager;
        // container for toolbar elements
        // constructor param is default space between elements
        toolVbox = new VBox(5);
        this.getItems().add(toolVbox);
        this.setOrientation(Orientation.VERTICAL);
        configureToggleButtons();
        configureComboBox();
        configureColorPickers();
    }

    private void configureToggleButtons() {
        // toggle group allows selection of only one button at a time
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton mouseButton = new ToggleButton("Mouse");
        ToggleButton pencilButton = new ToggleButton("Pencil");
        ToggleButton lineButton = new ToggleButton("Line");
        ToggleButton squareButton = new ToggleButton("Square");

        // all buttons will be the width of the VBox
        mouseButton.setMaxWidth(Double.MAX_VALUE);
        pencilButton.setMaxWidth(Double.MAX_VALUE);
        lineButton.setMaxWidth(Double.MAX_VALUE);
        squareButton.setMaxWidth(Double.MAX_VALUE);

        mouseButton.setOnAction(e -> stateManager.setSelectedTool(new MouseTool()));
        pencilButton.setOnAction(e -> stateManager.setSelectedTool(new PencilTool()));
        lineButton.setOnAction(e -> stateManager.setSelectedTool(new LineTool()));
        squareButton.setOnAction(e -> stateManager.setSelectedTool(new SquareTool()));

        toggleGroup.getToggles().add(mouseButton);
        toggleGroup.getToggles().add(pencilButton);
        toggleGroup.getToggles().add(lineButton);
        toggleGroup.getToggles().add(squareButton);
        toolVbox.getChildren().add(mouseButton);
        toolVbox.getChildren().add(pencilButton);
        toolVbox.getChildren().add(lineButton);
        toolVbox.getChildren().add(squareButton);
    }

    private void configureComboBox() {
        ComboBox strokeWidthComboBox = new ComboBox();
        strokeWidthComboBox.setMaxWidth(Double.MAX_VALUE);
        strokeWidthComboBox.getItems().addAll (
                StateManager.StrokeWidth.THIN,
                StateManager.StrokeWidth.MEDIUM,
                StateManager.StrokeWidth.WIDE);
        strokeWidthComboBox.setValue(StateManager.StrokeWidth.THIN);
        strokeWidthComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            stateManager.setSelectedStrokeWidth((StateManager.StrokeWidth) newValue);
        });
        toolVbox.getChildren().add(strokeWidthComboBox);
    }

    private void configureColorPickers() {
        ColorPicker strokeColorPicker = new ColorPicker();
        ColorPicker fillColorPicker = new ColorPicker();

        strokeColorPicker.setMaxWidth(Double.MAX_VALUE);
        fillColorPicker.setMaxWidth(Double.MAX_VALUE);

        strokeColorPicker.getStyleClass().add("button");
        fillColorPicker.getStyleClass().add("button");

        strokeColorPicker.setValue(Color.BLACK);
        fillColorPicker.setValue(Color.BLACK);

        strokeColorPicker.setOnAction(colorEvent -> {
            Color chosenColor = strokeColorPicker.getValue();
            stateManager.setStrokeColor(chosenColor);
        });
        fillColorPicker.setOnAction(colorEvent -> {
            Color chosenColor = fillColorPicker.getValue();
            stateManager.setFillColor(chosenColor);
        });

        Label strokeColorLabel = new Label("Stroke Color");
        Label fillColorLabel = new Label("Fill Color");

        toolVbox.getChildren().add(strokeColorLabel);
        toolVbox.getChildren().add(strokeColorPicker);
        toolVbox.getChildren().add(fillColorLabel);
        toolVbox.getChildren().add(fillColorPicker);
    }
}
