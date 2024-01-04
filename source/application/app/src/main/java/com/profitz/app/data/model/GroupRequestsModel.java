package com.profitz.app.data.model;

import com.profitz.app.promos.User;


public class GroupRequestsModel {
    private GroupsModel group;
    private User user;

    public GroupsModel getGroup() {
        return group;
    }

    public void setGroup(GroupsModel group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
