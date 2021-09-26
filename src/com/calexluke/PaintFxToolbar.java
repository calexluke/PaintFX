package com.calexluke;

import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PaintFxToolbar extends ToolBar {

    private StateManager stateManager;
    private VBox toolVbox;
    private ColorPicker strokeColorPicker;
    private ColorPicker fillColorPicker;
    private Slider polygonSlider;

    public PaintFxToolbar(StateManager stateManager) {
        super();
        this.stateManager = stateManager;
        // container for toolbar elements
        // constructor param is default space between elements
        toolVbox = new VBox(5);
        this.getItems().add(toolVbox);
        this.setOrientation(Orientation.VERTICAL);
        configurePolygonSlider();
        configureToggleButtons();
        configureComboBox();
        configureColorPickers();
    }

    private void configurePolygonSlider() {
        polygonSlider = new Slider();
        polygonSlider.setMin(3);
        polygonSlider.setMax(13);
        polygonSlider.setValue(5);
        polygonSlider.setMajorTickUnit(2);
        polygonSlider.setMinorTickCount(1);
        polygonSlider.setShowTickLabels(true);
        polygonSlider.setShowTickMarks(true);
        polygonSlider.setSnapToTicks(true);
        polygonSlider.valueProperty().addListener((options, oldValue, newValue) -> stateManager.setSelectedPolygonSides((int)polygonSlider.getValue()));
    }

    private void configureToggleButtons() {
        // toggle group allows selection of only one button at a time
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton mouseButton = new ToggleButton("Mouse");
        ToggleButton pencilButton = new ToggleButton("Pencil");
        ToggleButton eraseButton = new ToggleButton("Eraser");
        ToggleButton lineButton = new ToggleButton("Line");
        ToggleButton squareButton = new ToggleButton("Square");
        ToggleButton rectButton = new ToggleButton("Rectangle");
        ToggleButton roundedRectButton = new ToggleButton("Rounded Rectangle");
        ToggleButton circleButton = new ToggleButton("Circle");
        ToggleButton ovalButton = new ToggleButton("Oval");
        ToggleButton polygonButton = new ToggleButton("Polygon");
        ToggleButton textButton = new ToggleButton("Text");
        ToggleButton colorGrabButton = new ToggleButton("Grab Color");

        // all buttons will be the width of the VBox
        mouseButton.setMaxWidth(Double.MAX_VALUE);
        pencilButton.setMaxWidth(Double.MAX_VALUE);
        eraseButton.setMaxWidth(Double.MAX_VALUE);
        lineButton.setMaxWidth(Double.MAX_VALUE);
        squareButton.setMaxWidth(Double.MAX_VALUE);
        rectButton.setMaxWidth(Double.MAX_VALUE);
        roundedRectButton.setMaxWidth(Double.MAX_VALUE);
        circleButton.setMaxWidth(Double.MAX_VALUE);
        ovalButton.setMaxWidth(Double.MAX_VALUE);
        polygonButton.setMaxWidth(Double.MAX_VALUE);
        colorGrabButton.setMaxWidth(Double.MAX_VALUE);
        textButton.setMaxWidth(Double.MAX_VALUE);

        mouseButton.setOnAction(e -> stateManager.setSelectedTool(new MouseTool()));
        pencilButton.setOnAction(e -> stateManager.setSelectedTool(new PencilTool()));
        eraseButton.setOnAction(e -> stateManager.setSelectedTool(new EraserTool()));
        lineButton.setOnAction(e -> stateManager.setSelectedTool(new LineTool()));
        squareButton.setOnAction(e -> stateManager.setSelectedTool(new SquareTool()));
        rectButton.setOnAction(e -> stateManager.setSelectedTool(new RectTool()));
        roundedRectButton.setOnAction(e -> stateManager.setSelectedTool(new RoundedRectTool()));
        circleButton.setOnAction(e -> stateManager.setSelectedTool(new CircleTool()));
        ovalButton.setOnAction(e -> stateManager.setSelectedTool(new OvalTool()));
        polygonButton.setOnAction(e -> stateManager.setSelectedTool(new PolygonTool()));
        textButton.setOnAction(e -> stateManager.setSelectedTool(new TextTool()));
        colorGrabButton.setOnAction(e -> stateManager.setSelectedTool(new ColorGrabTool(strokeColorPicker, stateManager)));

        toggleGroup.getToggles().add(mouseButton);
        toggleGroup.getToggles().add(pencilButton);
        toggleGroup.getToggles().add(eraseButton);
        toggleGroup.getToggles().add(lineButton);
        toggleGroup.getToggles().add(squareButton);
        toggleGroup.getToggles().add(rectButton);
        toggleGroup.getToggles().add(roundedRectButton);
        toggleGroup.getToggles().add(circleButton);
        toggleGroup.getToggles().add(ovalButton);
        toggleGroup.getToggles().add(polygonButton);
        toggleGroup.getToggles().add(textButton);
        toggleGroup.getToggles().add(colorGrabButton);

        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(mouseButton);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(pencilButton);
        toolVbox.getChildren().add(eraseButton);
        toolVbox.getChildren().add(lineButton);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(squareButton);
        toolVbox.getChildren().add(rectButton);
        toolVbox.getChildren().add(roundedRectButton);
        toolVbox.getChildren().add(circleButton);
        toolVbox.getChildren().add(ovalButton);
        toolVbox.getChildren().add(polygonSlider);
        toolVbox.getChildren().add(polygonButton);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(textButton);
        toolVbox.getChildren().add(colorGrabButton);
        toolVbox.getChildren().add(new Label(" "));
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
        toolVbox.getChildren().add(new Label("Stroke Width"));
        toolVbox.getChildren().add(strokeWidthComboBox);
    }

    private void configureColorPickers() {
        strokeColorPicker = new ColorPicker();
        fillColorPicker = new ColorPicker();

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

        toolVbox.getChildren().add(new Label("Stroke Color"));
        toolVbox.getChildren().add(strokeColorPicker);

        toolVbox.getChildren().add(new Label("Fill Color"));
        toolVbox.getChildren().add(fillColorPicker);
    }
}
