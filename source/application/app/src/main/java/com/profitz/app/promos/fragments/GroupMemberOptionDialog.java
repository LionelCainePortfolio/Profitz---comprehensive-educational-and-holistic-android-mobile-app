package com.profitz.app.promos.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.profitz.app.R;import com.profitz.app.promos.User;
import com.profitz.app.util.Utility;




public class GroupMemberOptionDialog extends Dialog {
    private final Context context;
    private TextView userData;
    private User user;
    private ImageView userImage;
    private Button btnDelete, btnBan;
    private Runnable onDelete, onBan;

    public GroupMemberOptionDialog setOnBan(Runnable onBan) {
        this.onBan = onBan;
        return this;
    }

    public GroupMemberOptionDialog setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    public GroupMemberOptionDialog setUser(User user) {
        this.user = user;
        return this;
    }

    public GroupMemberOptionDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_group_member_option);

        userData = findViewById(R.id.userData);
        userImage = findViewById(R.id.userImage);
        btnDelete = findViewById(R.id.btnDelete);
        btnBan = findViewById(R.id.btnBan);

        if (user.isIs_ban()) {
            btnBan.setText("Unblock");
        }

        btnBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBan != null) {
                    onBan.run();
                }
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDelete != null) {
                    onDelete.run();
                }
                dismiss();
            }
        });

        userData.setText("Name: " + user.getName() + "\n\n" + "Phone: " + user.getPhone());
        Utility.showImage(context, Utility.BASE_URL + "/" + user.getPicture(), userImage);
    }
}
