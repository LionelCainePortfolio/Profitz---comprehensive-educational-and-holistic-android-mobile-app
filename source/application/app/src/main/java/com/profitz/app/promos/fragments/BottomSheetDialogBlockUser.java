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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class BottomSheetDialogBlockUser extends BottomSheetDialogFragment {
  private BottomSheetListener mListener;
  private Context mContext;
  public static final int CONNECTION_TIMEOUT = 10000;
  public static final int READ_TIMEOUT = 15000;
  User user2;

  View v;
  private MyPreferenceManager preferenceManager;
  private TextView blockUser;
  private String friendId, friendName;
  private HttpManager httpManager;
  private NestedScrollView parentLayout;
  private TextView text_block_user_username;
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

    v = inflater.inflate(R.layout.dialog_block_user, container, false);

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
    getActivity().getWindow().setNavigationBarColor(Color.rgb(255,255,255));
    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));


    user2 = MyPreferenceManager.getInstance(mContext).getUser();
    //promoId = PromoDetailActivity.getId();
    float scale = getResources().getDisplayMetrics().density;

    SocketManager.getInstance();

    httpManager = new HttpManager(mContext);
    preferenceManager = new MyPreferenceManager(mContext);

    parentLayout = v.findViewById(R.id.parentLayout);
    blockUser = v.findViewById(R.id.blockUser);
    text_block_user_username = v.findViewById(R.id.text_block_user_username);

    Bundle mArgs = getArguments();
    friendId = mArgs.getString("friendId");
    friendName = mArgs.getString ("friendName");

    String text_block_user_username_string = "Czy na pewno chcesz zablokować użytkownika " +friendName + "?";
    text_block_user_username.setText(text_block_user_username_string);


    blockUser.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        httpManager.blockUser(
                friendId,
                new HttpResponse() {
                  @Override
                  public void onSuccess(String response) {

                    try {
                      JSONObject jsonObject = new JSONObject(response);
                      String status = jsonObject.getString("status");
                      //Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                      Toasty.normal(mContext, jsonObject.getString("message")).show();

                      if (status.equalsIgnoreCase("success")) {
                        dismiss();
                      }
                    } catch (JSONException e) {
                      e.printStackTrace();
                    }
                  }

                  @Override
                  public void onError(String error) {
                    //
                  }
                }
        );      }
    });

    return v;
  }


  private void enableButton() {
    blockUser.setEnabled(true);
  }

  private void disableButton() {
    blockUser.setEnabled(false);
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
