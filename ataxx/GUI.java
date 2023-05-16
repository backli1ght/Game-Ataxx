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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GUI extends Application implements CommandSource, View, Reporter {
    private Game game;
    //窗口布局容器
    private BorderPane root;
    private GridPane grid;
    private Stage primaryStage;
    //选择的棋子
    private boolean isSelectBtn;
    private String selectData;
    //是否开启设置障碍的标志
    private boolean setBlockFlag;
    //用于设置人人/人机对战
    private int redIndex = 0;   //  0 ==manual  1=ai
    private int blueIndex = 1; //   0 ==manual  1=ai
    //棋格索引，可以通过a1 b1值找到对应的棋格
    private Map<String, Button> btnMap = new HashMap<>();

    /**
     * 更新棋局界面
     *
     * @param board
     */
    @Override
    public void update(Board board) {
        setLeftBoard(root);
        if (btnMap.isEmpty()) {
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
                    setBtnBlock(btnMap.get(ch1 + "" + colIndex));
                } else {
                    list.add(ch1 + "" + colIndex + "==========" + stateName);
                    setBtnColor(btnMap.get(ch1 + "" + colIndex), stateName);
                }
            }
        }
        String score = board.getScore();
        setRightScore(root, "SCORE:" + score);
    }


    /**
     * 获胜后的提示信息
     *
     * @param state 表示是red 还是 blue 获胜
     */
    @Override
    public void announceWinner(PieceState state) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over Dialog");
        alert.setHeaderText(null);
        alert.setContentText(state.toString() + " is the Winner");
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
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        root = new BorderPane();
        primaryStage = stage;
        setMenu(root);
        setLeftBoard(root);
        setRightScore(root, "SCORE: 0 red vs 0 blue");
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ataxx Game");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * 设置右侧score得分信息
     *
     * @param root    gui容器根容器
     * @param message 得分信息
     */
    public void setRightScore(BorderPane root, String message) {
        VBox vBox = new VBox();
        vBox.setPrefWidth(350);
        root.setRight(vBox);
        Label label = new Label(message);
        SStyleHelper.node(label).addStyle("-fx-font-size", "20px").apply();
        label.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label);
        vBox.setAlignment(Pos.CENTER);
    }

    /**
     * 设置左侧的棋局
     *
     * @param root gui容器根容器
     */
    void setLeftBoard(BorderPane root) {
        VBox vBox = new VBox();
        root.setLeft(vBox);
        vBox.setAlignment(Pos.CENTER);

        grid = new GridPane();
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
                button.setOnMouseClicked(mouseEvent -> {
                    buttonClick(mouseEvent);
                });
                btnMap.put(button.getUserData().toString(), button);
                button.setPrefSize(40, 40);
                GridPane.setConstraints(button, i, j);
                grid.getChildren().add(button);
            }
        }
        vBox.getChildren().add(grid);
    }

    /**
     * 在gui界面中添加菜单栏
     *
     * @param root gui中的根容器
     */
    public void setMenu(BorderPane root) {
        // 创建Game菜单项
        MenuItem newItem = new MenuItem("New");
        newItem.setOnAction(event -> {
            setLeftBoard(root);
            this.game = new Game(this, this, this);
            game.newGame(redIndex, blueIndex);
            if (redIndex == 1 && blueIndex == 0) { //表示ai先走，后面和人进行对战
                game.aiRun();
            }
            while (redIndex == 1 && blueIndex == 1) {  //ai 互相对战
                boolean winnerAnnounced = game.aiRun();
                if (winnerAnnounced) {
                    break;
                }
            }
        });
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.setOnAction(actionEvent -> {
            primaryStage.close();
        });
        Menu gameMenu = new Menu("Game");
        gameMenu.getItems().addAll(newItem, quitItem);

        // 创建Setting菜单项
        MenuItem modeItem = new MenuItem("Mode");
        modeItem.setOnAction(actionEvent -> {
            modeSetting();
        });
        MenuItem blockItem = new MenuItem("Block");
        blockItem.setOnAction(actionEvent -> {
            setBlock(actionEvent);
        });
        Menu editMenu = new Menu("Setting");
        editMenu.getItems().addAll(modeItem, blockItem);

        // 创建菜单栏
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(gameMenu, editMenu);
        // 将菜单栏添加到布局中
        root.setTop(menuBar);
    }

    /**
     * 设置障碍事件的菜单项，这个事件只是设置一个可以进行设置障碍的状态
     *
     * @param event
     */
    public void setBlock(Event event) {
        setBlockFlag = true;
        for (Button btn : btnMap.values()) {
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
     * @param event
     */
    public void buttonClick(Event event) {
        Button btn = (Button) event.getSource();
        String promptData = btn.getUserData().toString();
        if (setBlockFlag) {//触发设置障碍的事件
            game.block(promptData);
            setBlockFlag = false;
            for (Button button : btnMap.values()) {
                button.setCursor(Cursor.DEFAULT);
            }
            return;
        }
        if (isSelectBtn == false) {//触发选择棋子的事件
            isSelectBtn = true;
            SStyleHelper.node(btn).addStyle("-fx-border-color", "#c9dec1").apply();
            selectData = promptData;
            return;
        }
        if (promptData.equals(selectData)) {//取消棋子的事件，棋子选择后在此选择为取消选择
            SStyleHelper.node(btnMap.get(selectData)).addStyle("-fx-border-color", "black").apply();
            selectData = "";
            isSelectBtn = false;
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
            isSelectBtn = false;
            return;
        }
        if (redIndex == 0) {
            game.manualRun(moveStr);
            selectData = "";
            isSelectBtn = false;
            if (blueIndex == 1) {
                game.aiRun();
            }
        }
        if (blueIndex == 0) {
            game.manualRun(moveStr);
            selectData = "";
            isSelectBtn = false;
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
    public static void setBtnColor(Button button, String color) {
        Circle circle = new Circle();
        circle.setRadius(10);
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
    public static void setBtnBlock(Button button) {
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
        dialog.setTitle("Mode Setting Dialog");
        dialog.getDialogPane().setPrefSize(400, 200);

        // player mode
        Label redLabel = new Label("Red Player:");
        ComboBox<String> redBox = new ComboBox();
        redBox.setPrefWidth(240);
        redBox.setItems(FXCollections.observableArrayList("manual", "ai"));
        redBox.getSelectionModel().select(redIndex);

        //color mode
        Label blueLabel = new Label("Blue Player:");
        ComboBox<String> blueBox = new ComboBox();
        blueBox.setPrefWidth(240);
        blueBox.setItems(FXCollections.observableArrayList("manual", "ai"));
        blueBox.getSelectionModel().select(blueIndex);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.add(redLabel, 1, 1);
        grid.add(redBox, 2, 1);
        grid.add(blueLabel, 1, 2);
        grid.add(blueBox, 2, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType okButton = new ButtonType("保存", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("不保存", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            redIndex = redBox.getSelectionModel().getSelectedIndex();
            blueIndex = blueBox.getSelectionModel().getSelectedIndex();
        }
    }


}
