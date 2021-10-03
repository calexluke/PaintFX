package com.calexluke;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;


/*
Class to maintain the 'state' of the app.
Keep track of user settings/preferences, and serve that info to other classes which act on it.
*/

public class StateManager {

    public enum StrokeWidth {
        THIN,
        MEDIUM,
        WIDE
    }

    private ArrayList<Image> imageArray;
    // stores autoSaved images, indexed by tab
    private HashMap<Integer, Image> autoSaveImagesMap;
    private PaintFxTool selectedTool;
    private StrokeWidth selectedStrokeWidth;
    private int selectedPolygonSides;
    private Color strokeColor;
    private Color fillColor;
    private Boolean hasUnsavedChanges;
    private int selectedTabIndex;
    private HashMap<Integer, String> saveAsFilePathMap;

    public IntegerProperty autoSaveCounter;
    private int autoSaveCounterMax = 122;

    public StateManager() {
        ImageManager imageManager = new ImageManager();
        selectedTool = new MouseTool();
        selectedStrokeWidth = StrokeWidth.THIN;
        strokeColor = Color.BLACK;
        fillColor = Color.BLACK;
        hasUnsavedChanges = false;
        saveAsFilePathMap = new HashMap<>();
        autoSaveImagesMap = new HashMap<>();
        selectedTabIndex = 0;
        selectedPolygonSides = 5;
        autoSaveCounter = new SimpleIntegerProperty();
        autoSaveCounter.setValue(autoSaveCounterMax);

        configureTimer();

        imageArray = new ArrayList<>();
        imageArray.add(selectedTabIndex, imageManager.getLogoImage());
    }

    // setters

    public void setMainImageInCurrentTab(Image image) {
        imageArray.add(selectedTabIndex, image);
    }
    public void setSelectedTool(PaintFxTool tool) {
        if (selectedTool instanceof TextTool) {
            ((TextTool) selectedTool).removeTextFieldsFromPane();
        }
        selectedTool = tool;
    }
    public void setStrokeColor(Color color) {
        strokeColor = color;
    }
    public void setFillColor(Color color) {
        fillColor = color;
    }
    public void setSelectedStrokeWidth(StrokeWidth width) {
        selectedStrokeWidth = width;
    }
    public void setSelectedPolygonSides(int sides) { selectedPolygonSides = sides; }
    public void setSaveAsFilePathForCurrentTab(String filePath) {
        saveAsFilePathMap.put(selectedTabIndex, filePath);
    }
    public void setAutoSaveImageForCurrentTab(Image image) { autoSaveImagesMap.put(selectedTabIndex, image); }
    public void setHasUnsavedChanges(Boolean hasChanges) {
        hasUnsavedChanges = hasChanges;
    }
    public void setSelectedTabIndex(int index) {
        selectedTabIndex = index;
    }
    public void setAutoSaveCounterMax(int newValue) {
        autoSaveCounterMax = newValue;
        autoSaveCounter.setValue(autoSaveCounterMax);
    }
    public final void setAutoSaveCounter(int value) { autoSaveCounter.set(value); }

    // getters

    public Image getImageFromCurrentTab() {
        return imageArray.get(selectedTabIndex);
    }
    public PaintFxTool getSelectedTool() {
        return selectedTool;
    }
    public Color getStrokeColor() {
        return strokeColor;
    }
    public Color getFillColor() {
        return fillColor;
    }
    public StrokeWidth getSelectedStrokeWidth() {
        return selectedStrokeWidth;
    }
    public int getSelectedPolygonSides() { return selectedPolygonSides; }
    public String getSaveAsFilePathForCurrentTab() {
        return saveAsFilePathMap.get(selectedTabIndex);
    }
    public Image getAutoSaveImageForCurrentTab() {
        return autoSaveImagesMap.get(selectedTabIndex);
    }
    public Boolean getHasUnsavedChanges() {
        return hasUnsavedChanges;
    }
    public int getSelectedTabIndex() { return  selectedTabIndex; }
    public final int getAutoSaveCounter() { return autoSaveCounter.get(); }
    public IntegerProperty autoSaveCounterProperty() { return autoSaveCounter; }


    private void configureTimer() {
        // decrement counter every 1 second
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                event -> decrementCounter()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void decrementCounter() {
        if (autoSaveCounter.getValue() == 0) {
            setAutoSaveCounter(autoSaveCounterMax);
        } else {
            setAutoSaveCounter(autoSaveCounter.getValue() - 1);
        }
    }
}
