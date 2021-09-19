package com.calexluke;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Optional;


public class Main extends Application {

    private Group root;
    private BorderPane borderPane;
    private StackPane stackPane;
    private Scene scene;

    private FileManager fileManager;
    private ImageManager imageManager;
    private StateManager stateManager;

    private PaintFxCanvas mainCanvas;
    private ImageView mainImageView;
    private PaintFxScrollBar horizontalScrollBar;
    private PaintFxScrollBar verticalScrollBar;
    private ToolBar toolBar;
    private MenuBar menuBar;

    // default values, approximations of widths of border elements. These are reset dynamically after init
    private Double imageWidthOffset = 178.0;
    private Double imageHeightOffset = 84.0;

    //region Lifecycle

    @Override
    public void start(Stage primaryStage) throws Exception {

        // init and configure scene elements
        root = new Group();
        borderPane = new BorderPane();
        scene = new Scene(root, Constants.defaultSceneWidth, Constants.defaultSceneHeight);
        stackPane = new StackPane();
        root.getChildren().add(borderPane);
        borderPane.setCenter(stackPane);

        stateManager = new StateManager();
        imageManager = new ImageManager();
        fileManager = new FileManager(primaryStage, stateManager);

        configureMenuBar();
        configureToolBar();
        configureCanvas();
        configureScrollBars();

        // add listener to track changes in scene size
        ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) -> scaleBorderPaneToSceneSize();
        scene.widthProperty().addListener(sceneSizeListener);
        scene.heightProperty().addListener(sceneSizeListener);

        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);
        primaryStage.show();

        // this has to happen after the stage shows the scene, or the toolbar and menubar won't have correct height/width
        imageWidthOffset = toolBar.getWidth() + verticalScrollBar.getWidth() + (Constants.imageInsetValue);
        imageHeightOffset = menuBar.getHeight() + horizontalScrollBar.getHeight() + (Constants.imageInsetValue);

        scaleBorderPaneToSceneSize();
        displayMainImage();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void quitApplication() {
        // handle more shutdown stuff here

        if (stateManager.getHasUnsavedChanges()) {
            displaySmartSaveAlert();
        } else {
            System.exit(0);
        }
    }

    private void displaySmartSaveAlert() {
        ButtonType save = new ButtonType("Save Changes", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Exit Without Saving", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "You have unsaved changes!", save, quit);
        alert.setTitle("Unsaved changes!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == save) {
            fileManager.saveImage(imageManager.getSnapshotImageToSave(stackPane));
            System.exit(0);
        } else if (result.get() == quit) {
            System.exit(0);
        }
    }

    //endregion

    //region Configuration Methods

    private void configureMenuBar() {
        menuBar = new MenuBar();
        configurePaintMenu();
        configureImageMenu();
        configureViewMenu();
        configureHelpMenu();
        borderPane.setTop(menuBar);
    }

    private void configureToolBar() {
        toolBar = new PaintFxToolbar(stateManager);
        borderPane.setLeft(toolBar);
    }

    private void configureScrollBars() {
        horizontalScrollBar = new PaintFxScrollBar();
        verticalScrollBar = new PaintFxScrollBar();
        verticalScrollBar.setOrientation(Orientation.VERTICAL);
        borderPane.setBottom(horizontalScrollBar);
        borderPane.setRight(verticalScrollBar);

        // listeners for scrollbar value change
        // translate image in x or y direction based on scrollbar value
        ChangeListener<Number> horizontalValueChangeListener = (ov, oldVal, newVal) -> {
            stackPane.setTranslateX(-newVal.doubleValue());
        };

        ChangeListener<Number> verticalValueChangeListener = (ov, oldVal, newVal) -> {
            stackPane.setTranslateY(-newVal.doubleValue());
        };

        horizontalScrollBar.valueProperty().addListener(horizontalValueChangeListener);
        verticalScrollBar.valueProperty().addListener(verticalValueChangeListener);
    }

    void configureCanvas() {
        double width = Constants.defaultSceneWidth - imageWidthOffset;
        double height = Constants.defaultSceneHeight - imageHeightOffset;
        mainCanvas = new PaintFxCanvas(width, height, stateManager);
        stackPane.getChildren().add(mainCanvas);
        mainCanvas.toFront();
    }

    private void configurePaintMenu() {
        Menu mainMenu = new Menu("Pain(t)");
        MenuItem quit = new MenuItem("Quit Pain(t)");
        quit.setOnAction(e -> quitApplication());
        mainMenu.getItems().add(quit);
        menuBar.getMenus().add(mainMenu);
    }

    private void configureImageMenu() {
        Menu imageMenu = new Menu("File");

        MenuItem load = new MenuItem("Load New Image");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem save = new MenuItem("Save");
        MenuItem restore = new MenuItem("Restore main logo image");

        load.setOnAction(e -> selectNewImage());
        restore.setOnAction(e -> displayDefaultLogoImage());
        // take snapshot of canvas/image stack, send to fileManager to save
        saveAs.setOnAction(e -> fileManager.saveImageAs(imageManager.getSnapshotImageToSave(stackPane)));
        save.setOnAction(e -> fileManager.saveImage(imageManager.getSnapshotImageToSave(stackPane)));

        imageMenu.getItems().add(restore);
        imageMenu.getItems().add(save);
        imageMenu.getItems().add(saveAs);
        imageMenu.getItems().add(load);

        menuBar.getMenus().add(imageMenu);
    }

    private void configureViewMenu() {
        Menu viewMenu = new Menu("View");
        MenuItem zoomIn = new MenuItem("Zoom +");
        MenuItem zoomOut = new MenuItem("Zoom -");
        MenuItem autoScale = new MenuItem("Auto-Scale Image");

        autoScale.setOnAction(e -> scaleMainImageToSceneSize());
        zoomIn.setOnAction(e -> zoom(1.1));
        zoomOut.setOnAction(e -> zoom(0.9));
        viewMenu.getItems().add(zoomIn);
        viewMenu.getItems().add(zoomOut);
        viewMenu.getItems().add(autoScale);

        menuBar.getMenus().add(viewMenu);
    }

    private void configureHelpMenu() {
        Menu helpMenu = new Menu("Help");
        Alert helpMenuAlert = new Alert(Alert.AlertType.INFORMATION);
        MenuItem about = new MenuItem("About");
        MenuItem help = new MenuItem("Help");

        about.setOnAction(e -> {
            helpMenuAlert.setTitle(Constants.aboutAlertTitle);
            helpMenuAlert.setHeaderText(Constants.aboutAlertTitle);
            helpMenuAlert.setContentText(Constants.aboutAlertText);
            helpMenuAlert.show();
        });

        help.setOnAction(e -> {
            helpMenuAlert.setTitle(Constants.helpAlertTitle);
            helpMenuAlert.setHeaderText(Constants.helpAlertTitle);
            helpMenuAlert.setContentText(Constants.helpAlertText);
            helpMenuAlert.show();
        });
        helpMenu.getItems().add(about);
        helpMenu.getItems().add(help);
        menuBar.getMenus().add(helpMenu);
    }

    //endregion

    //region Image Selection and Display

    private void selectNewImage() {
        String filePath = fileManager.getImageFilePathFromUser((Stage)scene.getWindow());
        if (filePath != null) {
            try {
                Image newImage = imageManager.getImageFromFilePath(filePath);
                stateManager.setMainImage(newImage);
                displayMainImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDefaultLogoImage() {
        Image logo = imageManager.getLogoImage();
        stateManager.setMainImage(logo);
        displayMainImage();
    }

    // re-initialize mainIMageView and repopulate stackpane with imageview and canvas
    private void displayMainImage() {
        Image image = stateManager.getMainImage();

        if (image != null) {
            // new image view, clear any drawing from canvas
            mainImageView = new ImageView(image);
            mainCanvas.clearGraphicsContext();

            mainImageView.setX(0);
            mainImageView.setY(0);
            mainImageView.setPreserveRatio(true);

            // clear stackpane - otherwise the prior imageview will remain
            while (stackPane.getChildren().size() > 0) {
                stackPane.getChildren().remove(0);
            }
            stackPane.getChildren().add(mainCanvas);

            scaleMainImageToSceneSize();
            displayMainImageOnCanvas();

        } else {
            System.out.println("Unable to display main image - mainImage.image is null!");
        }
    }

    private void displayMainImageOnCanvas() {
        Image image = mainImageView.getImage();
        mainCanvas.clearGraphicsContext();
        mainCanvas.getGraphicsContext2D().drawImage(image, 0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
    }

    //endregion

    //region Image and Pane Scaling

    private void scaleMainImageToSceneSize() {
        double newWidth = scene.getWidth() - imageWidthOffset;
        double newHeight = scene.getHeight() - imageHeightOffset;
        scaleMainImage(newWidth, newHeight);
    }

    private void scaleMainImage(double newWidth, double newHeight) {
        mainImageView.setFitHeight(newHeight);
        mainImageView.setFitWidth(newWidth);
        scaleCanvasToImageSize();
        displayMainImageOnCanvas();

        stackPane.setAlignment(Pos.CENTER);
        updateScrollBars();
    }

    private void zoom(double factor) {
        double currentWidth = mainImageView.getFitWidth();
        double currentHeight = mainImageView.getFitHeight();
        scaleMainImage(currentWidth * factor, currentHeight * factor);
    }

    private void scaleCanvasToImageSize() {
        // workaround for aspect ratio issues - need to find the actual width and height of the view. One or the other
        // will be scaled to maintain aspect ratio, so you can't read it directly off of the view object.
        // from https://stackoverflow.com/questions/39408845/how-to-get-width-height-of-displayed-image-in-javafx-imageview
        mainCanvas.setWidth(getActualImageViewWidth());
        mainCanvas.setHeight(getActualImageViewHeight());
    }

    private double getActualImageViewHeight() {
        double aspectRatio = mainImageView.getImage().getWidth() / mainImageView.getImage().getHeight();
        double imageViewHeight = Math.min(mainImageView.getFitHeight(), mainImageView.getFitWidth() / aspectRatio);
        return imageViewHeight;
    }

    private double getActualImageViewWidth() {
        double aspectRatio = mainImageView.getImage().getWidth() / mainImageView.getImage().getHeight();
        double imageViewWidth = Math.min(mainImageView.getFitWidth(), mainImageView.getFitHeight() * aspectRatio);
        return imageViewWidth;
    }

    private void scaleBorderPaneToSceneSize() {
        borderPane.setMinWidth(scene.getWidth());
        borderPane.setMinHeight(scene.getHeight());
        borderPane.setMaxWidth(scene.getWidth());
        borderPane.setMaxHeight(scene.getHeight());

//        if (mainImageView != null) {
//            scaleMainImageToSceneSize();
//        }
        updateScrollBars();
    }

    //endregion

    //region Scrollbars

    /*
    The scroll bar will translate the image by the scrollbar's value.
    These methods will dynamically update the bounds of the scroll bar based on how far the image is "offscreen."
    The lower and upper bounds of the scrollbar will be the amount the image needs to be translated to be back
    "onscreen."
    */
    private void updateScrollBars() {
        Bounds imageBoundsInScene = stackPane.localToScene(stackPane.getBoundsInLocal());

        // actual image y values
        double imageTopY = imageBoundsInScene.getMinY();
        double imageBottomY = imageBoundsInScene.getMaxY();

        // actual image x values
        double imageLeftX = imageBoundsInScene.getMinX();
        double imageRightX = imageBoundsInScene.getMaxX();

        // Y values where the edges of the image should be if the image is in frame
        double verticalMin = menuBar.getHeight() + (Constants.imageInsetValue / 2);
        double verticalMax = scene.getHeight() - horizontalScrollBar.getHeight() - (Constants.imageInsetValue / 2);

        // x values where the edges of the image should be if the image is in frame
        double horizontalMin = toolBar.getWidth() + (Constants.imageInsetValue / 2);
        double horizontalMax = scene.getWidth() - verticalScrollBar.getWidth() - (Constants.imageInsetValue / 2);

        horizontalScrollBar.updateScrollBarBounds(imageLeftX, imageRightX, horizontalMin, horizontalMax);
        verticalScrollBar.updateScrollBarBounds(imageTopY, imageBottomY, verticalMin, verticalMax);
    }

    //endregion
}
