package com.profitz.app.data.model;


public class GroupsModel {
    private String id;
    private String group_name;
    private String created_by;
    private String created_at;
    private String image_url;
    private int num_unread;
    private boolean am_I_member;

    public String getPicture() {
        return image_url;
    }

    public void setPicture(String image_url) {
        this.image_url = image_url;
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

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
