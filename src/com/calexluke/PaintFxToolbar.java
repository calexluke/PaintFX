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
        configureColorPicker();
    }

    private void configureToggleButtons() {
        // toggle group allows selection of only one button at a time
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton mouseButton = new ToggleButton("Mouse");
        ToggleButton pencilButton = new ToggleButton("Pencil");
        ToggleButton lineButton = new ToggleButton("Line");

        // all buttons will be the width of the VBox
        mouseButton.setMaxWidth(Double.MAX_VALUE);
        pencilButton.setMaxWidth(Double.MAX_VALUE);
        lineButton.setMaxWidth(Double.MAX_VALUE);

        mouseButton.setOnAction(e -> stateManager.setSelectedTool(new MouseTool()));
        pencilButton.setOnAction(e -> stateManager.setSelectedTool(new PencilTool()));
        lineButton.setOnAction(e -> stateManager.setSelectedTool(new LineTool()));

        toggleGroup.getToggles().add(mouseButton);
        toggleGroup.getToggles().add(pencilButton);
        toggleGroup.getToggles().add(lineButton);
        toolVbox.getChildren().add(mouseButton);
        toolVbox.getChildren().add(pencilButton);
        toolVbox.getChildren().add(lineButton);
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

    private void configureColorPicker() {
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setMaxWidth(Double.MAX_VALUE);
        colorPicker.getStyleClass().add("button");
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(colorEvent -> {
            Color chosenColor = colorPicker.getValue();
            stateManager.setSelectedColor(chosenColor);
        });
        toolVbox.getChildren().add(colorPicker);
    }
}
