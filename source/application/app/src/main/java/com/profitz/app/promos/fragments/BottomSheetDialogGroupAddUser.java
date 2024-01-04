package com.profitz.app.promos.fragments;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.profitz.app.R;import com.profitz.app.data.model.GroupDetailModel;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.async.SendFcmNotification;
import com.profitz.app.promos.enums.MessageType;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


public class BottomSheetDialogGroupAddUser extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;

    View v;
    private MyPreferenceManager preferenceManager;
    private TextView reject;
    private String groupId;

    private CircleImageView image;

    private GroupDetailModel groupDetailModel;
    private CustomLoadingDialog customLoadingDialog;
    private EditText username;
    private TextView findMember;
    private String groupName;
    private HttpManager httpManager;
    private User user;
    private TextView username_found;
    private TextView btnCancel;
    private TextView btnOk;
    private ImageView imageAnim;
    private ConstraintLayout const_buttons_add_to_group;
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
// window.findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

        v = inflater.inflate(R.layout.dialog_add_group_member, container, false);

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
        groupName = mArgs.getString("groupName");



        SocketManager.getInstance();




        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();


        SocketManager.getInstance();

        httpManager = new HttpManager(mContext);
        preferenceManager = new MyPreferenceManager(mContext);

        reject = v.findViewById(R.id.reject);


        httpManager = new HttpManager(mContext);
        groupDetailModel = new GroupDetailModel();
        customLoadingDialog = new CustomLoadingDialog(mContext);


        username = v.findViewById(R.id.username);
        findMember = v.findViewById(R.id.findMember);
        imageAnim = v.findViewById(R.id.imageAnim);
        image = v.findViewById(R.id.image);
        btnCancel = v.findViewById(R.id.reject);
        btnOk = v.findViewById(R.id.add);
        username_found = v.findViewById(R.id.username_found);
        const_buttons_add_to_group = v.findViewById(R.id.const_buttons_add_to_group);


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
         //
            }
        });

        findMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isEmpty(Utility.getStringValue(username))) {
                    //Toast.makeText(mContext, "Wprowadź nazwę użytkownika.", Toast.LENGTH_LONG).show();
                    Toasty.error(mContext, "Wprowadź nazwę użytkownika.").show();

                } else if (username.getText().toString().equalsIgnoreCase(preferenceManager.getUser().getUsername())) {
                 //   Toast.makeText(mContext, "Nie możesz dodać samego siebie.", Toast.LENGTH_LONG).show();
                    Toasty.error(mContext, "Nie możesz dodać samego siebie.").show();

                } else {
                    customLoadingDialog.show();
                    httpManager.getUserForMember(username.getText().toString(), groupId, new HttpResponse() {
                        @Override
                        public void onSuccess(String response) {
                            customLoadingDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");

                                if (status.equalsIgnoreCase("error")) {
                                    //Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                    Toasty.normal(mContext, message).show();

                                } else {
                                    user = new Gson().fromJson(jsonObject.getString("user"), User.class);
                                    username.setVisibility(View.GONE);
                                    findMember.setVisibility(View.GONE);
                                    image.setVisibility(View.VISIBLE);
                                    username_found.setVisibility(View.VISIBLE);

                                    const_buttons_add_to_group.setVisibility(View.VISIBLE);

                                    username_found.setText(user.getUsername());
                                    Utility.showImageAnimated(mContext, user.getPicture(), image, imageAnim);


                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (Utility.isEmpty(Utility.getStringValue(username))) {
                                              //  Toast.makeText(mContext, "Wprowadź nazwę użytkownika.", Toast.LENGTH_LONG).show();
                                                Toasty.error(mContext, "Wprowadź nazwę użytkownika.").show();

                                            } else if (user == null) {
                                                //  Toast.makeText(mContext, "Nie odnaleziono użytkownika o takiej nazwie.", Toast.LENGTH_LONG).show();
                                                Toasty.error(mContext, "Nie odnaleziono użytkownika o takiej nazwie.").show();

                                            } else {
                                                customLoadingDialog.show();
                                                httpManager.addMemberInGroup(groupId, String.valueOf(user.getId()), new HttpResponse() {
                                                    @Override
                                                    public void onSuccess(String response) {
                                                        customLoadingDialog.dismiss();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            String message = jsonObject.getString("message");
                                                            String status = jsonObject.getString("status");

                                                      //   Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                                            Toasty.normal(mContext, message).show();

                                                            if (status.equalsIgnoreCase("success")) {
                                                                MessageModel messageModel = new MessageModel();
                                                                messageModel.setContent("Otrzymałeś zaproszenie do grupy "+ groupName+".");
                                                                messageModel.setMessage_type(MessageType.GROUP_INVITE.getType());
                                                                new SendFcmNotification(user.getFcm_token(), messageModel, null).execute();
                                                                SocketManager.sendRequest(String.valueOf(user.getId()), MessageType.GROUP_INVITE.getType());
                                                                dismiss();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(String error) {
                                                        customLoadingDialog.dismiss();
                                                    }
                                                });
                                            }


                                            dismiss();
                                        }
                                    });
                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            user = null;
                                            image.setVisibility(View.GONE);
                                            username_found.setVisibility(View.GONE);
                                            const_buttons_add_to_group.setVisibility(View.GONE);
                                            username.setVisibility(View.VISIBLE);
                                            findMember.setVisibility(View.VISIBLE);
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            customLoadingDialog.dismiss();
                        }
                    });
                }


            }
        });

        return v;
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
