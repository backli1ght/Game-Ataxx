package ataxx.demo;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneExample extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        setMenu(root);
        setBoard(root);
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GridPane Example");
        primaryStage.show();
    }


    public void setBoard(BorderPane root){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(0);
        grid.setVgap(0);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 && j != 7) {
                    Label label = new Label(7 - j + "");
                    GridPane.setConstraints(label, i, j);
                    grid.getChildren().add(label);
                    label.setPrefSize(40, 40);
                    label.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(label, HPos.LEFT);
                    GridPane.setValignment(label, VPos.CENTER);
//                    SStyleHelper.node(label).addStyle("-fx-border-color", "#a7bfd8").apply();
                    continue;
                }
                if (j == 7 && i != 0) {
                    int ascii = 64 + i;
                    char ch1 = (char) ascii;
                    Label label = new Label(ch1 + "");
                    label.setPrefSize(40, 40);
                    label.setAlignment(Pos.CENTER);
                    GridPane.setConstraints(label, i, j);
                    grid.getChildren().add(label);
                    GridPane.setHalignment(label, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(label, VPos.BOTTOM);
//                    SStyleHelper.node(label).addStyle("-fx-border-color", "#a7bfd8").apply();
                    continue;
                }
                if (i == 0 && j == 7) {
                    continue;
                }
                Button button = new Button("");
                button.setPrefSize(40, 40);
                GridPane.setConstraints(button, i, j);
                grid.getChildren().add(button);
            }
        }
        SStyleHelper.node(grid).addStyle("-fx-border-color", "#a7bfd8").apply();
        root.setCenter(grid);
    }




    public void setMenu(BorderPane root){
        // 创建菜单项
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");

        // 创建菜单并添加菜单项
        Menu gameMenu = new Menu("Game");
        gameMenu.getItems().addAll(newItem, openItem, saveItem, exitItem);

        MenuItem cutItem = new MenuItem("Cut");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");

        // 创建菜单并添加菜单项
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().addAll(cutItem, copyItem, pasteItem);

        // 创建菜单栏并添加菜单
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(gameMenu, editMenu);

        // 将菜单栏添加到布局中
        root.setTop(menuBar);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
