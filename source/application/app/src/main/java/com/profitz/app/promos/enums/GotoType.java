package com.profitz.app.promos.enums;

public enum GotoType {
    PRIVATE_TAB("private_tab");

    String type;

    GotoType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
