package com.calexluke;

import javafx.scene.image.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

// class for handling Image Operations

public class ImageManager {

    public Image getLogoImage() {
        return getImageFromAssets(Constants.logoImageFilePath);
    }

    // create Image object from image file in assets folder
    public Image getImageFromAssets(String pathToAsset) {
        Image image = null;
        try {
            image = new Image(pathToAsset);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return image;
    }

    // create Image object from image file in user's local file system
    public Image getImageFromFilePath(String filePath) {
        Image image = null;
        try {
            image = new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }
}
