package com.calexluke;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageManagerTest {

    ImageManager imageManager;

    @BeforeEach // setup()
    public void before() throws Exception {
        // hacky work-around from https://www.youtube.com/watch?v=vr3D3cUGeis
        // force JavaFX to initialize so the test can build
        JFXPanel panel = new JFXPanel();
        imageManager = new ImageManager();
    }

    @Test
    void getLogoImage() {
        //ImageManager imageManager = new ImageManager();
        Image logo = imageManager.getLogoImage();
        assertNotNull(logo);
    }

    @Test
    void getImageFromAssets() {
        //ImageManager imageManager = new ImageManager();
        Image image = imageManager.getImageFromAssets(Constants.LOGO_IMAGE_PATH);
        assertNotNull(image);
    }
}