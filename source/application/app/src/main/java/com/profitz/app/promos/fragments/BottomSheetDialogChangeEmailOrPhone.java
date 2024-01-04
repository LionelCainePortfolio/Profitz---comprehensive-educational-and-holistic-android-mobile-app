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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class BottomSheetDialogChangeEmailOrPhone extends BottomSheetDialogFragment {
  private BottomSheetListener mListener;
  private Context mContext;
  public static final int CONNECTION_TIMEOUT = 10000;
  public static final int READ_TIMEOUT = 15000;
  User user2;
  String source;
  View v;
  Bundle mArgs;
  CountryCodePicker countryCodePicker;
  String phone_number;
  TextInputEditText phone_number_edit;
  TextInputEditText email_edit;
  ConstraintLayout const_update_phone;
  ConstraintLayout const_update_email;
  TextInputLayout input_layout_number_phone;
  TextInputLayout input_layout_email;
  TextView update_email_or_phone;
  FirebaseAuth mAuth;
  String mVerificationId;
  TextView text1_const_update_phone;
  TextView text5_const_update_phone;
  TextView text3_const_update_phone;
  ProgressBar pBarSms_const_update_phone;
  OtpTextView otpTextView_const_update_phone;
  TextView text1_const_update_email;
  TextView text5_const_update_email;
  TextView text3_const_update_email;
  ProgressBar pBarSms_const_update_email;
  OtpTextView otpTextView_const_update_email;

  PhoneAuthOptions options;
  PhoneAuthProvider.ForceResendingToken mResendToken;
  private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
  boolean unlocked_resend;
  int resend;
  int oneMin;
  int twoMin;
  int fiveMin;
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

    v = inflater.inflate(R.layout.dialog_change_email_or_phone, container, false);

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
    email_edit = v.findViewById(R.id.email_edit);
    countryCodePicker = v.findViewById(R.id.editNumberCode);
    phone_number_edit = v.findViewById(R.id.phone_number_edit);
    //phone_number_edit = view.findViewById(R.id.editNumber);

    const_update_phone = v.findViewById(R.id.const_update_phone);
    const_update_email = v.findViewById(R.id.const_update_email);

    input_layout_number_phone = v.findViewById(R.id.input_layout_number_phone);
    input_layout_email = v.findViewById(R.id.input_layout_email);
    countryCodePicker.registerCarrierNumberEditText(phone_number_edit);

    text1_const_update_phone = v.findViewById(R.id.text1_const_update_phone);
    text5_const_update_phone = v.findViewById(R.id.text5_const_update_phone);
    text3_const_update_phone = v.findViewById(R.id.text3_const_update_phone);
    pBarSms_const_update_phone = v.findViewById(R.id.pBarSms_const_update_phone);
    otpTextView_const_update_phone = v.findViewById(R.id.otp_view_const_update_phone);

    text1_const_update_email = v.findViewById(R.id.text1_const_update_email);
    text5_const_update_email = v.findViewById(R.id.text5_const_update_email);
    text3_const_update_email = v.findViewById(R.id.text3_const_update_email);

    mArgs = getArguments();
    if (mArgs != null) {
      source = mArgs.getString("source");
    }
    getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.bg_Profitz));
    user2 = MyPreferenceManager.getInstance(mContext).getUser();

    if (source !=null && source.equals("editEmail")){
      String actual_email = user2.getEmail();
      email_edit.setText(actual_email);
      input_layout_number_phone.setVisibility(View.GONE);
      input_layout_email.setVisibility(View.VISIBLE);
    }
    else if (source !=null && source.equals("editPhone")){
      String actual_phone = user2.getPhone().substring(3);
      phone_number_edit.setText(actual_phone);
      input_layout_email.setVisibility(View.GONE);
      input_layout_number_phone.setVisibility(View.VISIBLE);

    }

    //promoId = PromoDetailActivity.getId();


    update_email_or_phone = v.findViewById(R.id.update_email_or_phone);
    update_email_or_phone.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        if (source !=null && source.equals("editEmail")) {
          final String edit = email_edit.getText().toString();

          if (Utility.isEmpty(Utility.getStringValue(email_edit))) {
          //  Toast.makeText(mContext, "Wprowadź adres email.", Toast.LENGTH_LONG).show();
            Toasty.error(mContext, "Wprowadź adres email.").show();

          }
          else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit).matches()) {
         //   Toast.makeText(mContext, "Wprowadź poprawny adres email.", Toast.LENGTH_LONG).show();
            Toasty.error(mContext, "Wprowadź poprawny adres email.").show();

          }
          else{
            final String url = "https://yoururl.com/api/check_email.php?email="+edit;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                      @Override
                      public void onResponse(JSONObject response) {
                        try {
                          String email_valid = response.getString("email_valid");
                          // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                          if (email_valid.equals("1"))
                          {
                           // Toast.makeText(mContext, "Wprowadzony adres email istnieje już w bazie danych.", Toast.LENGTH_LONG).show();
                            Toasty.error(mContext, "Wprowadzony adres email istnieje już w bazie danych.").show();

                          }
                          else if (email_valid.equals("0")){
                            update_email_or_phone.setVisibility(View.GONE);
                            input_layout_email.setVisibility(View.GONE);
                            const_update_email.setVisibility(View.VISIBLE);


                            String email_edit_text = edit;
                            text1_const_update_email.setText("Wysłaliśmy kod na adres email "+ email_edit_text);







                          }

                        } catch (JSONException e) {
                          e.printStackTrace();
                        }

                      }
                    }, new Response.ErrorListener() {

                      @Override
                      public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                      }
                    });
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);
          }
               }
              else if (source !=null && source.equals("editPhone")){
             if (countryCodePicker.isValidFullNumber()){
            phone_number = countryCodePicker.getFullNumberWithPlus();

            //Get complete phone number
            String _getUserEnteredPhoneNumber = phone_number_edit.getText().toString().trim();
            if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
     //         Toast.makeText(mContext, "Wprowadzony numer telefonu jest nieprawidłowy.", Toast.LENGTH_LONG).show();
              Toasty.error(mContext, "Wprowadzony numer telefonu jest nieprawidłowy.").show();

            }
            else{
              final String _phoneNo = countryCodePicker.getFullNumberWithPlus();
              String number_string_text = countryCodePicker.getFormattedFullNumber();
              String number_string_text_wo_spacebars = number_string_text.replaceAll("\\s+","");

              final String edit = phone_number_edit.getText().toString();

              final String url = "https://yoururl.com/api/check_phone.php?phone="+number_string_text_wo_spacebars;
              JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                      (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                          try {
                            String phone_valid = response.getString("phone_valid");
                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                            if (phone_valid.equals("1"))
                            {
                          //    Toast.makeText(mContext, "Wprowadzony numer telefonu istnieje już w bazie danych.", Toast.LENGTH_LONG).show();
                              Toasty.error(mContext, "Wprowadzony numer telefonu istnieje już w bazie danych.").show();

                            }
                            else if (phone_valid.equals("0")){
                              update_email_or_phone.setVisibility(View.GONE);
                              input_layout_number_phone.setVisibility(View.GONE);
                              const_update_phone.setVisibility(View.VISIBLE);

                              mAuth = FirebaseAuth.getInstance();
                              String phone_number = _phoneNo;
                              String phone_number_text = edit;


                              text1_const_update_phone.setText("Wysłaliśmy kod sms na numer "+ phone_number_text);



                              if (getArguments() != null) {
                                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                  @Override
                                  public void onVerificationCompleted(PhoneAuthCredential credential) {
                                    // This callback will be invoked in two situations:
                                    // 1 - Instant verification. In some cases the phone number can be instantly
                                    //     verified without needing to send or enter a verification code.
                                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                    //     detect the incoming verification SMS and perform verification without
                                    //     user action.
                                    Log.d("OTP", "onVerificationCompleted:" + credential);

                                    String code = credential.getSmsCode();
                                    if (code != null){
                                      otpTextView_const_update_phone.setOTP(code);

                                         //start of updateotptextview
                                    if (mVerificationId != null){
                                      PhoneAuthCredential credential2 = PhoneAuthProvider.getCredential(mVerificationId, code);
                                      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                                      firebaseAuth.signInWithCredential(credential2)
                                              .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                  if (task.isSuccessful()) {
                                                   // editListener.onEditPressed6(mVerificationId);

                                                    otpTextView_const_update_phone.showSuccess();
                                                    final Handler handler = new Handler(Looper.getMainLooper());
                                                    handler.postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                        //phoneVerified();

                                                        dismiss();
                                                        Toasty.success(mContext, "Numer telefonu został zaaktualizowany.").show();


                                                      }
                                                    }, 500);


                                                  } else {
                                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                   //   editListener.onEditPressed6("wrong_code");

                                                      otpTextView_const_update_phone.showError();
                                                      otpTextView_const_update_phone.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.vibrate));

                                                      final Handler handler = new Handler(Looper.getMainLooper());
                                                      handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                          otpTextView_const_update_phone.resetState();
                                                        }
                                                      }, 500);
                                                    }
                                                  }
                                                }
                                              });
                                    }
                                    else{
                                    //  editListener.onEditPressed6("wrong_code");

                                      otpTextView_const_update_phone.showError();
                                      otpTextView_const_update_phone.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.vibrate));
                                      final Handler handler = new Handler(Looper.getMainLooper());
                                      handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                          otpTextView_const_update_phone.setOTP("");
                                          otpTextView_const_update_phone.resetState();
                                        }
                                      }, 500);
                                    }
                                    //end of updateotptextview



                                    }
                                  }

                                  @Override
                                  public void onVerificationFailed(FirebaseException e) {
                                    // This callback is invoked in an invalid request for verification is made,
                                    // for instance if the the phone number format is not valid.
                                    Log.w("OTP", "onVerificationFailed", e);
                                    //editListener.onEditPressed6("wrong_code");
                               //     Toast.makeText(mContext, "Wystąpił błąd. Spróbuj ponownie.", Toast.LENGTH_LONG).show();
                                    Toasty.error(mContext, "Wystąpił błąd. Spróbuj ponownie.").show();



                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                      // Invalid request
                                    } else if (e instanceof FirebaseTooManyRequestsException) {
                                      // The SMS quota for the project has been exceeded
                                    }

                                    // Show a message and update the UI
                                  }

                                  @Override
                                  public void onCodeSent(@NonNull String verificationId,
                                                         @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                    // The SMS verification code has been sent to the provided phone number, we
                                    // now need to ask the user to enter the code and then construct a credential
                                    // by combining the code with a verification ID.
                                    Log.d("OTP", "onCodeSent:" + verificationId);

                                    // Save verification ID and resending token so we can use them later
                                    mVerificationId = verificationId;
                                    mResendToken = token;
                                    unlocked_resend = false;
                                  }
                                };


                                options =
                                        PhoneAuthOptions.newBuilder(mAuth)
                                                .setPhoneNumber(phone_number)       // Phone number to verify
                                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                .setActivity(getActivity())              // Activity (for callback binding)
                                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                .build();
                                PhoneAuthProvider.verifyPhoneNumber(options);


                                resend = 0;



                                pBarSms_const_update_phone.setRotation(180);

                                oneMin = 60 * 1000; // 1 minute in milli seconds
                                twoMin = 120 * 1000; // 1 minute in milli seconds
                                fiveMin = 300 * 1000; // 1 minute in milli seconds




                                otpTextView_const_update_phone.setOtpListener(new OTPListener() {
                                  @Override
                                  public void onInteractionListener() {
                                    // fired when user types something in the Otpbox
                                  }
                                  @Override
                                  public void onOTPComplete(String otp) {
                                    // fired when user has entered the OTP fully.
                                    //start of updateotptextview
                                    if (mVerificationId != null){
                                      PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                                      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                                      firebaseAuth.signInWithCredential(credential)
                                              .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                  if (task.isSuccessful()) {
                                                   // editListener.onEditPressed6(mVerificationId);

                                                    otpTextView_const_update_phone.showSuccess();
                                                    final Handler handler = new Handler(Looper.getMainLooper());
                                                    handler.postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                       // phoneVerified();



                                                      }
                                                    }, 500);


                                                  } else {
                                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                   //   editListener.onEditPressed6("wrong_code");

                                                      otpTextView_const_update_phone.showError();
                                                      otpTextView_const_update_phone.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.vibrate));

                                                      final Handler handler = new Handler(Looper.getMainLooper());
                                                      handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                          otpTextView_const_update_phone.resetState();
                                                        }
                                                      }, 500);
                                                    }
                                                  }
                                                }
                                              });
                                    }
                                    else{
                                    //  editListener.onEditPressed6("wrong_code");

                                      otpTextView_const_update_phone.showError();
                                      otpTextView_const_update_phone.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.vibrate));
                                      final Handler handler = new Handler(Looper.getMainLooper());
                                      handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                          otpTextView_const_update_phone.setOTP("");
                                          otpTextView_const_update_phone.resetState();
                                        }
                                      }, 500);
                                    }
                                    //end of updateotptextview


                                  }
                                });


                              }
                              text5_const_update_phone.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v){
                                  if (unlocked_resend){
                                    text3_const_update_phone.setVisibility(View.VISIBLE);
                                    pBarSms_const_update_phone.setVisibility(View.VISIBLE);
                                    text5_const_update_phone.setTextColor(Color.parseColor("#D5D5D5"));
                                    PhoneAuthProvider.verifyPhoneNumber(options);
                                    if (resend == 0){
                                      unlocked_resend = false;
                                      new CountDownTimer(twoMin, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                          text3_const_update_phone.setText("Możesz poprosić o ponowne wysłanie kodu za " + millisUntilFinished / 1000 +" sekund");
                                          long finishedSeconds = twoMin - millisUntilFinished;
                                          text5_const_update_phone.setTextColor(Color.parseColor("#D5D5D5"));
                                          int total = (int) (((float)finishedSeconds / (float)twoMin) * 100.0);
                                          pBarSms_const_update_phone.setProgress(total, true);

                                        }

                                        public void onFinish() {
                                          unlocked_resend = true;
                                          text3_const_update_phone.setVisibility(View.GONE);
                                          pBarSms_const_update_phone.setVisibility(View.GONE);
                                          text5_const_update_phone.setTextColor(Color.parseColor("#1E1E1E"));
                                          resend = 1;
                                        }

                                      }.start();
                                    }
                                    else if (resend == 1){
                                      unlocked_resend = false;

                                      new CountDownTimer(fiveMin, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                          text3_const_update_phone.setText("Możesz poprosić o ponowne wysłanie kodu za " + millisUntilFinished / 1000 +" sekund");
                                          long finishedSeconds = fiveMin - millisUntilFinished;
                                          text5_const_update_phone.setTextColor(Color.parseColor("#D5D5D5"));

                                          int total = (int) (((float)finishedSeconds / (float)fiveMin) * 100.0);
                                          pBarSms_const_update_phone.setProgress(total, true);
                                        }

                                        public void onFinish() {
                                          unlocked_resend = true;
                                          text3_const_update_phone.setVisibility(View.GONE);
                                          pBarSms_const_update_phone.setVisibility(View.GONE);
                                          text5_const_update_phone.setTextColor(Color.parseColor("#1E1E1E"));
                                          resend = 1;

                                        }

                                      }.start();
                                    }

                                  }

                                }
                              });

                              new CountDownTimer(oneMin, 1000) {

                                public void onTick(long millisUntilFinished) {
                                  text3_const_update_phone.setText("Możesz poprosić o ponowne wysłanie kodu za " + millisUntilFinished / 1000 +" sekund");
                                  long finishedSeconds = oneMin - millisUntilFinished;
                                  text5_const_update_phone.setTextColor(Color.parseColor("#D5D5D5"));
                                  int total = (int) (((float)finishedSeconds / (float)oneMin) * 100.0);
                                  pBarSms_const_update_phone.setProgress(total, true);
                                }

                                public void onFinish() {
                                  unlocked_resend = true;
                                  text3_const_update_phone.setVisibility(View.GONE);
                                  pBarSms_const_update_phone.setVisibility(View.GONE);
                                  text5_const_update_phone.setTextColor(Color.parseColor("#1E1E1E"));
                                  resend = 0;

                                }

                              }.start();

                            }
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }

                        }
                      }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                          // TODO: Handle error

                        }
                      });
              Volley.newRequestQueue(mContext).add(jsonObjectRequest);


            }
                }
             else {
               //  Toast.makeText(mContext, "Wprowadź numer telefonu.", Toast.LENGTH_LONG).show();
               Toasty.error(mContext, "Wprowadź poprawny numer telefonu.").show();
             }
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
