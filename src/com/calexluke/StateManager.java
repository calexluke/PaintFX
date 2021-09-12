package com.calexluke;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class StateManager {

    public enum ToolType {
        MOUSE,
        PENCIL,
        LINE
    }

    public enum StrokeWidth {
        THIN,
        MEDIUM,
        WIDE
    }

    private Image mainImage;
    private ToolType selectedTool;
    private StrokeWidth selectedStrokeWidth;
    private String saveAsFilePath;

    // will be used to handle undo operations
    private Image currentImage;
    private ArrayList<Image> undoArray;
    private int undoIndex = 0;

    public StateManager() {
        ImageManager imageManager = new ImageManager();
        selectedTool = ToolType.MOUSE;
        selectedStrokeWidth = StrokeWidth.THIN;
        saveAsFilePath = null;
        mainImage = imageManager.getLogoImage();
    }

    // setters

    public void setMainImage(Image image) {
        mainImage = image;
    }

    public void setSelectedTool(ToolType tool) {
        selectedTool = tool;
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

    public ToolType getSelectedTool() {
        return selectedTool;
    }

    public StrokeWidth getSelectedStrokeWidth() {
        return selectedStrokeWidth;
    }

    public String getSaveAsFilePath() {
        return saveAsFilePath;
    }

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
