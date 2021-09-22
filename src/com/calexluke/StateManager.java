package com.calexluke;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
    private ArrayList<PaintFxCanvas> canvasArray;
    private PaintFxTool selectedTool;
    private StrokeWidth selectedStrokeWidth;
    private Color strokeColor;
    private Color fillColor;
    private Boolean hasUnsavedChanges;
    private int selectedTabIndex;
    private HashMap<Integer, String> saveAsFilePathMap;

    // will be used to handle undo operations
    private Image currentImage;
    private ArrayList<Image> undoArray;
    private int undoIndex = 0;

    public StateManager() {
        ImageManager imageManager = new ImageManager();
        selectedTool = new MouseTool();
        selectedStrokeWidth = StrokeWidth.THIN;
        strokeColor = Color.BLACK;
        fillColor = Color.BLACK;
        hasUnsavedChanges = false;
        saveAsFilePathMap = new HashMap<>();
        selectedTabIndex = 0;

        imageArray = new ArrayList<>();
        canvasArray = new ArrayList<>();

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
    public void setSaveAsFilePathForCurrentTab(String filePath) {
        saveAsFilePathMap.put(selectedTabIndex, filePath);
    }
    public void setHasUnsavedChanges(Boolean hasChanges) {
        hasUnsavedChanges = hasChanges;
    }
    public void setSelectedTabIndex(int index) {
        selectedTabIndex = index;
    }

    // getters

    public Image getImageFromCurrentTab() {
        return imageArray.get(selectedTabIndex);
    }
    public PaintFxCanvas getCanvasFromCurrentTab() {
        return canvasArray.get(selectedTabIndex);
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
    public String getSaveAsFilePathForCurrentTab() {
        return saveAsFilePathMap.get(selectedTabIndex);
    }
    public Boolean getHasUnsavedChanges() {
        return hasUnsavedChanges;
    }
    public int getSelectedTabIndex() { return  selectedTabIndex; }


    // the following is not currently used! For use in future undo feature
    public Image getCurrentImage(Image image) {
        return currentImage;
    }
    void resetUndoArray() {
        undoIndex = 0;
        undoArray.clear();
        undoArray.add(imageArray.get(selectedTabIndex));
        currentImage = undoArray.get(undoIndex);
    }
}
