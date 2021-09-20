package com.calexluke;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
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

    // take snapshot of canvas/image 'stack' to save
    public WritableImage getSnapshotImageToSave(PaintFxCanvas canvas) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        //Take snapshot of the scene
        WritableImage writableImage = canvas.snapshot(params, null);
        return  writableImage;
    }

    // convert image to the proper format to save as jpg
    public BufferedImage getBufferedImageForJPG(WritableImage image) {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage convertedImage = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        convertedImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
        return convertedImage;
    }
}
