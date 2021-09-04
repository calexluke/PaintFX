package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application {
    Group root;
    Scene scene;
    VBox vbox;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Group();
        vbox = new VBox();
        root.getChildren().add(vbox);
        scene = new Scene(root, 1000, 1000);

        configureMenuBar();
        loadDefaultImage();

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
        quit.setOnAction(e -> quitApplication());

        MenuItem load = new MenuItem("Load New Image");
        load.setOnAction(e -> selectNewImage(e));

        MenuItem restore = new MenuItem("Restore default image");
        restore.setOnAction(e -> loadDefaultImage());

        // add items to menus
        mainMenu.getItems().add(quit);
        imageMenu.getItems().add(restore);
        imageMenu.getItems().add(load);

        // add menus to menu bar
        menuBar.getMenus().add(mainMenu);
        menuBar.getMenus().add(imageMenu);

        // add menu bar to vbox
        vbox.getChildren().add(menuBar);
    }

    private void quitApplication() {
        // handle shutdown stuff here
        Platform.exit();
    }

    private void selectNewImage(ActionEvent event) {
        Image newImage;
        Stage stage = (Stage) root.getScene().getWindow();
        String filePath = getImageFilePathFromUser(stage);
        if (filePath != null) {
            try {
                newImage = new Image(new FileInputStream(filePath));
                displayImage(newImage);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        } else {
            // This should not happen
            System.out.println("Bad file type");
        }
    }

    private String getImageFilePathFromUser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.PNG");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String path = file.getAbsolutePath();
            return path;
        } else {
            return null;
        }
    }

    private void loadDefaultImage() {
        displayImage(new Image("/sample/Assets/PAIN(t).png"));
    }

    private void displayImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitHeight(1000);
        imageView.setFitWidth(1000);
        imageView.setPreserveRatio(true);

        // If image already exists, remove it
        if (vbox.getChildren().size() > 1) {
            vbox.getChildren().remove(1);
        }
        vbox.getChildren().add(imageView);
    }
}
