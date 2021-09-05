package sample;

import javafx.scene.image.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

// class for handling Image Operations

public class ImageManager {
    private final String logoImageFilePath = "/sample/Assets/PAIN(t).png";

    public Image getLogoImage() {
        return getImageFromAssets(logoImageFilePath);
    }

    public Image getImageFromAssets(String pathToAsset) {
        Image image = null;
        try {
            image = new Image(pathToAsset);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Image getImageFromFilePath(String filePath) throws FileNotFoundException {
        return new Image(new FileInputStream(filePath));
    }
}
