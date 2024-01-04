package com.profitz.app.promos.interfaces;


import com.profitz.app.data.model.MessageModel;

public interface MessageHandler {
    void onMessageSend(MessageModel messageModel);
}
