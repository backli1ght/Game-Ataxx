package ataxx;

//The GUI for the Ataxx Game

import ataxx.demo.SStyleHelper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.*;

public class GUI extends Application implements CommandSource, View, Reporter {
    private Game game;
    //窗口布局容器
    private BorderPane borderPane;
    private Stage primaryStage;
    //选择的棋子
    private boolean isSelectButton;
    private String selectData;
    //是否开启设置障碍的标志
    private boolean setBlockFlag;
    //用于设置人人/人机对战
    private int redIndex = 0;   //  0 ==manual  1=ai
    private int blueIndex = 1; //   0 ==manual  1=ai
    //棋格索引，可以通过a1 b1值找到对应的棋格
    private final Map<String, Button> buttonMap = new HashMap<>();

    /**
     * 更新棋局界面
     *
     */
    @Override
    public void update(Board board) {
        setBoard(borderPane);
        if (buttonMap.isEmpty()) {
            return;
        }
        int rowMaxIndex = 104;
        int colMaxIndex = 8;
        List<String> list = new ArrayList<>();
        for (int rowIndex = 97; rowIndex < rowMaxIndex; rowIndex++) {
            for (int colIndex = 1; colIndex < colMaxIndex; colIndex++) {
                char ch1 = (char) rowIndex;
                char charValue = (char) ('0' + (char) colIndex);
                PieceState state = board.getContent(ch1, charValue);
                String stateName = state.toString().toLowerCase();
                if (stateName.equals("empty")) {
                    continue;
                }
                if (stateName.equals("blocked")) {
                    setButtonBlock(buttonMap.get(ch1 + "" + colIndex));
                } else {
                    list.add(ch1 + "" + colIndex + "==========" + stateName);
                    setButtonColor(buttonMap.get(ch1 + "" + colIndex), stateName);
                }
            }
        }
        String score = board.getScore();
        setBottomScore(borderPane, score);
    }


    /**
     * 获胜后的提示信息
     *
     * @param state 表示是red 还是 blue 获胜
     */
    @Override
    public void announceWinner(PieceState state) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(state.toString() + " Wins!");
        alert.showAndWait();
    }

    @Override
    public void announceMove(Move move, PieceState player) {

    }

    @Override
    public void message(String format, Object... args) {
        System.out.printf(format, args);
        System.out.println();
    }

    @Override
    public void error(String format, Object... args) {

    }

    /**
     * gui程序启动的入口
     *
     */
    @Override
    public void start(Stage stage) throws Exception {
        borderPane = new BorderPane();
        primaryStage = stage;
        setMenu(borderPane);
        setBoard(borderPane);
        setBottomScore(borderPane, "0 red vs 0 blue");
        Scene scene = new Scene(borderPane, 390, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ataxx Game");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * 设置右侧score得分信息
     *
     * @param borderPane    gui容器根容器
     * @param message 得分信息
     */
    public void setBottomScore(BorderPane borderPane, String message) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(350);
        borderPane.setBottom(vBox);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        String[] parts = message.split(" vs ");
        Label redLabel = createColoredLabel(parts[0], Color.RED);
        Label blueLabel = createColoredLabel(parts[1], Color.BLUE);
        Label vsLable = createColoredLabel(" vs ", Color.GRAY);

        redLabel.setPadding(new Insets(-30, 0, 0, 0));
        blueLabel.setPadding(new Insets(-30, 0, 0, 0));
        vsLable.setPadding(new Insets(-30, 0, 0, 0));

        redLabel.setPrefHeight(70);
        hbox.getChildren().addAll(redLabel, vsLable, blueLabel);
        vBox.getChildren().add(hbox);
        vBox.setAlignment(Pos.CENTER);
    }

    private StackPane createLabelWrapper(Label label, double translationY) {
        StackPane wrapper = new StackPane();
        wrapper.getChildren().add(label);
        wrapper.setTranslateY(translationY);
        return wrapper;
    }

    private Label createColoredLabel(String text, Color color) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 30px; -fx-font-family: 'Britannic Bold'; -fx-font-weight: bold; -fx-text-fill: " + toHexCode(color) + ";");
        return label;
    }

    private String toHexCode(Color color) {
        return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    /**
     * 设置左侧的棋局
     *
     * @param borderPane gui容器根容器
     */
    void setBoard(BorderPane borderPane) {
        VBox vBox = new VBox();
//        borderPane.setCenter(vBox);
        borderPane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(0);
        grid.setVgap(0);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i == 0 && j != 7) {
                    Label label = new Label(7 - j + "");  //显示数字
                    GridPane.setConstraints(label, i, j);
                    grid.getChildren().add(label);
                    label.setPrefSize(40, 40);
                    label.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(label, HPos.LEFT);
                    GridPane.setValignment(label, VPos.CENTER);
                    continue;
                }
                if (j == 7 && i != 0) {
                    int ascii = 96 + i;
                    char ch1 = (char) ascii;
                    Label label = new Label(ch1 + "");   //显示字母参考
                    label.setPrefSize(40, 40);
                    label.setAlignment(Pos.CENTER);
                    GridPane.setConstraints(label, i, j);
                    grid.getChildren().add(label);
                    GridPane.setHalignment(label, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(label, VPos.BOTTOM);
                    continue;
                }
                int ascii = 96 + i;
                char ch1 = (char) ascii;
                int rowIndex = 7 - j;
                if (i == 0 && j == 7) {
                    continue;
                }
                Button button = new Button("");
                button.setUserData(ch1 + "" + rowIndex);    //通过button组建棋盘
                button.setOnMouseClicked(this::buttonClick);
                buttonMap.put(button.getUserData().toString(), button);
                button.setPrefSize(40, 40);
                GridPane.setConstraints(button, i, j);
                grid.getChildren().add(button);
            }
        }
        vBox.getChildren().add(grid);
        borderPane.setCenter(vBox);
    }

    /**
     * 在gui界面中添加菜单栏
     *
     * @param borderPane gui中的根容器
     */
    public void setMenu(BorderPane borderPane) {
        // 创建工具栏
        ToolBar toolBar = new ToolBar();

        // 创建并添加按钮
        Button newButton = new Button("New");
        newButton.setOnAction(event -> {
            setBoard(borderPane);
            this.game = new Game(this, this, this);
            game.newGame(redIndex, blueIndex);
            if (redIndex == 1 && blueIndex == 0) {
                game.aiRun();
            }
            while (redIndex == 1 && blueIndex == 1) {
                boolean winnerAnnounced = game.aiRun();
                if (winnerAnnounced) {
                    break;
                }
            }
        });
        newButton.setPrefSize(90, 30);

        toolBar.getItems().add(newButton);



        Button modeButton = new Button("Mode");
        modeButton.setOnAction(actionEvent -> {
            modeSetting();
        });
        toolBar.getItems().add(modeButton);
        modeButton.setPrefSize(90, 30);

        Button blockButton = new Button("Block");
        blockButton.setOnAction(this::setBlock);
        toolBar.getItems().add(blockButton);
        blockButton.setPrefSize(90, 30);

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(actionEvent -> {
            primaryStage.close();
        });
        toolBar.getItems().add(quitButton);
        quitButton.setPrefSize(90, 30);

        // 将工具栏添加到布局中
        borderPane.setTop(toolBar);
    }

    /**
     * 设置障碍事件的菜单项，这个事件只是设置一个可以进行设置障碍的状态
     *
     */
    public void setBlock(Event event) {
        setBlockFlag = true;
        for (Button btn : buttonMap.values()) {
            btn.setCursor(Cursor.CROSSHAIR);
        }
    }

    /**
     * 棋局中棋格的点击触发事件
     * - 1.设置障碍的事件
     * - 2.选择棋子的事件
     * - 3.取消棋子的事件，棋子选择后在此选择为取消选择
     * - 4.移动选择的棋子到指定位置的事件
     *
     */
    public void buttonClick(Event event) {
        Button btn = (Button) event.getSource();
        String promptData = btn.getUserData().toString();
        if (setBlockFlag) {//触发设置障碍的事件
            game.block(promptData);
            setBlockFlag = false;
            for (Button button : buttonMap.values()) {
                button.setCursor(Cursor.DEFAULT);
            }
            return;
        }
        if (!isSelectButton) {//触发选择棋子的事件
            isSelectButton = true;
            SStyleHelper.node(btn).addStyle("-fx-border-color", "#c9dec1").apply();
            selectData = promptData;
            return;
        }
        if (promptData.equals(selectData)) {//取消棋子的事件，棋子选择后在此选择为取消选择
            SStyleHelper.node(buttonMap.get(selectData)).addStyle("-fx-border-color", "black").apply();
            selectData = "";
            isSelectButton = false;
            return;
        }
        String moveStr = (selectData + "-" + promptData).toLowerCase(); //移动棋子
        if (!game.getAtaxxBoard().moveLegal(Move.move(moveStr))) { //校验如果棋子移动不合法，则提示
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operater Illegal Dialog");
            alert.setHeaderText(null);
            alert.setContentText("the move place is illegal,please move again.");
            alert.showAndWait();
            selectData = "";
            isSelectButton = false;
            return;
        }
        if (redIndex == 0) {
            game.manualRun(moveStr);
            selectData = "";
            isSelectButton = false;
            if (blueIndex == 1) {
                game.aiRun();
            }
        }
        if (blueIndex == 0) {
            game.manualRun(moveStr);
            selectData = "";
            isSelectButton = false;
            if (redIndex == 1) {
                game.aiRun();
            }
        }
    }

    /**
     * 设置棋格的颜色
     *
     * @param button 需要设置颜色的棋格
     * @param color  设置的颜色
     */
    public static void setButtonColor(Button button, String color) {
        Circle circle = new Circle();
        circle.setRadius(11);
        SStyleHelper.node(circle).addStyle("-fx-fill", color).apply();
        // 将圆形设置为按钮的背景
        button.setBackground(null);
        button.setGraphic(circle);
        SStyleHelper.node(button).addStyle("-fx-border-color", "BLACK").apply();
    }

    /**
     * 设置棋格是否为障碍
     *
     * @param button 需要设置为障碍的棋子
     */
    public static void setButtonBlock(Button button) {
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
//
////                0.2 * width, 0.0,
////                0.4 * width, 0.0,
////                0.6 * width, 0.2 * height,
////                width, 0.2 * height,
////                width, 0.8 * height,
////                0.6 * width, 0.8 * height,
////                0.4 * width, height,
////                0.2 * width, height,
////                0.2 * width, 0.8 * height,
////                0.0, 0.8 * height,
////                0.0, 0.2 * height,
////                0.2 * width, 0.2 * height
//
////                0.2 * width, 0.0,
////                0.5 * width, 0.15 * height,
////                0.8 * width, 0.0,
////                width, 0.3 * height,
////                0.8 * width, 0.6 * height,
////                width, height,
////                0.5 * width, 0.85 * height,
////                0.2 * width, height,
////                0.0, 0.7 * height,
////                0.0, 0.3 * height,
////                0.2 * width, 0.15 * height
//
        );
        SStyleHelper.node(star).addStyle("-fx-fill", "#7030a0").apply();
        button.setBackground(null);
        button.setGraphic(star);
        SStyleHelper.node(button).addStyle("-fx-border-color", "BLACK").apply();
    }


    @Override
    public String getCommand(String prompt) {
        return null;
    }


    /**
     * 设置模式
     * - 1. 设置是人机还是人人对战
     * - 2. 设置人机对战，人先手还是ai先手
     */
    public void modeSetting() {
        // 创建一个按钮
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Choose the Mode");
        dialog.getDialogPane().setPrefSize(350, 150);

        // player mode
        Label redLabel = new Label("Red:");
        ComboBox<String> redBox = new ComboBox<>();
        redBox.setPrefWidth(240);
        redBox.setItems(FXCollections.observableArrayList("Manual", "AI"));
        redBox.getSelectionModel().select(redIndex);

        //color mode
        Label blueLabel = new Label("Blue:");
        ComboBox<String> blueBox = new ComboBox<>();
        blueBox.setPrefWidth(240);
        blueBox.setItems(FXCollections.observableArrayList("Manual", "AI"));
        blueBox.getSelectionModel().select(blueIndex);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.add(redLabel, 1, 1);
        grid.add(redBox, 2, 1);
        grid.add(blueLabel, 1, 2);
        grid.add(blueBox, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            redIndex = redBox.getSelectionModel().getSelectedIndex();
            blueIndex = blueBox.getSelectionModel().getSelectedIndex();
        }
    }
}
