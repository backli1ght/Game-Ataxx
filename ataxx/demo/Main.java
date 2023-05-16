package ataxx.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 创建一个按钮
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Mode Setting Dialog");
        dialog.getDialogPane().setPrefSize(400, 200);

        // player mode
        Label playerLabel = new Label("player mode:");
        ComboBox<String> playerBox = new ComboBox();
        playerBox.setPrefWidth(240);
        playerBox.setItems(FXCollections.observableArrayList("manual VS ai", "manual VS manual"));
        playerBox.getSelectionModel().selectFirst();

        //color mode
        Label colorLabel = new Label("red user:");
        ComboBox<String> colorBox = new ComboBox();
        colorBox.setPrefWidth(240);
        colorBox.setItems(FXCollections.observableArrayList("manual", "ai"));
        colorBox.getSelectionModel().selectFirst();

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.add(playerLabel, 1, 1);
        grid.add(playerBox, 2, 1);
        grid.add(colorLabel, 1, 2);
        grid.add(colorBox, 2, 2);
        dialog.getDialogPane().setContent(grid);

        playerBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("manual VS ai")) {
                colorBox.setItems(FXCollections.observableArrayList("manual", "ai"));
                colorBox.getSelectionModel().selectFirst();
            } else if (newValue.equals("manual VS manual")) {
                colorBox.setItems(FXCollections.observableArrayList("manual"));
                colorBox.getSelectionModel().selectFirst();
            }
        });
        ButtonType okButton = new ButtonType("保存", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("不保存", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        System.err.println(dialog.getResult());
        if (result.isPresent() && result.get() == okButton) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}