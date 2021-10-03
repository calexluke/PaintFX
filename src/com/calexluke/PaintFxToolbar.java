package com.calexluke;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class PaintFxToolbar extends ToolBar {

    private StateManager stateManager;
    private VBox toolVbox;
    private ColorPicker strokeColorPicker;
    private ColorPicker fillColorPicker;
    private Slider polygonSlider;

    private Slider timerSlider;
    private HBox timerLabelHBox;
    private Label timerLabel;
    private Label autoSaveLabel;
    private boolean timerLabelHidden = false;

    private ShapeTool selectedShapeTool;
    private ComboBox shapeToolComboBox;
    private HashMap<String, ShapeTool> shapeToolsMap;


    public PaintFxToolbar(StateManager stateManager) {
        super();
        this.stateManager = stateManager;
        // container for toolbar elements
        // constructor param is default space between elements
        toolVbox = new VBox(5);
        this.getItems().add(toolVbox);
        this.setOrientation(Orientation.VERTICAL);
        configureShapeToolMap();
        configurePolygonSlider();
        configureShapeToolComboBox();
        configureToggleButtons();
        configureStrokeWidthComboBox();
        configureColorPickers();
        configureTimerSection();
        configureTimerListener();

        // initial value
        selectedShapeTool = shapeToolsMap.get(shapeToolComboBox.getValue());
    }

    private void configureShapeToolMap() {
        shapeToolsMap = new HashMap<>();
        shapeToolsMap.put(Constants.SQUARE_SHAPE, new SquareTool());
        shapeToolsMap.put(Constants.RECTANGLE_SHAPE, new RectTool());
        shapeToolsMap.put(Constants.ROUNDED_RECT_SHAPE, new RoundedRectTool());
        shapeToolsMap.put(Constants.CIRCLE_SHAPE, new CircleTool());
        shapeToolsMap.put(Constants.OVAL_SHAPE, new OvalTool());
        shapeToolsMap.put(Constants.POLYGON_SHAPE, new PolygonTool());
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
        ToggleButton shapeToolButton = new ToggleButton("Shape");
        ToggleButton textButton = new ToggleButton("Text");
        ToggleButton lassoButton = new ToggleButton("Lasso");
        ToggleButton colorGrabButton = new ToggleButton("Grab Color");

        // all buttons will be the width of the VBox
        mouseButton.setMaxWidth(Double.MAX_VALUE);
        pencilButton.setMaxWidth(Double.MAX_VALUE);
        eraseButton.setMaxWidth(Double.MAX_VALUE);
        lineButton.setMaxWidth(Double.MAX_VALUE);

        shapeToolButton.setMaxWidth(Double.MAX_VALUE);
        colorGrabButton.setMaxWidth(Double.MAX_VALUE);
        textButton.setMaxWidth(Double.MAX_VALUE);
        lassoButton.setMaxWidth(Double.MAX_VALUE);

        mouseButton.setOnAction(e -> stateManager.setSelectedTool(new MouseTool()));
        pencilButton.setOnAction(e -> stateManager.setSelectedTool(new PencilTool()));
        eraseButton.setOnAction(e -> stateManager.setSelectedTool(new EraserTool()));
        lineButton.setOnAction(e -> stateManager.setSelectedTool(new LineTool()));

        shapeToolButton.setOnAction(e -> stateManager.setSelectedTool(selectedShapeTool));
        textButton.setOnAction(e -> stateManager.setSelectedTool(new TextTool()));
        lassoButton.setOnAction(e -> stateManager.setSelectedTool(new LassoTool()));
        colorGrabButton.setOnAction(e -> stateManager.setSelectedTool(new ColorGrabTool(strokeColorPicker, stateManager)));

        toggleGroup.getToggles().add(mouseButton);
        toggleGroup.getToggles().add(pencilButton);
        toggleGroup.getToggles().add(eraseButton);
        toggleGroup.getToggles().add(lineButton);
        toggleGroup.getToggles().add(shapeToolButton);
        toggleGroup.getToggles().add(textButton);
        toggleGroup.getToggles().add(lassoButton);
        toggleGroup.getToggles().add(colorGrabButton);

        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(mouseButton);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(pencilButton);
        toolVbox.getChildren().add(eraseButton);
        toolVbox.getChildren().add(lineButton);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(shapeToolButton);
        toolVbox.getChildren().add(shapeToolComboBox);
        toolVbox.getChildren().add(new Label("Polygon sides:"));
        toolVbox.getChildren().add(polygonSlider);
        toolVbox.getChildren().add(new Label(" "));
        toolVbox.getChildren().add(textButton);
        toolVbox.getChildren().add(lassoButton);
        toolVbox.getChildren().add(colorGrabButton);
        toolVbox.getChildren().add(new Label(" "));
    }

    private void configureShapeToolComboBox() {
        shapeToolComboBox = new ComboBox<String>();
        shapeToolComboBox.setMaxWidth(Double.MAX_VALUE);
        shapeToolComboBox.getItems().addAll (
                Constants.SQUARE_SHAPE,
                Constants.RECTANGLE_SHAPE,
                Constants.ROUNDED_RECT_SHAPE,
                Constants.CIRCLE_SHAPE,
                Constants.OVAL_SHAPE,
                Constants.POLYGON_SHAPE
        );
        shapeToolComboBox.setValue(Constants.SQUARE_SHAPE);
        shapeToolComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            selectedShapeTool = shapeToolsMap.get(newValue);
            stateManager.setSelectedTool(selectedShapeTool);
        });
    }

    private void configureStrokeWidthComboBox() {
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

    //region Timer stuff

    private void configureTimerSection() {
        autoSaveLabel = new Label("Auto-Save in:    ");
        timerLabel = new Label(getTimerString());
        timerLabelHBox = new HBox();
        timerLabelHBox.setMaxWidth(Double.MAX_VALUE);
        timerLabelHBox.setAlignment(Pos.CENTER);
        timerLabelHBox.getChildren().add(autoSaveLabel);
        timerLabelHBox.getChildren().add(timerLabel);
        configureTimerSlider();

        Button showHideButton = new Button("Show/Hide Timer");
        showHideButton.setMaxWidth(Double.MAX_VALUE);
        showHideButton.setOnAction(e -> toggleTimerLabelHidden());

        toolVbox.getChildren().add(new Label(""));
        toolVbox.getChildren().add(timerLabelHBox);
        toolVbox.getChildren().add(timerSlider);
        toolVbox.getChildren().add(showHideButton);
    }

    private void toggleTimerLabelHidden() {
        timerLabelHBox.getChildren().remove(autoSaveLabel);
        timerLabelHBox.getChildren().remove(timerLabel);

        // toggle value
        timerLabelHidden = !timerLabelHidden;

        // act on new value
        if (timerLabelHidden) {
            // add spacer label
            timerLabelHBox.getChildren().add(new Label(""));
        } else {
            timerLabelHBox.getChildren().add(autoSaveLabel);
            timerLabelHBox.getChildren().add(timerLabel);
        }
    }

    private void configureTimerSlider() {
        int startingValue = stateManager.getAutoSaveCounterMax() / 60;
        timerSlider = new Slider();
        timerSlider.setMin(2);
        timerSlider.setMax(5);
        timerSlider.setValue(startingValue);
        timerSlider.setMajorTickUnit(.5);
        timerSlider.setMinorTickCount(0);
        timerSlider.setShowTickLabels(true);
        timerSlider.setShowTickMarks(true);
        timerSlider.setSnapToTicks(true);

        // update timer after selection is done
        timerSlider.setOnMouseReleased(event -> {
            System.out.println("User selected " + timerSlider.getValue());
            int seconds = (int)(timerSlider.getValue() * 60);
            stateManager.setAutoSaveCounterMax(seconds);
        });
    }

    private void configureTimerListener() {
        stateManager.autoSaveCounterProperty().addListener((o, oldValue, newValue) -> {
            //System.out.println("Counter changed! New value: " + newValue);
            timerLabel.setText(getTimerString());
        });
    }

    private String getTimerString() {
        int counter = stateManager.getAutoSaveCounter();
        int remainder = counter % 60;
        int minutes = counter / 60;
        String minuteString = String.valueOf(minutes);
        String secondsString;
        if (remainder >= 10) {
            secondsString = String.valueOf(remainder);
        } else {
            secondsString = "0" + remainder;
        }
        return minuteString + ":" + secondsString;
    }

    //endregion
}
