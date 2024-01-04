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
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BottomSheetDialogDisabledFuture extends BottomSheetDialogFragment {
  private BottomSheetListener mListener;
  private Context mContext;
  public static final int CONNECTION_TIMEOUT = 10000;
  public static final int READ_TIMEOUT = 15000;
  User user2;
  String source;
  View v;
  Bundle mArgs;
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

    v = inflater.inflate(R.layout.dialog_disabled_future, container, false);

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

    mArgs = getArguments();
    if (mArgs != null) {
      source = mArgs.getString("source");
    }
    else{
      source = "promosActivity";
    }
    if (source !=null && source.equals("promosActivity")){
      getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.bg_Profitz));
    }
    else if (source !=null && source.equals("pointsActivity")){
      getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }
    else{
      getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));

    }

    user2 = MyPreferenceManager.getInstance(mContext).getUser();
    //promoId = PromoDetailActivity.getId();


    LottieAnimationView animationView;
    TextView click3 = v.findViewById(R.id.button_understand_disable_future);
    click3.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if (source !=null && source.equals("promosActivity")){
          getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.bg_Profitz));
        }
        else if (source !=null && source.equals("pointsActivity")){
          getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }
        dismiss();

      }
    });
    animationView = v.findViewById(R.id.anim_declined);
    animationView.setVisibility(View.VISIBLE);

    animationView
            .addAnimatorUpdateListener(
                    (animation) -> {
                      // Do something.

                    });
    animationView
            .playAnimation();
    animationView.setVisibility(View.VISIBLE);



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
