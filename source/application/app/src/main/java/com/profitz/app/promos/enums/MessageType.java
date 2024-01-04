package com.profitz.app.promos.enums;



public enum MessageType {
    GROUP("group"),
    PRIVATE_CHAT("private"),

    GROUP_REQUEST("group_request"),
    GROUP_INVITE("group_invite"),

    GROUP_REQUEST_RESPONSE("group_request_response"),
    GROUP_INVITE_RESPONSE("group_invite_response"),

    GROUP_BAN("group_ban"),
    GROUP_REMOVE("group_remove"),

    FRIEND_REQUEST_SENT("friend_request_sent"),
    FRIEND_REQUEST_RESPONSE("friend_request_response");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
