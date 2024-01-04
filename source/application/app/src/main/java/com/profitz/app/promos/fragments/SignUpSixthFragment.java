package com.profitz.app.promos.fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;
import com.profitz.app.promos.activities.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;


public class SignUpSixthFragment extends Fragment {
    Context context;
    OnEditTextChanged editListener;
    CountryCodePicker countryCodePicker;
    String phone_number;
    EditText phone_number_edit;
    String mParam1;
    String mParam12;
    OtpTextView otpTextView;
    SignUpActivity activity;
    private FirebaseAuth mAuth;
    String mVerificationId;
    TextView text5_step6;
    ProgressBar pBarSms;
    TextView text7_step6;
    boolean unlocked_resend;
    int resend;
    int oneMin;
    int twoMin;
    int fiveMin;

    PhoneAuthOptions options;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step6, container, false);
        context = inflater.getContext();
        mAuth = FirebaseAuth.getInstance();

        Bundle arguments = getArguments();
        activity = (SignUpActivity) getActivity();
        String phone_number = activity.getPhoneNumberString();

        String phone_number_text = activity.getPhoneNumberStringFormatted();

        TextView text3_step6 = view.findViewById(R.id.text3_step6);
        text3_step6.setText("Wysłaliśmy kod sms na numer "+ phone_number_text);

        otpTextView = view.findViewById(R.id.otp_view);
        editListener.onEditPressed6("wrong_code");
      /*  if (getArguments() != null) {
            mParam1 = getArguments().getString("step6");
            if (mParam1.equals("wrong_code")) {
                otpTextView.showError();
                otpTextView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.vibrate));

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        otpTextView.resetState();
                    }
                }, 500);
            }
        }

       */
        //phone_number_edit = view.findViewById(R.id.editNumber);
      //  editListener.onEditPressed6(phone_number);
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
                        otpTextView.setOTP(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    Log.w("OTP", "onVerificationFailed", e);
                    editListener.onEditPressed6("wrong_code");

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
            text7_step6 = view.findViewById(R.id.text7_step6);
            text5_step6 = view.findViewById(R.id.text5_step6);
            pBarSms = view.findViewById(R.id.pBarSms);
            pBarSms.setRotation(180);

            oneMin = 60 * 1000; // 1 minute in milli seconds
            twoMin = 120 * 1000; // 1 minute in milli seconds
            fiveMin = 300 * 1000; // 1 minute in milli seconds




            otpTextView.setOtpListener(new OTPListener() {
                @Override
                public void onInteractionListener() {
                    // fired when user types something in the Otpbox
                }
                @Override
                public void onOTPComplete(String otp) {
                    // fired when user has entered the OTP fully.
                    verifyCode(otp);
                }
            });


        }
        text7_step6.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (unlocked_resend){
                    text5_step6.setVisibility(View.VISIBLE);
                    pBarSms.setVisibility(View.VISIBLE);
                    text7_step6.setTextColor(Color.parseColor("#D5D5D5"));
                    PhoneAuthProvider.verifyPhoneNumber(options);
                    if (resend == 0){
                        unlocked_resend = false;
                        new CountDownTimer(twoMin, 1000) {

                            public void onTick(long millisUntilFinished) {
                                text5_step6.setText("Możesz poprosić o ponowne wysłanie kodu za " + millisUntilFinished / 1000 +" sekund");
                                long finishedSeconds = twoMin - millisUntilFinished;
                                text7_step6.setTextColor(Color.parseColor("#D5D5D5"));
                                int total = (int) (((float)finishedSeconds / (float)twoMin) * 100.0);
                                pBarSms.setProgress(total, true);

                            }

                            public void onFinish() {
                                unlocked_resend = true;
                                text5_step6.setVisibility(View.GONE);
                                pBarSms.setVisibility(View.GONE);
                                text7_step6.setTextColor(Color.parseColor("#1E1E1E"));
                                resend = 1;
                            }

                        }.start();
                    }
                    else if (resend == 1){
                        unlocked_resend = false;

                        new CountDownTimer(fiveMin, 1000) {
                            public void onTick(long millisUntilFinished) {
                                text5_step6.setText("Możesz poprosić o ponowne wysłanie kodu za " + millisUntilFinished / 1000 +" sekund");
                                long finishedSeconds = fiveMin - millisUntilFinished;
                                text7_step6.setTextColor(Color.parseColor("#D5D5D5"));

                                int total = (int) (((float)finishedSeconds / (float)fiveMin) * 100.0);
                                pBarSms.setProgress(total, true);
                            }

                            public void onFinish() {
                                unlocked_resend = true;
                                text5_step6.setVisibility(View.GONE);
                                pBarSms.setVisibility(View.GONE);
                                text7_step6.setTextColor(Color.parseColor("#1E1E1E"));
                                resend = 1;

                            }

                        }.start();
                    }

                }

            }
        });

        new CountDownTimer(oneMin, 1000) {

            public void onTick(long millisUntilFinished) {
                text5_step6.setText("Możesz poprosić o ponowne wysłanie kodu za " + millisUntilFinished / 1000 +" sekund");
                long finishedSeconds = oneMin - millisUntilFinished;
                text7_step6.setTextColor(Color.parseColor("#D5D5D5"));
                int total = (int) (((float)finishedSeconds / (float)oneMin) * 100.0);
                pBarSms.setProgress(total, true);
            }

            public void onFinish() {
                unlocked_resend = true;
                text5_step6.setVisibility(View.GONE);
                pBarSms.setVisibility(View.GONE);
                text7_step6.setTextColor(Color.parseColor("#1E1E1E"));
                resend = 0;

            }

        }.start();

       /* countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

            }
        }); */

        return view;


    }




    private void verifyCode(String code) {

        if (mVerificationId != null){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

           firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            editListener.onEditPressed6(mVerificationId);

                            otpTextView.showSuccess();
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    activity.phoneVerified();
                                }
                            }, 500);


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                editListener.onEditPressed6("wrong_code");

                                otpTextView.showError();
                                otpTextView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.vibrate));

                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        otpTextView.resetState();
                                    }
                                }, 500);
                            }
                        }
                    }
                });
                 }
        else{
            editListener.onEditPressed6("wrong_code");

            otpTextView.showError();
            otpTextView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.vibrate));
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    otpTextView.setOTP("");
                    otpTextView.resetState();
                }
            }, 500);
        }


        }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    String data;
    public void setData(String yourData){
        data = yourData;
        //phone_number_edit.setError("Wprowadzony numer jest nieprawidłowy.");
        //  phone_number_edit.requestFocus();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            editListener=(OnEditTextChanged) getActivity();
        }catch(ClassCastException e){
            throw new ClassCastException(activity +"must implemnt onEditPressed");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}