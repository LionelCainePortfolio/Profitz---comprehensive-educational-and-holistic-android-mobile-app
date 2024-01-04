package com.profitz.app.data.model;

public class UserModel {
    // Basic info
    private String id;
    private String name;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String fcm_token;
    private String picture;
    private boolean is_online;
    private boolean is_ban;
    private String last_scene;
    private int unread_messages;

    public int getUnread_messages() {
        return unread_messages;
    }

    public void setUnread_messages(int unread_messages) {
        this.unread_messages = unread_messages;
    }

    public boolean isIs_ban() {
        return is_ban;
    }

    public void setIs_ban(boolean is_ban) {
        this.is_ban = is_ban;
    }

    public UserModel() {
    }

    public UserModel(String username, String name, String phone, String password, String email) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setPhone(phone);
        setEmail(email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isIs_online() {
        return is_online;
    }

    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }

    public String getLast_scene() {
        return last_scene;
    }

    public void setLast_scene(String last_scene) {
        this.last_scene = last_scene;
    }
}
