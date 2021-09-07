package com.calexluke;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private Group root = new Group();
    private VBox vbox = new VBox();
    private Scene scene;
    private FileManager fileManager = new FileManager();
    private ImageManager imageManager = new ImageManager();

    private final int menuBarVboxIndex = 0;
    private final int mainImageVBoxIndex = 1;
    private final int defaultSceneHeight = 1000;
    private final int defaultSceneWidth = 1000;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root.getChildren().add(vbox);
        scene = new Scene(root, defaultSceneWidth, defaultSceneHeight);

        // add listener to track changes in scene size
        ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) -> scaleMainImageToSceneSize();
        scene.widthProperty().addListener(sceneSizeListener);
        scene.heightProperty().addListener(sceneSizeListener);

        configureMenuBar();
        displayDefaultLogoImage();

        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void configureMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu mainMenu = new Menu("Pain(t)");
        Menu imageMenu = new Menu("Image");

        // create menu items
        MenuItem quit = new MenuItem("Quit Pain(t)");
        MenuItem load = new MenuItem("Load New Image");
        MenuItem restore = new MenuItem("Restore main logo image");
        quit.setOnAction(e -> quitApplication());
        load.setOnAction(e -> selectNewImage());
        restore.setOnAction(e -> displayDefaultLogoImage());

        // add items to menus
        mainMenu.getItems().add(quit);
        imageMenu.getItems().add(restore);
        imageMenu.getItems().add(load);

        // add menus to menu bar
        menuBar.getMenus().add(mainMenu);
        menuBar.getMenus().add(imageMenu);

        // add menu bar to vbox
        vbox.getChildren().add(menuBarVboxIndex, menuBar);
    }

    private void quitApplication() {
        // handle more shutdown stuff here
        System.exit(0);
    }

    private void selectNewImage() {
        String filePath = fileManager.getImageFilePathFromUser((Stage)scene.getWindow());
        if (filePath != null) {
            try {
                displayMainImage(imageManager.getImageFromFilePath(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDefaultLogoImage() {
        Image defaultImage = imageManager.getLogoImage();
        if (defaultImage != null) {
            displayMainImage(defaultImage);
        }
    }

    private void displayMainImage(Image image) {
        ObservableList<Node> childNodes = vbox.getChildren();
        ImageView imageView = new ImageView(image);

        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitHeight(scene.getHeight() - 40);
        imageView.setFitWidth(scene.getWidth() - 40);
        imageView.setPreserveRatio(true);

        // If main image already exists, remove it
        if (childNodes.size() > 1 && childNodes.get(mainImageVBoxIndex) != null) {
            childNodes.remove(mainImageVBoxIndex);
        }
        childNodes.add(mainImageVBoxIndex, imageView);
        VBox.setMargin(childNodes.get(mainImageVBoxIndex), new Insets(20, 20, 20, 20));
    }

    private void scaleMainImageToSceneSize() {
        ObservableList<Node> childNodes = vbox.getChildren();
        double height = scene.getHeight();
        double width = scene.getWidth();

        if (childNodes.size() > 1 && childNodes.get(mainImageVBoxIndex) != null) {
            try {
                ImageView mainImageView = (ImageView) childNodes.get(mainImageVBoxIndex);
                mainImageView.setFitHeight(height - 40);
                mainImageView.setFitWidth(width - 40);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
