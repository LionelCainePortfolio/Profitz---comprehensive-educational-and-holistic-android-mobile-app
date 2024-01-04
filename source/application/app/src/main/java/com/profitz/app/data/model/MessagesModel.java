package com.profitz.app.data.model;


public class MessagesModel {
    private String id;
    private String message_name;
    private String picture;
    private int num_unread;
    private boolean am_I_member;
    private String username;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isAm_I_member() {
        return am_I_member;
    }

    public void setAm_I_member(boolean am_I_member) {
        this.am_I_member = am_I_member;
    }

    public int getNum_unread() {
        return num_unread;
    }

    public void setNum_unread(int num_unread) {
        this.num_unread = num_unread;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessages_name() {
        return message_name;
    }


    public void setMessages_name(String message_name) {
        this.message_name = message_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String message_name) {
        this.username = username;
    }

}
