package com.calexluke;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;


public class Main extends Application {

    private Group root;
    private BorderPane borderPane;
    private TabPane tabPane;
    private Scene scene;

    public FileManager fileManager;
    public ImageManager imageManager;
    public StateManager stateManager;

    private PaintFxScrollBar horizontalScrollBar;
    private PaintFxScrollBar verticalScrollBar;
    private ToolBar toolBar;
    private MenuBar menuBar;

    private Boolean commandIsDown = false;
    private Boolean shiftIsDown = false;

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
        root.getChildren().add(borderPane);


        stateManager = new StateManager();
        imageManager = new ImageManager();
        fileManager = new FileManager(primaryStage, stateManager);

        configureTabPane();
        configureNewTabWithCanvas();
        configureMenuBar();
        configureToolBar();
        configureScrollBars();
        configureKeyboardListeners();
        configureTimerListener();

        // add listener to track changes in scene size
        ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) -> scaleBorderPaneToSceneSize();
        scene.widthProperty().addListener(sceneSizeListener);
        scene.heightProperty().addListener(sceneSizeListener);

        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);
        primaryStage.show();

        // this has to happen after the stage shows the scene, or the toolbar and menubar won't have correct height/width
        imageWidthOffset = toolBar.getWidth() + verticalScrollBar.getWidth() + (Constants.imageInsetValue);
        imageHeightOffset = menuBar.getHeight() + horizontalScrollBar.getHeight() + tabPane.getTabMaxHeight() + (Constants.imageInsetValue) ;

        displayMainImageInCurrentTab();
        scaleBorderPaneToSceneSize();

        PaintFxLogger logger = new PaintFxLogger();
        logger.clearLogFile();
        logger.writeToLog("Test Log");
        logger.writeToLog("A Second Test Log");

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
            fileManager.saveImage(imageManager.getSnapshotImageToSave(getCurrentCanvas()));
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
            int tabIndex = stateManager.getSelectedTabIndex();
            Node stackPane = tabPane.getTabs().get(tabIndex).getContent();
            stackPane.setTranslateX(-newVal.doubleValue());
        };
        ChangeListener<Number> verticalValueChangeListener = (ov, oldVal, newVal) -> {
            int tabIndex = stateManager.getSelectedTabIndex();
            Node stackPane = tabPane.getTabs().get(tabIndex).getContent();
            stackPane.setTranslateY(-newVal.doubleValue());
        };

        horizontalScrollBar.valueProperty().addListener(horizontalValueChangeListener);
        verticalScrollBar.valueProperty().addListener(verticalValueChangeListener);
    }

    private void configureTabPane() {
        tabPane = new TabPane();
        borderPane.setCenter(tabPane);

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        int index = tabPane.getSelectionModel().getSelectedIndex();
                        stateManager.setSelectedTabIndex(index);
                    }
                }
        );
    }

    void configureNewTabWithCanvas() {
        int newTabIndex = tabPane.getTabs().size();
        Tab tab = new Tab();
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        tab.setText("Tab " + (newTabIndex + 1));
        tab.setContent(stackPane);
        tabPane.getTabs().add(tab);
        configureCanvasInTab(newTabIndex);
    }


    void configureCanvasInTab(int tabIndex) {
        Tab tab = tabPane.getTabs().get(tabIndex);
        StackPane stackPane = (StackPane) tab.getContent();

        double width = Constants.defaultSceneWidth - imageWidthOffset;
        double height = Constants.defaultSceneHeight - imageHeightOffset;
        stackPane.getChildren().add(new PaintFxCanvas(width, height, stateManager));
    }

    private void configurePaintMenu() {
        Menu mainMenu = new Menu("Pain(t)");
        MenuItem quit = new MenuItem("Quit Pain(t)");
        quit.setOnAction(e -> quitApplication());
        mainMenu.getItems().add(quit);
        menuBar.getMenus().add(mainMenu);
    }

    private void configureImageMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem load = new MenuItem("Load New Image");
        MenuItem loadAutoSave = new MenuItem("Load Auto-Saved Image");
        MenuItem loadInNewTab = new MenuItem("Load Image In New Tab");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem save = new MenuItem("Save");
        MenuItem restore = new MenuItem("Restore main logo image");
        MenuItem undo = new MenuItem("Undo");
        MenuItem redo = new MenuItem("Redo");



        load.setOnAction(e -> selectNewImageForCurrentTab());
        loadAutoSave.setOnAction(e -> loadAutoSavedImageInCurrentTab());
        restore.setOnAction(e -> displayDefaultLogoImageInCurrentTab());
        // take snapshot of canvas, send to fileManager to save
        saveAs.setOnAction(e -> fileManager.saveImageAs(imageManager.getSnapshotImageToSave(getCurrentCanvas())));
        save.setOnAction(e -> fileManager.saveImage(imageManager.getSnapshotImageToSave(getCurrentCanvas())));
        loadInNewTab.setOnAction(e -> {
            configureNewTabWithCanvas();
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(tabPane.getTabs().size() - 1);
            selectNewImageForCurrentTab();
        });
        undo.setOnAction(e -> getCurrentCanvas().undoDrawOperation());
        redo.setOnAction(e -> getCurrentCanvas().redoDrawOperation());

        fileMenu.getItems().add(restore);
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(saveAs);
        fileMenu.getItems().add(load);
        fileMenu.getItems().add(loadAutoSave);
        fileMenu.getItems().add(loadInNewTab);
        fileMenu.getItems().add(undo);
        fileMenu.getItems().add(redo);

        menuBar.getMenus().add(fileMenu);
    }

    private void configureViewMenu() {
        Menu viewMenu = new Menu("View");
        MenuItem zoomIn = new MenuItem("Zoom +");
        MenuItem zoomOut = new MenuItem("Zoom -");
        MenuItem autoScale = new MenuItem("Auto-Scale Image");

        autoScale.setOnAction(e -> scaleCurrentImageToSceneSize());
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

    private void configureKeyboardListeners() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.COMMAND) {
                commandIsDown = true;
                System.out.println("Command is down");
            }
            if (e.getCode() == KeyCode.SHIFT) {
                shiftIsDown = true;
                System.out.println("Shift is down");
            }
            if (e.getCode() == KeyCode.S && commandIsDown) { fileManager.saveImage(imageManager.getSnapshotImageToSave(getCurrentCanvas())); }
            if (e.getCode() == KeyCode.EQUALS && commandIsDown) { zoom(1.1);}
            if (e.getCode() == KeyCode.MINUS && commandIsDown) { zoom(0.9);}
            if (e.getCode() == KeyCode.Q && commandIsDown) { quitApplication(); }
            if (e.getCode() == KeyCode.Z && commandIsDown && !shiftIsDown) { getCurrentCanvas().undoDrawOperation(); }
            if (e.getCode() == KeyCode.Z && commandIsDown && shiftIsDown) { getCurrentCanvas().redoDrawOperation(); }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.COMMAND) {
                commandIsDown = false;
            }
            if (e.getCode() == KeyCode.SHIFT) {
                shiftIsDown = false;
            }
        });
    }

    private void configureTimerListener() {
        stateManager.autoSaveCounterProperty().addListener((o, oldValue, newValue) -> {
            if (newValue.equals(0)) {
                System.out.println("Auto-saving image");
                stateManager.setAutoSaveImageForCurrentTab(imageManager.getSnapshotImageToSave(getCurrentCanvas()));
            }
        });
    }

    //endregion

    //region Image Selection and Display

    private void selectNewImageForCurrentTab() {
        String filePath = fileManager.getImageFilePathFromUser((Stage)scene.getWindow());
        if (filePath != null) {
            try {
                Image newImage = imageManager.getImageFromFilePath(filePath);
                stateManager.setMainImageInCurrentTab(newImage);
                displayMainImageInCurrentTab();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAutoSavedImageInCurrentTab() {
        Image autoSavedImage = stateManager.getAutoSaveImageForCurrentTab();
        if (autoSavedImage != null) {
            stateManager.setMainImageInCurrentTab(autoSavedImage);
            displayMainImageInCurrentTab();
        }
    }

    private void displayDefaultLogoImageInCurrentTab() {
        Image logo = imageManager.getLogoImage();
        stateManager.setMainImageInCurrentTab(logo);
        displayMainImageInCurrentTab();
    }

    // re-initialize mainImageView and repopulate stackpane with imageview and canvas
    // using imageView to handle aspect ratio stuff
    private void displayMainImageInCurrentTab() {
        Image image = stateManager.getImageFromCurrentTab();

        if (image != null) {
            // new image view, clear any drawing from canvas
            PaintFxCanvas canvas = getCurrentCanvas();
            canvas.setImageView(new ImageView(image));
            canvas.clearGraphicsContext();
            canvas.clearOperationsList();

            canvas.getImageView().setX(0);
            canvas.getImageView().setY(0);
            canvas.getImageView().setPreserveRatio(true);

            scaleCurrentImageToSceneSize();
            canvas.drawImageOnCanvas();
        } else {
            System.out.println("Unable to display main image - mainImage.image is null!");
        }
    }

    //endregion

    //region Image and Pane Scaling

    private void scaleCurrentImageToSceneSize() {
        double newWidth = scene.getWidth() - imageWidthOffset;
        double newHeight = scene.getHeight() - imageHeightOffset;
        scaleCurrentImage(newWidth, newHeight);
    }

    private void scaleCurrentImage(double newWidth, double newHeight) {
        getCurrentCanvas().scaleImage(newWidth, newHeight);
        updateScrollBars();
    }

    private void scaleAllImagesToSceneSize() {
        double newWidth = scene.getWidth() - imageWidthOffset;
        double newHeight = scene.getHeight() - imageHeightOffset;
        scaleAllImages(newWidth, newHeight);
    }

    private void scaleAllImages(double newWidth, double newHeight) {
        for (Tab tab : tabPane.getTabs()) {
            StackPane stackPane = (StackPane) tab.getContent();
            for (Node child : stackPane.getChildren()) {
                if (child instanceof PaintFxCanvas) {
                    PaintFxCanvas canvas = (PaintFxCanvas) child;
                    canvas.scaleImage(newWidth, newHeight);
                }
            }
        }
        updateScrollBars();
    }

    private void zoom(double factor) {
        ImageView imageView = getCurrentCanvas().getImageView();
        double currentWidth = imageView.getFitWidth();
        double currentHeight = imageView.getFitHeight();

        // need a boundary, otherwise the app can crash if zoomed in too far. The 3 is just a ballpark number that seems to work.
        boolean zoomedInTooFar = (currentWidth >= 3 * scene.getWidth()) || (currentHeight >= 3 * scene.getHeight());

        if (!zoomedInTooFar || factor < 1) {
            // if too far zoomed in, can still zoom back out
            scaleCurrentImage(currentWidth * factor, currentHeight * factor);
            System.out.println("Horizontal scrollbar: " + horizontalScrollBar.getValue());
            System.out.println("Vertical scrollbar: " + verticalScrollBar.getValue());
        }
    }

    private void scaleBorderPaneToSceneSize() {
        borderPane.setMinWidth(scene.getWidth());
        borderPane.setMinHeight(scene.getHeight());
        borderPane.setMaxWidth(scene.getWidth());
        borderPane.setMaxHeight(scene.getHeight());

        scaleAllImagesToSceneSize();
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
        Node stackPane = tabPane.getTabs().get(stateManager.getSelectedTabIndex()).getContent();
        Bounds imageBoundsInScene = stackPane.localToScene(stackPane.getBoundsInLocal());

        // actual image y values
        double imageTopY = imageBoundsInScene.getMinY();
        double imageBottomY = imageBoundsInScene.getMaxY();

        // actual image x values
        double imageLeftX = imageBoundsInScene.getMinX();
        double imageRightX = imageBoundsInScene.getMaxX();

        // Y values where the edges of the image should be if the image is in frame
        double verticalMin = menuBar.getHeight() + tabPane.getTabMaxHeight() + (Constants.imageInsetValue / 2.0);
        double verticalMax = scene.getHeight() - horizontalScrollBar.getHeight() - (Constants.imageInsetValue / 2.0);

        // x values where the edges of the image should be if the image is in frame
        double horizontalMin = toolBar.getWidth() + (Constants.imageInsetValue / 2.0);
        double horizontalMax = scene.getWidth() - verticalScrollBar.getWidth() - (Constants.imageInsetValue / 2.0);

        horizontalScrollBar.updateScrollBarBounds(imageLeftX, imageRightX, horizontalMin, horizontalMax);
        verticalScrollBar.updateScrollBarBounds(imageTopY, imageBottomY, verticalMin, verticalMax);
    }

    //endregion

    //region Tab-related

    private PaintFxCanvas getCurrentCanvas() {
        PaintFxCanvas canvas = null;
        Tab currentTab = tabPane.getTabs().get(getSelectedTabIndex());
        StackPane stackPane = (StackPane) currentTab.getContent();
        for (Node child : stackPane.getChildren()) {
            if (child instanceof PaintFxCanvas) {
                canvas = (PaintFxCanvas) child;
            }
        }
        return canvas;
    }

    private int getSelectedTabIndex() {
        return tabPane.getSelectionModel().getSelectedIndex();
    }

    //endregion
}
