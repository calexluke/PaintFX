package com.calexluke;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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

    public void saveImageToFile(Image image) {
        // to be added later
    }
}
