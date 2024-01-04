package com.profitz.app.promos;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.profitz.app.R;


public class CustomLoadingDialog extends Dialog {
    private final Context context;

    public CustomLoadingDialog(@NonNull Context context) {
        super(context, R.style.NewDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        setContentView(R.layout.dialog_custom_loading_layout);
    }
}
