package ataxx.demo;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.json.JSONObject;
import javafx.css.Styleable;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Bleeth
 * @date ：2021-11-07
 * @description：Css辅助工具类
 */
public class SStyleHelper {

    private static Map<Node, JSONObject> styleMap = new HashMap<>();
    private static Map<Node, JSONObject> hoverStyleMap = new HashMap<>();
    private static Map<Node, JSONObject> focusStyleMap = new HashMap<>();
    private static Map<Node, JSONObject> pressStyleMap = new HashMap<>();
    private static Map<Styleable, JSONObject> styleableMap = new HashMap<>();

    private Node node;
    private Styleable styleable;


    public static SStyleHelper node(Node node) {
        SStyleHelper helper = new SStyleHelper();
        helper.node = node;
        if (!styleMap.containsKey(node)) {
            styleMap.put(node, new JSONObject());
        }
        if (!hoverStyleMap.containsKey(node)) {
            hoverStyleMap.put(node, new JSONObject());
        }
        if (!focusStyleMap.containsKey(node)) {
            focusStyleMap.put(node, new JSONObject());
        }
        if (!pressStyleMap.containsKey(node)) {
            pressStyleMap.put(node, new JSONObject());
        }
        return helper;
    }

    public static SStyleHelper style(Styleable styleable) {
        SStyleHelper helper = new SStyleHelper();
        helper.styleable = styleable;
        if (!styleableMap.containsKey(styleable)) {
            styleableMap.put(styleable, new JSONObject());
        }
        return helper;
    }



    public static String getStyle(Node node, String style) {
        JSONObject styleObj = styleMap.get(node);
        String styleValue = styleObj.getStr(style);
        return styleValue;
    }

    public static void copyStyle(Node source, Node target) {
        JSONObject sourceObj = styleMap.get(source);
        styleMap.put(target, sourceObj);
        SStyleHelper.node(target)
                .apply();
    }

    public SStyleHelper addHoverStyle(String style, Object value) {
        JSONObject styleObj = hoverStyleMap.get(node);
        styleObj.set(style, value);
        return this;
    }

    public SStyleHelper addFocusStyle(String style, Object value) {
        JSONObject styleObj = focusStyleMap.get(node);
        styleObj.set(style, value);
        return this;
    }

    public SStyleHelper addPressStyle(String style, Object value) {
        JSONObject styleObj = pressStyleMap.get(node);
        styleObj.set(style, value);
        return this;
    }

    public Object getHoverStyle(String style) {
        JSONObject styleObj = hoverStyleMap.get(node);
        Object styleValue = styleObj.getObj(style);
        return styleValue;
    }


    public SStyleHelper addStyle(String style, Object value) {
        JSONObject styleObj = styleMap.get(node);
        styleObj.set(style, value);

        //基本样式附加在hover上
        JSONObject hoverStyleObj = hoverStyleMap.get(node);
        hoverStyleObj.set(style, value);

        //基本样式附加在focus上
        JSONObject focusObj = focusStyleMap.get(node);
        focusObj.set(style, value);

        //基本样式附加在Press上
        JSONObject pressObj = pressStyleMap.get(node);
        pressObj.set(style, value);

        return this;
    }


    public void apply() {
        JSONObject styleObj = styleMap.get(node);
        if (styleObj.isEmpty()) {
            addStyle("-fx-background-color", "#ffffff");
        }

        JSONObject hoverStyleObj = hoverStyleMap.get(node);

        JSONObject focusObj = focusStyleMap.get(node);

        JSONObject pressObj = pressStyleMap.get(node);


        StrBuilder styleBuilder = new StrBuilder();
        StrBuilder hoverStyleBuilder = new StrBuilder();
        StrBuilder focusStyleBuilder = new StrBuilder();
        StrBuilder pressStyleBuilder = new StrBuilder();


        styleObj.entrySet().stream().forEach(entry -> {
            String key = entry.getKey();
            String value = styleObj.getStr(key);
            styleBuilder.append(key).append(":").append(value).append(";");
        });

        hoverStyleObj.entrySet().stream().forEach(entry -> {
            String key = entry.getKey();
            String value = hoverStyleObj.getStr(key);
            hoverStyleBuilder.append(key).append(":").append(value).append(";");
        });

        focusObj.entrySet().stream().forEach(entry -> {
            String key = entry.getKey();
            String value = focusObj.getStr(key);
            focusStyleBuilder.append(key).append(":").append(value).append(";");
        });

        pressObj.entrySet().stream().forEach(entry -> {
            String key = entry.getKey();
            String value = pressObj.getStr(key);
            pressStyleBuilder.append(key).append(":").append(value).append(";");
        });

        node.setStyle(styleBuilder.toString());


        if (!hoverStyleBuilder.isEmpty()) {
            node.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (node.isHover()) {
                    node.setStyle(hoverStyleBuilder.toString());
                } else {
                    node.setStyle(styleBuilder.toString());
                }
            });
        }

        if (!focusStyleBuilder.isEmpty()) {
            node.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (node.isFocused()) {
                    node.setStyle(focusStyleBuilder.toString());
                } else {
                    node.setStyle(styleBuilder.toString());
                }
            });
        }

        if (!pressStyleBuilder.isEmpty()) {
            node.pressedProperty().addListener((observable, oldValue, newValue) -> {
                if (node.isPressed()) {
                    node.setStyle(pressStyleBuilder.toString());
                } else {
                    if (node.isHover()) {
                        node.setStyle(hoverStyleBuilder.toString());
                    } else {
                        node.setStyle(styleBuilder.toString());
                    }
                }
            });
        }
    }

}
