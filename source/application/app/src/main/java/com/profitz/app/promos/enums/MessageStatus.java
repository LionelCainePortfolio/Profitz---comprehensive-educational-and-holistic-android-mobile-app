package com.profitz.app.promos.enums;



public enum MessageStatus {
    SENT("sent"),
    DELIVERED("delivered"),
    READ("read");

    String type;

    MessageStatus(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
