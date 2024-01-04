package com.profitz.app.promos.enums;



public enum LeftMenuItems {
    GROUP_REQUESTS("Group requests"),
    FRIEND_REQUESTS("Friend requests"),
    LOGOUT("Log out");
    String item;

    LeftMenuItems(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
