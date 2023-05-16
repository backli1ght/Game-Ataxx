package ataxx.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
public void start(Stage primaryStage) throws Exception {
    // Create a dialog
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Mode Setting Dialog");
    dialog.getDialogPane().setPrefSize(400, 200);

    // Player mode
    Label playerLabel = new Label("player mode:");
    ComboBox<String> playerBox = createComboBox("manual VS ai", "manual VS manual");

    // Color mode
    Label colorLabel = new Label("red user:");
    ComboBox<String> colorBox = createComboBox("manual", "ai");

    GridPane grid = createGridPane();
    grid.add(playerLabel, 1, 1);
    grid.add(playerBox, 2, 1);
    grid.add(colorLabel, 1, 2);
    grid.add(colorBox, 2, 2);
    dialog.getDialogPane().setContent(grid);

    playerBox.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue.equals("manual VS ai")) {
            colorBox.setItems(FXCollections.observableArrayList("manual", "ai"));
        } else if (newValue.equals("manual VS manual")) {
            colorBox.setItems(FXCollections.observableArrayList("manual"));
        }
        colorBox.getSelectionModel().selectFirst();
    });

    ButtonType okButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

    System.err.println(dialog.getResult());

}

    private ComboBox<String> createComboBox(String... items) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(240);
        comboBox.setItems(FXCollections.observableArrayList(items));
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}