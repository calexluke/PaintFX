package com.calexluke;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
    private Group root = new Group();
    private BorderPane borderPane;
    private StackPane stackPane;
    private Scene scene;
    private FileManager fileManager = new FileManager();
    private ImageManager imageManager = new ImageManager();
    private StateManager stateManager;

    private Canvas mainCanvas;
    private ImageView mainImageView;
    private GraphicsContext graphicsContext;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stateManager = new StateManager();
        borderPane = new BorderPane();
        root.getChildren().add(borderPane);
        scene = new Scene(root, Constants.defaultSceneWidth, Constants.defaultSceneHeight);
        stackPane = new StackPane();

        // add listener to track changes in scene size
//        ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) -> scaleMainImageToSceneSize();
//        scene.widthProperty().addListener(sceneSizeListener);
//        scene.heightProperty().addListener(sceneSizeListener);

        configureMenuBar();
        configureToolBar();
        borderPane.setCenter(stackPane);
        configureCanvas();
        displayMainImage();

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
        Menu imageMenu = new Menu("File");
        Menu viewMenu = new Menu("View");
        Menu helpMenu = new Menu("Help");

        // paint menu items
        MenuItem quit = new MenuItem("Quit Pain(t)");
        quit.setOnAction(e -> quitApplication());
        mainMenu.getItems().add(quit);

        // image menu items
        MenuItem load = new MenuItem("Load New Image");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem save = new MenuItem("Save");
        MenuItem restore = new MenuItem("Restore main logo image");
        load.setOnAction(e -> selectNewImage());
        saveAs.setOnAction(e -> saveImageAs());
        save.setOnAction(e -> saveImage());
        restore.setOnAction(e -> displayDefaultLogoImage());
        imageMenu.getItems().add(restore);
        imageMenu.getItems().add(save);
        imageMenu.getItems().add(saveAs);
        imageMenu.getItems().add(load);

        // View menu items
        MenuItem zoomIn = new MenuItem("Zoom +");
        MenuItem zoomOut = new MenuItem("Zoom -");
        MenuItem autoScale = new MenuItem("Auto-Scale Image");
        autoScale.setOnAction(e -> scaleMainImageToSceneSize());
        viewMenu.getItems().add(zoomIn);
        viewMenu.getItems().add(zoomOut);
        viewMenu.getItems().add(autoScale);

        // help menu items
        MenuItem about = new MenuItem("About");
        MenuItem help = new MenuItem("Help");
        helpMenu.getItems().add(about);
        helpMenu.getItems().add(help);

        // add menus to menu bar
        menuBar.getMenus().add(mainMenu);
        menuBar.getMenus().add(imageMenu);
        menuBar.getMenus().add(viewMenu);
        menuBar.getMenus().add(helpMenu);

        // add menu bar to vbox
        //vbox.getChildren().add(menuBarVboxIndex, menuBar);
        borderPane.setTop(menuBar);
    }

    private void configureToolBar() {
        ToolBar toolBar = new ToolBar();

        // configure tool buttons
        // toggle group allows selection of only one button at a time
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton mouseButton = new ToggleButton("Mouse");
        ToggleButton pencilButton = new ToggleButton("Pencil");
        ToggleButton lineButton = new ToggleButton("Line");
        mouseButton.setOnAction(e -> {
            stateManager.setSelectedTool(StateManager.ToolType.MOUSE);
        });
        pencilButton.setOnAction(e -> {
            stateManager.setSelectedTool(StateManager.ToolType.PENCIL);
        });
        lineButton.setOnAction(e -> {
            stateManager.setSelectedTool(StateManager.ToolType.LINE);
        });

        toggleGroup.getToggles().add(mouseButton);
        toggleGroup.getToggles().add(pencilButton);
        toggleGroup.getToggles().add(lineButton);
        toolBar.getItems().add(mouseButton);
        toolBar.getItems().add(pencilButton);
        toolBar.getItems().add(lineButton);


        // configure stroke width combo box
        ComboBox strokeWidthComboBox = new ComboBox();
        strokeWidthComboBox.getItems().addAll (
                StateManager.StrokeWidth.THIN,
                StateManager.StrokeWidth.MEDIUM,
                StateManager.StrokeWidth.WIDE);
        strokeWidthComboBox.setValue(StateManager.StrokeWidth.THIN);
        strokeWidthComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            stateManager.setSelectedStrokeWidth((StateManager.StrokeWidth) newValue);
            updateStrokeWidth();
        });
        toolBar.getItems().add(strokeWidthComboBox);

        // configure color picker
        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.getStyleClass().add("button");
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(colorEvent -> {
            Color chosenColor = colorPicker.getValue();
            graphicsContext.setStroke(chosenColor);
        });
        toolBar.getItems().add(colorPicker);

        toolBar.setOrientation(Orientation.VERTICAL);
        borderPane.setLeft(toolBar);
    }

    private void quitApplication() {
        // handle more shutdown stuff here
        System.exit(0);
    }

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

    // re initialize mainIMageView and repopulate stackpane with imageview and canvas
    private void displayMainImage() {
        Image image = stateManager.getMainImage();

        if (image != null) {
            // new image view, clear any drawing from canvas
            mainImageView = new ImageView(image);
            graphicsContext.clearRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());

            mainImageView.setX(0);
            mainImageView.setY(0);
            mainImageView.setPreserveRatio(true);

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

        double newWidth = scene.getWidth() - Constants.imageOffset;
        double newHeight = scene.getHeight() - Constants.imageOffset;
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
        mainCanvas = new Canvas(Constants.defaultSceneWidth - Constants.imageOffset, Constants.defaultSceneHeight - Constants.imageOffset);
        graphicsContext = mainCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillOval(50,50,20,20);
        stackPane.getChildren().add(mainCanvas);
        mainCanvas.toFront();
        //mainCanvas.setOnMouseClicked(mouseEvent -> onClick(mouseEvent));
        mainCanvas.setOnMouseDragged(mouseEvent -> onDrag(mouseEvent));
        mainCanvas.setOnMousePressed(e -> onMousePressed(e));
        mainCanvas.setOnMouseReleased(e -> onMouseReleased(e));
    }


    void onMouseReleased(MouseEvent e) {
        StateManager.ToolType tool = stateManager.getSelectedTool();

        graphicsContext.closePath();
        if (tool == StateManager.ToolType.LINE) {
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.closePath();
            graphicsContext.stroke();
            // graphicsContext.fillOval(e.getX(),e.getY(),20,20);
        }
    }

    void onDrag(MouseEvent e) {
        if (stateManager.getSelectedTool() == StateManager.ToolType.PENCIL) {
            graphicsContext.lineTo(e.getX(), e.getY());
            graphicsContext.stroke();
        }
        //graphicsContext.fillOval(e.getX(),e.getY(),20,20);
    }

    void onMousePressed(MouseEvent e) {
        //graphicsContext.fillOval(e.getX(),e.getY(),20,20);
        graphicsContext.beginPath();
        graphicsContext.moveTo(e.getX(), e.getY());
    }

    void updateStrokeWidth() {
        switch (stateManager.getSelectedStrokeWidth()) {
            case THIN:
                graphicsContext.setLineWidth(2);
                break;
            case MEDIUM:
                graphicsContext.setLineWidth(5);
                break;
            case WIDE:
                graphicsContext.setLineWidth(10);
                break;
            default:
                graphicsContext.setLineWidth(2);
        }
    }

    WritableImage getSnapshotToSave() {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        //Take snapshot of the scene
        WritableImage writableImage = stackPane.snapshot(params, null);
        return  writableImage;
    }

    void saveImageAs() {
        String filePath = fileManager.getSaveAsFilePathFromUser((Stage)scene.getWindow());
        if (filePath != null) {
            fileManager.saveImageToFilePath(getSnapshotToSave(), filePath);
            stateManager.setSaveAsFilePath(filePath);
        }
    }

    void saveImage() {
        String filePath = stateManager.getSaveAsFilePath();
        if (filePath != null) {
            fileManager.saveImageToFilePath(getSnapshotToSave(), filePath);
        } else {
            saveImageAs();
        }
    }
}
