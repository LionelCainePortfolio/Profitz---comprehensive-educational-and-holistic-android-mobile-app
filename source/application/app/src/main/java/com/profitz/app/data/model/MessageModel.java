package com.profitz.app.data.model;

import com.profitz.app.promos.User;


public class MessageModel {
    private String id;
    private String content;
    private User send_from;
    private User send_to;
    private String attachment;
    private String attachmentExtension;
    private String message_type;
    private boolean is_read;
    private String created;
    private String system_message;
    private String many_messages_in_one_time;
    private  String first_message;
    private  String last_message;

    public String getAttachmentExtension() {
        return attachmentExtension;
    }

    public void setAttachmentExtension(String attachmentExtension) {
        this.attachmentExtension = attachmentExtension;
    }

    public String isSystemMessage(){return system_message;}
    public void setSystemMessage(String system_message){this.system_message = system_message; }

    public String getManyMessagesInOneMin(){return many_messages_in_one_time;}
    public void setManyMessagesInOneMin(String many_messages_in_one_time){this.many_messages_in_one_time = many_messages_in_one_time; }


    public String getManyMessagesInOneMinFirstMessage(){return first_message;}
    public void setManyMessagesInOneMinFirstMessage(String first_message){this.first_message = first_message; }

    public boolean isImageChanged;
    public boolean isImageChanged() {
        return isImageChanged;
    }

    public void setImageChanged(boolean imageChanged) {
        isImageChanged = imageChanged;
    }

    public String getManyMessagesInOneMinLastMessage(){return last_message;}
    public void setManyMessagesInOneMinLastMessage(String last_message){this.last_message = last_message; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSend_from() {
        return send_from;
    }

    public void setSend_from(User send_from) {
        this.send_from = send_from;
    }

    public User getSend_to() {
        return send_to;
    }

    public void setSend_to(User send_to) {
        this.send_to = send_to;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}
