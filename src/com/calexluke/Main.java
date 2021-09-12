package com.calexluke;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private Group root = new Group();
    private VBox vbox = new VBox();
    private BorderPane borderPane;
    private StackPane stackPane;
    private Scene scene;
    private FileManager fileManager = new FileManager();
    private PaintImage mainImage;

    private Canvas testCanvas;

    GraphicsContext graphicsContext;


    private final int menuBarVboxIndex = 0;
    private final int mainImageVBoxIndex = 0;
    private final int defaultSceneHeight = 1000;
    private final int defaultSceneWidth = 1000;
    private final String logoImageFilePath = "/com/calexluke/Assets/PAIN(t).png";

    @Override
    public void start(Stage primaryStage) throws Exception {
        root.getChildren().add(vbox);
        scene = new Scene(root, defaultSceneWidth, defaultSceneHeight);
        mainImage = new PaintImage();

        stackPane = new StackPane();

        // add listener to track changes in scene size
//        ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) -> scaleMainImageToSceneSize();
//        scene.widthProperty().addListener(sceneSizeListener);
//        scene.heightProperty().addListener(sceneSizeListener);

        configureMenuBar();
        vbox.getChildren().add(stackPane);
        displayDefaultLogoImage();

        testCanvas = new Canvas(1000, 1000);
        graphicsContext = testCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillOval(50,50,20,20);
        stackPane.getChildren().add(1, testCanvas);
        testCanvas.toFront();

        testCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        graphicsContext.fillOval(e.getX(),e.getY(),20,20);
                    }
                });


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
        Menu viewMenu = new Menu("View");
        Menu toolsMenu = new Menu("Tools");
        Menu helpMenu = new Menu("Help");

        // paint menu items
        MenuItem quit = new MenuItem("Quit Pain(t)");
        quit.setOnAction(e -> quitApplication());
        mainMenu.getItems().add(quit);

        // image menu items
        MenuItem load = new MenuItem("Load New Image");
        MenuItem save = new MenuItem("Save Image");
        MenuItem restore = new MenuItem("Restore main logo image");
        load.setOnAction(e -> selectNewImage());
        save.setOnAction(e -> saveImage());
        restore.setOnAction(e -> displayDefaultLogoImage());
        imageMenu.getItems().add(restore);
        imageMenu.getItems().add(save);
        imageMenu.getItems().add(load);

        // View menu items
        MenuItem zoomIn = new MenuItem("Zoom +");
        MenuItem zoomOut = new MenuItem("Zoom -");
        MenuItem autoScale = new MenuItem("Auto-Scale Image");
        autoScale.setOnAction(e -> scaleMainImageToSceneSize());
        viewMenu.getItems().add(zoomIn);
        viewMenu.getItems().add(zoomOut);
        viewMenu.getItems().add(autoScale);

        // tools menu items
        MenuItem mouse = new MenuItem("Mouse");
        MenuItem line = new MenuItem("Line");
        mouse.setOnAction(e -> {
                toolsMenu.setText("Mouse");
        });
        line.setOnAction(e -> {
            toolsMenu.setText("Line");
        });
        toolsMenu.getItems().add(mouse);
        toolsMenu.getItems().add(line);

        // help menu items
        MenuItem about = new MenuItem("About");
        MenuItem help = new MenuItem("Help");
        helpMenu.getItems().add(about);
        helpMenu.getItems().add(help);

        // add menus to menu bar
        menuBar.getMenus().add(mainMenu);
        menuBar.getMenus().add(imageMenu);
        menuBar.getMenus().add(viewMenu);
        menuBar.getMenus().add(toolsMenu);
        menuBar.getMenus().add(helpMenu);

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
                mainImage.setImageWithFilePath(filePath);
                displayMainImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDefaultLogoImage() {
        mainImage.setImageWithAssetPath(logoImageFilePath);
        displayMainImage();
    }

    private void displayMainImage() {
        Image image = mainImage.getImage();

        if (image != null) {
            ObservableList<Node> stackNodes = stackPane.getChildren();
            ImageView imageView = new ImageView(image);

            imageView.setX(0);
            imageView.setY(0);
            imageView.setFitHeight(scene.getHeight() - 40);
            imageView.setFitWidth(scene.getWidth() - 40);
            imageView.setPreserveRatio(true);

            // If main imageView already exists, remove it
            if (stackNodes.size() >= 1 && stackNodes.get(mainImageVBoxIndex) != null) {
                stackNodes.remove(mainImageVBoxIndex);
            }
            stackNodes.add(mainImageVBoxIndex, imageView);
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setOpaqueInsets(new Insets(20, 20, 20, 20));
            //VBox.setMargin(stackNodes.get(mainImageVBoxIndex), new Insets(20, 20, 20, 20));
        } else {
            System.out.println("Unable to display main image - mainImage.image is null!");
        }
    }

    private void scaleMainImageToSceneSize() {
        vbox.setMinWidth(scene.getWidth());
        vbox.setMinHeight(scene.getHeight());
        vbox.setMaxWidth(scene.getWidth());
        vbox.setMaxHeight(scene.getHeight());
//        ObservableList<Node> childNodes = vbox.getChildren();
//        if (childNodes.size() > 1 && childNodes.get(mainImageVBoxIndex) != null) {
//            try {
//                ImageView mainImageView = (ImageView) childNodes.get(mainImageVBoxIndex);
//                mainImageView.setFitHeight(scene.getHeight() - 40);
//                mainImageView.setFitWidth(scene.getWidth() - 40);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    void saveImage() {

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        //Take snapshot of the scene
        WritableImage writableImage = stackPane.snapshot(params, null);

        // Write snapshot to file system as a .png image
        File outFile = new File("imageops-snapshot.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null),
                    "png", outFile);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
