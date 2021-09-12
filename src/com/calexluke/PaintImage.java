package com.calexluke;

import javafx.scene.image.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PaintImage {

    private Image originalImage;
    private ArrayList<Image> undoArray;
    private int undoIndex;

    private final String logoImageFilePath = "/com/calexluke/Assets/PAIN(t).png";

    public PaintImage() {
        undoIndex = 0;
    }

    public void setImageWithAssetPath(String pathToAsset) {
        Image image = getImageFromAssets(pathToAsset);
        if (image != null) {
            this.originalImage = image;
            //undoArray.set(undoIndex, image);
        }
    }

    public void setImageWithFilePath(String filePath) {
        try {
            Image image = getImageFromFilePath(filePath);
            this.originalImage = image;
            //undoArray.set(undoIndex, image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Image getImage() {
        return originalImage;
    }

    public Image getLogoImage() {
        return getImageFromAssets(logoImageFilePath);
    }

    // create Image object from image file in assets folder
    private Image getImageFromAssets(String pathToAsset) {
        Image image = null;
        try {
            image = new Image(pathToAsset);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return image;
    }

    // create Image object from image file in user's local file system
    private Image getImageFromFilePath(String filePath) throws FileNotFoundException {
        return new Image(new FileInputStream(filePath));
    }
}
