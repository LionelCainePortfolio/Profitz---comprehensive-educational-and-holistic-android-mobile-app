package com.profitz.app.data.model;


public class TopMenuModel {
    private int image, notification;
    private String title;

    public TopMenuModel(int image, String title, int notification) {
        this.image = image;
        this.title = title;
        this.notification = notification;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
