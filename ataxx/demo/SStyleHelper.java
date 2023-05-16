package ataxx.demo;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.json.JSONObject;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class SStyleHelper {
    private static final Map<Node, JSONObject> styleMap = new HashMap<>();
    private static final Map<Node, JSONObject> hoverStyleMap = new HashMap<>();
    private static final Map<Node, JSONObject> focusStyleMap = new HashMap<>();
    private static final Map<Node, JSONObject> pressStyleMap = new HashMap<>();

    private Node node;


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


        focus(styleObj, hoverStyleObj, styleBuilder, hoverStyleBuilder);

        focus(focusObj, pressObj, focusStyleBuilder, pressStyleBuilder);

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

    private void focus(JSONObject focusObj, JSONObject pressObj, StrBuilder focusStyleBuilder, StrBuilder pressStyleBuilder) {
        focusObj.forEach((key, value1) -> {
            String value = focusObj.getStr(key);
            focusStyleBuilder.append(key).append(":").append(value).append(";");
        });

        pressObj.forEach((key, value1) -> {
            String value = pressObj.getStr(key);
            pressStyleBuilder.append(key).append(":").append(value).append(";");
        });
    }

}
