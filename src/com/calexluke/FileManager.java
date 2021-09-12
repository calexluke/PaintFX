package com.calexluke;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

// Class for handling file system operations

public class FileManager {

    private final FileChooser.ExtensionFilter imageExtensionFilter = new FileChooser.ExtensionFilter("Image files",
            "*.jpg", "*.jpeg", "*.png", "*.PNG");

    // Returns null if user exits file chooser without making a selection
    public String getImageFilePathFromUser(Stage stage) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(imageExtensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath();
        }
        return path;
    }

    public String getSaveAsFilePathFromUser(Stage stage) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            path = file.getAbsolutePath();
        }
        return path;
    }

    public void saveImageToFilePath(WritableImage image, String filePath) {
        File outFile = new File(filePath);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null),
                    "png", outFile);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
