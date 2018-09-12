package com.xphsc.easyjdbc.core.enums;

/**
 * Created by ${huipei.x}
 */
public enum LikeType {

    LEFT("left", "左边%"),
    RIGHT("right", "右边%"),
    CUSTOM("custom", "定制"),
    DEFAULT("default", "两边%");

    private final String type;
    private final String name;

    private LikeType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }
}
