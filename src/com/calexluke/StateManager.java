package com.calexluke;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;


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

    private Image mainImage;
    private PaintFxTool selectedTool;
    private StrokeWidth selectedStrokeWidth;
    private Color strokeColor;
    private Color fillColor;
    private String saveAsFilePath;

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
        saveAsFilePath = null;
        mainImage = imageManager.getLogoImage();
    }

    // setters

    public void setMainImage(Image image) {
        mainImage = image;
    }
    public void setSelectedTool(PaintFxTool tool) {
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
    public void setSaveAsFilePath(String filePath) {
        saveAsFilePath = filePath;
    }

    // getters

    public Image getMainImage() {
        return mainImage;
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
    public String getSaveAsFilePath() {
        return saveAsFilePath;
    }

    // the following is not currently used! For use in future undo feature
    public Image getCurrentImage(Image image) {
        return currentImage;
    }
     void resetUndoArray() {
        undoIndex = 0;
        undoArray.clear();
        undoArray.add(mainImage);
        currentImage = undoArray.get(undoIndex);
    }
}
