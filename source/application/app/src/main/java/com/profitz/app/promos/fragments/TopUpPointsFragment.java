package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.profitz.app.R;import com.profitz.app.promos.PaypalConfig;
import com.profitz.app.promos.activities.HotPay;
import com.profitz.app.promos.activities.PointsActivity;
import com.profitz.app.promos.activities.SuccessActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

@SuppressLint("ValidFragment")
public class TopUpPointsFragment extends Fragment {
    View view;
    private String paymentAmount;
    int paymentMethod = 0;
    int paymentPoints = 10;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private Context mContext;
    LottieAnimationView animationView;

    //Paypal Configuration Object
    private static final PayPalConfiguration config = new PayPalConfiguration() // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID)
            .merchantName("Profitz")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"))
            ;  // or live (ENVIRONMENT_PRODUCTION)



    boolean clicked_first = false;
    boolean clicked_second = false;
    boolean clicked_third = false;
    boolean clicked_fourth = false;
    boolean clicked_fifth = false;
    boolean clicked_sixth = false;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_topuppoints, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setStatusBarColor(Color.gold_gradient_shape);
        //Drawable background = MyInformationsActivity.getResources().getDrawable(R.drawable.gold_gradient_shape);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(mContext, R.color.transparent));
        // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.grey);

        //textView1 = view.findViewById(R.id.text1_step1);
        //textView2 = view.findViewById(R.id.text2_step1);
        //imageView1 = view.findViewById(R.id.img_step1);
        //editText1 = view.findViewById(R.id.editTextName);
        RelativeLayout Payment_methodsButton = view.findViewById(R.id.payment_method_rl);
        ImageView img_payment_method = view.findViewById(R.id.payment_method_view);
        TextView textViewToChange = view.findViewById(R.id.pay_with_change);

        ImageView imgBackArrow = view.findViewById(R.id.imgBackArrow);
        RelativeLayout relativeButton1 = view.findViewById(R.id.topup_rl1);
        RelativeLayout relativeButton2 = view.findViewById(R.id.topup_rl2);
        RelativeLayout relativeButton3 = view.findViewById(R.id.topup_rl3);
        RelativeLayout relativeButton4 = view.findViewById(R.id.topup_rl4);
        RelativeLayout relativeButton5 = view.findViewById(R.id.topup_rl5);
        RelativeLayout relativeButton6 = view.findViewById(R.id.topup_rl6);
        //Getting the amount from Radio Button
        relativeButton1.setBackgroundResource(R.drawable.radio_checked);
        paymentAmount = "1";

        Button continue_button = view.findViewById(R.id.continue_button);

        imgBackArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_new = new Intent(getActivity(), PointsActivity.class);
                startActivity(intent_new);

            }
        });


        relativeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_first) {
                    clicked_first = false;
                } else {
                    paymentAmount = "1.69"; // 1 eur + 0,17 (17% podatek) + 0,23 (23% podatek VAT)= 1,40 + 0,29 dodatkowo za ilość
                    paymentPoints = 10;
                    clicked_first = true;
                    relativeButton1.setBackgroundResource(R.drawable.radio_checked);
                    relativeButton2.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton3.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton4.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton5.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton6.setBackgroundResource(R.drawable.radio_unchecked);
                }

            }
        });
        relativeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_second) {
                    clicked_second = false;
                } else {
                    paymentAmount = "4,49"; // 3 eur + 0,51 (17% podatek) + 0,69 (23% podatek VAT)= 4,20 + 0,29 dodatkowo za ilość
                    paymentPoints = 30;
                    clicked_second = true;
                    relativeButton1.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton3.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton4.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton5.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton6.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton2.setBackgroundResource(R.drawable.radio_checked);
                }

            }
        });
        relativeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_third) {
                    clicked_third = false;

                } else {
                    paymentAmount = "7,19"; // 5 eur + 0,85 (17% podatek) + 1,15 (23% podatek VAT)= 7 + 0,19 dodatkowo za ilość
                    paymentPoints = 50;
                    clicked_third = true;
                    relativeButton3.setBackgroundResource(R.drawable.radio_checked);
                    relativeButton1.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton2.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton5.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton6.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton4.setBackgroundResource(R.drawable.radio_unchecked);
                }

            }
        });
        relativeButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_fourth) {
                    clicked_fourth = false;
                } else {
                    paymentAmount = "9.69"; // 7 eur + 1,19 (17% podatek) + 1,69 (23% podatek VAT)= 9.88 - 0.19 dodatkowo za ilość
                    paymentPoints = 70;
                    clicked_fourth = true;
                    relativeButton1.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton2.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton3.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton5.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton6.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton4.setBackgroundResource(R.drawable.radio_checked);
                }

            }
        });
        relativeButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_fifth) {
                    clicked_fifth = false;
                } else {
                    paymentAmount = "12.79"; // 10 eur + 1,7 (17% podatek) + 2,3 (23% podatek VAT)= 14 - 1.21 dodatkowo za ilość
                    paymentPoints = 100;
                    clicked_fifth = true;
                    relativeButton1.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton2.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton3.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton4.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton6.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton5.setBackgroundResource(R.drawable.radio_checked);
                }

            }
        });
        relativeButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked_sixth) {
                    clicked_sixth = false;
                } else {
                    paymentAmount = "17.49"; // 15 eur + 2,55 (17% podatek) + 3,45 (23% podatek VAT)= 21 - 3.51 dodatkowo za ilość
                    paymentPoints = 150;
                    clicked_sixth = true;
                    relativeButton1.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton2.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton3.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton4.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton5.setBackgroundResource(R.drawable.radio_unchecked);
                    relativeButton6.setBackgroundResource(R.drawable.radio_checked);
                }

            }
        });
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        builder.setTitle("Wybierz sposób płatności");

// add a list
        String[] pay_methods = {"PayPal", "HotPay"};
        builder.setItems(pay_methods, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        paymentMethod = 0;
                        textViewToChange.setText("PayPal");
                        textViewToChange.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17); //dip or sp etc as you need
                        img_payment_method.setImageResource(R.drawable.paypal);
                        break;
                    case 1:
                        paymentMethod = 1;
                        textViewToChange.setText("HotPay");
                        textViewToChange.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15); //dip or sp etc as you need
                        img_payment_method.setImageResource(R.drawable.hotpay);

                        break;
                }
            }
        });
        continue_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   if (paymentMethod == 0){
                    getPayment();
                }*/
           if (paymentMethod == 1) {
               Intent in = new Intent(getActivity(), HotPay.class);
               in.putExtra("SEKRET", "U05Bc0oyNXAvKzBOK3BUV1VORTdYbjVOZ3BPYjh1Y3FtQXVhSm1HUVZaQT0,");
               in.putExtra("KWOTA", paymentAmount);
               in.putExtra("NAZWA_USLUGI", paymentPoints+" punktów w Profitz");
               in.putExtra("ADRES_WWW", "https://profitz.app");
               in.putExtra("ID_ZAMOWIENIA","1");
               startActivity(in);
           }

/*
               final Dialog dialog3 = new Dialog(mContext); // Context, this, etc.
                dialog3.setContentView(R.layout.dialog_disable_future);
                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Button click3 = (Button) dialog3.findViewById(R.id.button_understand_disable_future);
                click3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                animationView = dialog3.findViewById(R.id.anim_declined);

                animationView.setVisibility(View.VISIBLE);

                animationView
                        .addAnimatorUpdateListener(
                                (animation) -> {
                                    // Do something.

                                });
                animationView
                        .playAnimation();
                animationView.setVisibility(View.VISIBLE);

                dialog3.show();

            } });
// create and show the alert dialog


        return view;

              */
            }
        });
        AlertDialog dialog = builder.create();

        Payment_methodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void getPayment() {

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new
                BigDecimal(paymentAmount),"EUR","Zakup punktów: "+paymentPoints+" sztuk",PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //invoice number
        payment.invoiceNumber("321");

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                Log.d("CONFIRM", String.valueOf(confirm));
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.d("paymentExample", paymentDetails);
                        Log.i("paymentExample", paymentDetails);
                        Log.d("Pay Confirm : ", String.valueOf(confirm.getPayment().toJSONObject()));
//                        Starting a new activity for the payment details and status to show

                        startActivity(new Intent(getActivity(), SuccessActivity.class).putExtra("PaymentDetails", paymentDetails));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred : ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }
    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

}
