package com.profitz.app.data.model;

import com.profitz.app.promos.User;

import java.util.ArrayList;


public class GroupDetailModel {
    private GroupsModel details;
    private User admin;
    private ArrayList<User> members;
    private ArrayList<MessageModel> messages;
    private int number_of_members;

    public int getNumber_of_members() {
        return number_of_members;
    }

    public void setNumber_of_members(int number_of_members) {
        this.number_of_members = number_of_members;
    }

    public GroupsModel getDetails() {
        return details;
    }

    public void setDetails(GroupsModel details) {
        this.details = details;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public ArrayList<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageModel> messages) {
        this.messages = messages;
    }
}
