package com.profitz.app.promos.interfaces;


import com.profitz.app.data.model.MessageModel;

public interface DownloadAttachmentListener {
    void onDownloadClick(MessageModel messageModel);
    void onDownloaded();
}
