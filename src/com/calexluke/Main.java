package com.calexluke;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


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
    private ScrollBar horizontalScrollBar;
    private ScrollBar verticalScrollBar;
    private ToolBar toolBar;
    private MenuBar menuBar;

    // default values, approximations of widths of border elements. Will be reset dynamically after init
    private Double imageWidthOffset = 178.0;
    private Double imageHeightOffset = 84.0;

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



        // this has to happen after the stage shows the scene, otherwise the toolbar and menubar won't have heights
        imageWidthOffset = toolBar.getWidth() + verticalScrollBar.getWidth() + (Constants.imageInsetValue);
        imageHeightOffset = menuBar.getHeight() + horizontalScrollBar.getHeight() + (Constants.imageInsetValue);

        displayMainImage();
        scaleBorderPaneToSceneSize();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void quitApplication() {
        // handle more shutdown stuff here
        System.exit(0);
    }

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
        horizontalScrollBar = new ScrollBar();
        verticalScrollBar = new ScrollBar();
        verticalScrollBar.setOrientation(Orientation.VERTICAL);

        BorderPane.setAlignment(horizontalScrollBar, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(verticalScrollBar, Pos.TOP_RIGHT);
        borderPane.setBottom(horizontalScrollBar);
        borderPane.setRight(verticalScrollBar);

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

            stackPane.getChildren().add(mainImageView);
            stackPane.getChildren().add(mainCanvas);

            scaleMainImageToSceneSize();
        } else {
            System.out.println("Unable to display main image - mainImage.image is null!");
        }
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

        stackPane.setAlignment(Pos.CENTER);
        stackPane.setTranslateX(0);
        stackPane.setTranslateY(0);
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

    void scaleBorderPaneToSceneSize() {
        borderPane.setMinWidth(scene.getWidth());
        borderPane.setMinHeight(scene.getHeight());
        borderPane.setMaxWidth(scene.getWidth());
        borderPane.setMaxHeight(scene.getHeight());

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
        updateHorizontalScrollBar(imageBoundsInScene);
        updateVerticalScrollBar(imageBoundsInScene);
    }

    private void setScrollBarBounds(ScrollBar scrollBar, double min, double max) {
        scrollBar.setMin(min);
        scrollBar.setMax(max);
        scrollBar.setValue(0);

        // scrollbar thumb takes up half of the current range
        double range = max - min;
        scrollBar.setVisibleAmount(range / 2);
    }

    private void updateVerticalScrollBar(Bounds boundsOfImage) {
        // actual image y values
        double imageTopY = boundsOfImage.getMinY();
        double imageBottomY = boundsOfImage.getMaxY();

        // x values where the edges of the image should be if the image is in frame
        double verticalMin = menuBar.getHeight() + (Constants.imageInsetValue / 2);
        double verticalMax = scene.getHeight() - horizontalScrollBar.getHeight() - (Constants.imageInsetValue / 2);

        // Amount the image needs to be translated to be back in frame
        double newScrollBarMin = -(verticalMin - imageTopY);
        double newScrollBarMax = imageBottomY - verticalMax;

        setScrollBarBounds(verticalScrollBar, newScrollBarMin, newScrollBarMax);
    }

    private void updateHorizontalScrollBar(Bounds boundsOfImage) {
        // actual image x values
        double imageLeftX = boundsOfImage.getMinX();
        double imageRightX = boundsOfImage.getMaxX();

        // x values where the edges of the image should be if the image is in frame
        double horizontalMin = toolBar.getWidth() + (Constants.imageInsetValue / 2);
        double horizontalMax = scene.getWidth() - verticalScrollBar.getWidth() - (Constants.imageInsetValue / 2);

        // Amount the image needs to be translated to be back in frame
        double newScrollBarMin = -(horizontalMin - imageLeftX);
        double newScrollBarMax = imageRightX - horizontalMax;

        setScrollBarBounds(horizontalScrollBar, newScrollBarMin, newScrollBarMax);
    }

    //endregion
}
