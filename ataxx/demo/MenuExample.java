package ataxx.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuExample extends Application {
@Override
public void start(Stage primaryStage) throws Exception {
    BorderPane root = new BorderPane();

    // Create file menu
    Menu fileMenu = new Menu("File");
    MenuItem newItem = new MenuItem("New");
    MenuItem openItem = new MenuItem("Open");
    MenuItem saveItem = new MenuItem("Save");
    MenuItem exitItem = new MenuItem("Exit");
    fileMenu.getItems().addAll(newItem, openItem, saveItem, new SeparatorMenuItem(), exitItem);

    // Create edit menu
    Menu editMenu = new Menu("Edit");
    MenuItem cutItem = new MenuItem("Cut");
    MenuItem copyItem = new MenuItem("Copy");
    MenuItem pasteItem = new MenuItem("Paste");
    editMenu.getItems().addAll(cutItem, copyItem, pasteItem);

    // Create menu bar and add menus
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu, editMenu);

    // Set the menu bar as the top node of the border pane
    root.setTop(menuBar);

    Scene scene = new Scene(root, 400, 300);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Menu Example");
    primaryStage.show();
}
    public static void main(String[] args) {
        launch(args);
    }
}