package com.calexluke;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageManagerTest {

    // hacky work-around from https://www.youtube.com/watch?v=vr3D3cUGeis
    // force JavaFX to initialize so the test can build
    private JFXPanel panel = new JFXPanel();

    @Test
    void getLogoImage() {
        ImageManager imageManager = new ImageManager();
        Image logo = imageManager.getLogoImage();
        assertNotNull(logo);
    }

    @Test
    void getImageFromAssets() {
        ImageManager imageManager = new ImageManager();
        Image image = imageManager.getImageFromAssets(Constants.LOGO_IMAGE_PATH);
        assertNotNull(image);
    }
}