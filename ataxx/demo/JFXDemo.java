package ataxx.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 * @author BleethNie
 * @version 1.0
 * @date 2023-05-14 20:28
 **/
public class JFXDemo extends Application {

    public void run1(Stage primaryStage) throws Exception {
        Button button = new Button("Show Alert");
        button.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("This is an information message!");
            alert.showAndWait();
        });

        StackPane root = new StackPane(button);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Alert Example");
        primaryStage.show();

    }

    public void run2(Stage primaryStage) throws Exception {
        Button button = new Button("Click me");
        button.setPrefSize(50, 50);
        // 创建内切圆形状
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setStyle("-fx-fill: blue;");

        // 将圆形设置为按钮的背景
        button.setBackground(null);
        button.setGraphic(circle);

        StackPane root = new StackPane(button);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Button Background Example");
        primaryStage.show();
    }

    public void run3(Stage primaryStage) throws Exception {
        Button button = new Button("Click me");
        button.setPrefSize(100,100);
        // 设置按钮样式
        SStyleHelper.node(button)
                .addStyle("-fx-background-color","blue")
                .addStyle("-fx-background-radius","100px")
                .addStyle("-fx-border-color", "#a7bfd8")
                .apply();

        StackPane root = new StackPane(button);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Button Color Example");
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        double width = 20; // 宽度
        double height = 20; // 高度

        // 创建五角星形状
        Polygon star = new Polygon();
        star.getPoints().addAll(
                0.5 * width, 0.0,
                0.65 * width, 0.35 * height,
                width, 0.35 * height,
                0.7 * width, 0.6 * height,
                0.85 * width, height,
                0.5 * width, 0.75 * height,
                0.15 * width, height,
                0.3 * width, 0.6 * height,
                0.0, 0.35 * height,
                0.35 * width, 0.35 * height
        );
//        star.setFill(Color.YELLOW);

        StackPane root = new StackPane(star);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Star Shape Example");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}