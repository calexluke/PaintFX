package com.calexluke;

import javafx.application.Application;
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

    private Canvas mainCanvas;
    private ImageView mainImageView;

    GraphicsContext graphicsContext;

    private final int mainImageViewStackPaneIndex = 0;
    private final int mainCanvasStackPaneIndex = 1;
    private final int defaultSceneHeight = 1000;
    private final int defaultSceneWidth = 1000;
    private final int imageOffset = 50;
    private final String logoImageFilePath = "/com/calexluke/Assets/PAIN(t).png";

    @Override
    public void start(Stage primaryStage) throws Exception {
        borderPane = new BorderPane();
        root.getChildren().add(borderPane);
        //root.getChildren().add(vbox);
        scene = new Scene(root, defaultSceneWidth, defaultSceneHeight);
        mainImage = new PaintImage();

        stackPane = new StackPane();

        // add listener to track changes in scene size
//        ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) -> scaleMainImageToSceneSize();
//        scene.widthProperty().addListener(sceneSizeListener);
//        scene.heightProperty().addListener(sceneSizeListener);

        configureMenuBar();
        borderPane.setCenter(stackPane);
        configureCanvas();

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
        //vbox.getChildren().add(menuBarVboxIndex, menuBar);
        borderPane.setTop(menuBar);
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

    // re initialize mainIMageView and repopulate stackpane with imageview and canvas
    private void displayMainImage() {
        Image image = mainImage.getImage();

        if (image != null) {
            // new image view, clear any drawing from canvas
            mainImageView = new ImageView(image);
            graphicsContext.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

            mainImageView.setX(0);
            mainImageView.setY(0);
            mainImageView.setPreserveRatio(true);

            //stackPane.getChildren().removeAll();
            while (stackPane.getChildren().size() > 0) {
                stackPane.getChildren().remove(0);
            }

            stackPane.getChildren().add(mainImageView);
            stackPane.getChildren().add(mainCanvas);

            scaleMainImageToSceneSize();
        } else {
            System.out.println("Unable to display main image - mainImage.image is null!");
        }
    }

    private void scaleMainImageToSceneSize() {

        double newWidth = scene.getWidth() - imageOffset;
        double newHeight = scene.getHeight() - imageOffset;
        mainImageView.setFitHeight(newHeight);
        mainImageView.setFitWidth(newWidth);

        // workaround for aspect ratio issues - need to find the actual width and height of the view. One or the other
        // will be scaled to maintain aspect ratio.
        // from https://stackoverflow.com/questions/39408845/how-to-get-width-height-of-displayed-image-in-javafx-imageview
        double aspectRatio = mainImageView.getImage().getWidth() / mainImageView.getImage().getHeight();
        double imageViewWidth = Math.min(mainImageView.getFitWidth(), mainImageView.getFitHeight() * aspectRatio);
        double imageViewHeight = Math.min(mainImageView.getFitHeight(), mainImageView.getFitWidth() / aspectRatio);

        mainCanvas.setWidth(imageViewWidth);
        mainCanvas.setHeight(imageViewHeight);

        borderPane.setMinWidth(scene.getWidth());
        borderPane.setMinHeight(scene.getHeight());
        borderPane.setMaxWidth(scene.getWidth());
        borderPane.setMaxHeight(scene.getHeight());

        stackPane.setAlignment(Pos.CENTER);
        stackPane.setOpaqueInsets(new Insets(20, 20, 20, 20));
    }

    void configureCanvas() {
        mainCanvas = new Canvas(defaultSceneWidth - imageOffset, defaultSceneHeight - imageOffset);
        graphicsContext = mainCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillOval(50,50,20,20);
        stackPane.getChildren().add(mainCanvas);
        mainCanvas.toFront();
        mainCanvas.setOnMouseClicked(mouseEvent -> onClick(mouseEvent));

//        mainCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
//                new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent e) {
//                        graphicsContext.fillOval(e.getX(),e.getY(),20,20);
//                    }
//                });
    }

    void onClick(MouseEvent e) {
        graphicsContext.fillOval(e.getX(),e.getY(),20,20);
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
