package com.profitz.app.promos.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.ArticlesActivity;
import com.profitz.app.promos.activities.ChatSupportActivity;
import com.profitz.app.promos.activities.PointsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BottomSheetDialogNeedHelp extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    String promoId;
    String userId;
    String commentId;
    String username;
    View v;
    BottomSheetDialog dialog2;
    String source;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         dialog2 = new BottomSheetDialog(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog2);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return dialog2;
    }
    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        Window window = dialog.getWindow();
        // Set the Dialog Background Transparent
// window.findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        v = inflater.inflate(R.layout.dialog_need_help, container, false);


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().setNavigationBarColor(Color.rgb(255,255,255));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        Bundle mArgs = getArguments();
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();

        if (mArgs != null) {
              source = mArgs.getString("source");
        }


        ImageView click_dismiss = (ImageView) v.findViewById(R.id.dialog_dismiss);
        click_dismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { dismiss();
                setWhiteNavigationBar(dialog2);
                getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.white));

            }
        });

        RelativeLayout tutorial = (RelativeLayout) v.findViewById(R.id.help_dialog_first);
        tutorial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
                setWhiteNavigationBar(dialog2);
                getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.white));
                if (source.equals("pointsActivity")){
                    ((PointsActivity)getActivity()).showTutorSequence(500);

                }

            }
        });
        RelativeLayout help_section = (RelativeLayout) v.findViewById(R.id.help_dialog_second);
        help_section.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
                //Intent intent = new Intent(getActivity(), HelpActivity.class);
                //v.getContext().startActivity(intent);
                setWhiteNavigationBar(dialog2);
                getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.white));
                if (source.equals("pointsActivity")){
                    Intent myIntent = new Intent(getActivity(), ArticlesActivity.class);
                    myIntent.putExtra("categories_ids","Punkty, Nagrody, Osiagniecia");
                    myIntent.putExtra("topic_name","Punkty, nagrody i osiągniecia");
                    startActivity(myIntent);
                }

            }
        });
        RelativeLayout chat = (RelativeLayout) v.findViewById(R.id.help_dialog_third);
        chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
                setWhiteNavigationBar(dialog2);
                Intent intent = new Intent(getActivity(), ChatSupportActivity.class);
                v.getContext().startActivity(intent);

            }
        });






        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setBlackNavigationBar (@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.rgb(30,30,30));

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
       // ((PromosActivity)getActivity()).setBlackNavigationBar();
        setBlackNavigationBar(dialog2);
        getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.bg_Profitz));

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement BottomSheetListener");
        }
    }

}
