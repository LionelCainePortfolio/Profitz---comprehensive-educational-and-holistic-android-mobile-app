package com.profitz.app.promos.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.profitz.app.R;import com.profitz.app.data.model.GroupDetailModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;

import com.profitz.app.promos.activities.GroupMetaDetailActivity;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.Utility;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.EasyPermissions;


public class BottomSheetDialogGroupEdit extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;

    View v;
    private MyPreferenceManager preferenceManager;
    private TextView reject;
    private String groupId;
    private EditText groupName;
    private TextView editGroup;
    private CircleImageView image;
    private final String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private GroupDetailModel groupDetailModel;
    private CustomLoadingDialog customLoadingDialog;



    private HttpManager httpManager;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog);
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        Window window = dialog.getWindow();
        if (dialog != null && dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        // Set the Dialog Background Transparent
       //window.findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.white));
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        v = inflater.inflate(R.layout.dialog_group_edit, container, false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);


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
        getActivity().getWindow().setNavigationBarColor(Color.rgb(255, 255, 255));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        Bundle mArgs = getArguments();
        groupId = mArgs.getString("groupId");


        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();


        SocketManager.getInstance();

        httpManager = new HttpManager(mContext);
        preferenceManager = new MyPreferenceManager(mContext);

        reject = v.findViewById(R.id.reject);


        httpManager = new HttpManager(mContext);
        groupDetailModel = new GroupDetailModel();
        customLoadingDialog = new CustomLoadingDialog(mContext);

        groupName = v.findViewById(R.id.groupName);
        image = v.findViewById(R.id.image);
        editGroup = v.findViewById(R.id.editGroup);
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int length = groupName.getText().length();
                if (length >=3){
                    customLoadingDialog.show();
                    httpManager.editGroup(groupId, groupName.getText().toString(), Utility.BASE_URL + "/" + groupDetailModel.getDetails().getPicture(), new HttpResponse() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                            String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                String filePath = jsonObject.getString("file_path");
                                groupDetailModel.getDetails().setPicture(filePath);
                                //Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                Toasty.normal(mContext, message).show();

                                dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            customLoadingDialog.dismiss();
                        }

                        @Override
                        public void onError(String error) {
                            Log.i("my_log", error);
                        }
                    });
                }
                else{
                  //  Toast.makeText(mContext, "Wprowadź minimum 3 znaki.", Toast.LENGTH_LONG).show();
                    Toasty.error(mContext, "Wprowadź minimum 3 znaki.").show();

                }

            }
        });

        httpManager.getGroupMetaDetail(groupId, new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                groupDetailModel = new Gson().fromJson(response, GroupDetailModel.class);
                groupName.setText(groupDetailModel.getDetails().getGroup_name());
                Utility.showImage(mContext, Utility.BASE_URL + "/" + groupDetailModel.getDetails().getPicture(), image);
            }

            @Override
            public void onError(String error) {
                //
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EasyPermissions.hasPermissions(getActivity(), galleryPermissions)) {
                    ((GroupMetaDetailActivity) getActivity()).selectImage();
                } else {
                    EasyPermissions.requestPermissions(getActivity(), "Dostęp do galerii", 101, galleryPermissions);
                }
            }
        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((GroupMetaDetailActivity) getActivity()).getGroupDetail();
                dismiss();
            }
        });


        return v;
    }


    public void changePicture(byte[] bytesImage){
        Glide.with(mContext)
                .asBitmap()
                .load(bytesImage)
                .placeholder(R.drawable.placeholder_logo)
                .into(image);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);

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
