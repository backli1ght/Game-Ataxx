package ataxx.demo;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneExample extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = createRootPane();
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GridPane Example");
        primaryStage.show();
    }

    private BorderPane createRootPane() {
        BorderPane root = new BorderPane();
        root.setTop(createMenuBar());
        root.setCenter(createGridPane());
        return root;
    }

    private MenuBar createMenuBar() {
        // Create menu items
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");

        // Create game menu and add menu items
        Menu gameMenu = new Menu("Game");
        gameMenu.getItems().addAll(newItem, openItem, saveItem, exitItem);

        MenuItem cutItem = new MenuItem("Cut");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");

        // Create edit menu and add menu items
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(cutItem, copyItem, pasteItem);

        // Create menu bar and add menus
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(gameMenu, editMenu);

        return menuBar;
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(0);
        grid.setVgap(0);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 && j != 7) {
                    Label label = createLabel(7 - j);
                    GridPane.setConstraints(label, i, j);
                    grid.getChildren().add(label);
                    continue;
                }
                if (j == 7 && i != 0) {
                    char ch1 = (char) (64 + i);
                    Label label = createLabel(ch1);
                    GridPane.setConstraints(label, i, j);
                    grid.getChildren().add(label);
                    continue;
                }
                if (i == 0 && j == 7) {
                    continue;
                }
                Button button = createButton();
                GridPane.setConstraints(button, i, j);
                grid.getChildren().add(button);
            }
        }

        grid.setStyle("-fx-border-color: #a7bfd8;");
        return grid;
    }

    private Label createLabel(Object value) {
        Label label = new Label(value.toString());
        label.setPrefSize(40, 40);
        label.setAlignment(Pos.CENTER);
        if (value instanceof Integer) {
            GridPane.setHalignment(label, HPos.LEFT);
            GridPane.setValignment(label, VPos.CENTER);
        } else if (value instanceof Character) {
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.BOTTOM);
        }
        return label;
    }

    private Button createButton() {
        Button button = new Button("");
        button.setPrefSize(40, 40);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }




}
