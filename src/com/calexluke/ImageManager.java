package com.calexluke;

import javafx.scene.image.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

// class for handling Image Operations

public class ImageManager {
    private final String logoImageFilePath = "/com/calexluke/Assets/PAIN(t).png";

    public Image getLogoImage() {
        return getImageFromAssets(logoImageFilePath);
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
    public Image getImageFromFilePath(String filePath) throws FileNotFoundException {
        return new Image(new FileInputStream(filePath));
    }
}
