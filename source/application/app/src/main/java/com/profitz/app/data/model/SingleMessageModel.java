package com.profitz.app.data.model;

import com.profitz.app.promos.User;


public class SingleMessageModel {
    private String _id;
    private String message;
    private boolean is_discussion;
    private String send_from;
    private String send_to;
    private long created;
    private User user;
    private boolean isAnonymous;
    private String imageAttachment;

    // From enumeration
    private String messageStatus;

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getImageAttachment() {
        return imageAttachment;
    }

    public void setImageAttachment(String imageAttachment) {
        this.imageAttachment = imageAttachment;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SingleMessageModel() {
    }

    public SingleMessageModel(String message, String send_from) {
        this.message = message;
        this.send_from = send_from;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean is_discussion() {
        return is_discussion;
    }

    public void setIs_discussion(boolean is_discussion) {
        this.is_discussion = is_discussion;
    }

    public String getSend_from() {
        return send_from;
    }

    public void setSend_from(String send_from) {
        this.send_from = send_from;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
