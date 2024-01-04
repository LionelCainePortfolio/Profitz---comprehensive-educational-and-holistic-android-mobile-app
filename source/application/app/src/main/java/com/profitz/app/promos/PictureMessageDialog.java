package com.profitz.app.promos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;


import androidx.annotation.NonNull;

import com.profitz.app.R;import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.interfaces.DownloadAttachmentListener;
import com.profitz.app.util.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class PictureMessageDialog extends Dialog {
    private final Context context;
    private MessageModel messageModel;

    private ImageView image, download;
    private DownloadAttachmentListener downloadAttachmentListener;

    public PictureMessageDialog setDownloadAttachmentListener(DownloadAttachmentListener downloadAttachmentListener) {
        this.downloadAttachmentListener = downloadAttachmentListener;
        return this;
    }

    public PictureMessageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.dialog_image_detail);

        image = findViewById(R.id.image);
        download = findViewById(R.id.download);
//        Utility.showImage(context, Utility.BASE_URL + "/" + messageModel.getAttachment(), image);
        String imagePath = Utility.BASE_URL + "/" + messageModel.getAttachment();
        if (!Utility.isEmpty(imagePath)) {

            Picasso.with(context).load(imagePath)
                    .placeholder(R.drawable.placeholder_logo)
                    .error(R.drawable.placeholder_logo)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(image);
        }


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAttachmentListener.onDownloadClick(messageModel);
            }
        });
    }

    public PictureMessageDialog setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
        return this;
    }
}
