package com.profitz.app.promos.enums;


public enum NotificationType {
    NewPrivateMessage("new_private_message"),
    ServerUpdated("server_updated"),
    Error("error");
    String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
