package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.DoneActivity;
import com.profitz.app.promos.adapters.AdapterReview;

public class BottomSheetDialogDone extends SuperBottomSheetFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    private View bottomSheet;
    private RecyclerView mRVReviewList;
    private AdapterReview mAdapter;
    int promoId;
    String promoName;
    String promoBonus;
    String promoAdditionalInfo;
    String promoReceivedPoints;
    View v;
    LottieAnimationView animationView;
    String promoStatus;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle mArgs = getArguments();
        promoName = mArgs.getString("promoName");
        promoId=mArgs.getInt("promoId");
        promoStatus = mArgs.getString("promoStatus");
        promoBonus = mArgs.getString("promoBonus");
        promoAdditionalInfo = mArgs.getString("promoAdditionalInfo");
        promoReceivedPoints = mArgs.getString("promoReceivedPoints");

        if (promoStatus.equals("under_review"))
        {
            v = inflater.inflate(R.layout.fragment_single_done_under_review, container, false);
            animationView = v.findViewById(R.id.anim_in_review);
            TextView text_under_review_3 = v.findViewById(R.id.text_under_review_3);
            text_under_review_3.setText("Jeżeli weryfikacja będzie pozytywna - otrzymasz od nas bonus w postaci " + promoBonus + " punktów.");
            TextView buttonContact = v.findViewById(R.id.buttonContact);
            buttonContact.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @TargetApi(Build.VERSION_CODES.CUPCAKE)
                @Override
                public void onClick(View view) {
                   // ((DoneActivity)getActivity()).openChat();
                    dismiss();
                }
            });
        }
        else if (promoStatus.equals("accepted"))
        {
            v= inflater.inflate(R.layout.fragment_single_done_accepted, container, false);
            animationView = v.findViewById(R.id.anim_accepted);
            TextView buttonContact = v.findViewById(R.id.buttonContact);

            TextView accepted_text2 = v.findViewById(R.id.text_accepted_2);
            accepted_text2.setText("Dziękujemy za wykonanie tej promocji z naszego polecenia! Doceniamy naszych partnerów. Dlatego przyznaliśmy Ci aż " + promoBonus +" bonusowych punktów!");
            TextView text_accepted_5 = v.findViewById(R.id.text_accepted_5);
            text_accepted_5.setText("Jeżeli masz problemy z wypłatą zarobionej premii z aplikacji "+ promoName +", skontaktuj się z nami używając poniższego przycisku.");
            if (promoReceivedPoints.equals("no")){
            text_accepted_5.setText("Kliknij na poniższy przycisk aby odebrać swoje bonusowe punkty.");
            buttonContact.setVisibility(View.GONE);
            TextView buttonGetPoints = v.findViewById(R.id.buttonGetPoints);
            buttonGetPoints.setVisibility(View.VISIBLE);

            buttonGetPoints.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @TargetApi(Build.VERSION_CODES.CUPCAKE)
                @Override
                public void onClick(View view) {
                    ((DoneActivity)getActivity()).openDialogReview(promoName, promoId, promoBonus);
                    dismiss();
                }
            });

            }
            buttonContact.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @TargetApi(Build.VERSION_CODES.CUPCAKE)
                @Override
                public void onClick(View view) {
                    //((DoneActivity)getActivity()).openChat();
                    dismiss();
                }
            });
        }
        else if (promoStatus.equals("declined"))
        {
            v = inflater.inflate(R.layout.fragment_single_done_declined, container, false);
            animationView = v.findViewById(R.id.anim_declined);
            TextView accepted_text2 = v.findViewById(R.id.text_declined_3);
            accepted_text2.setText("Powód: "+promoAdditionalInfo);
            TextView buttonContact = v.findViewById(R.id.buttonContact);
            buttonContact.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @TargetApi(Build.VERSION_CODES.CUPCAKE)
                @Override
                public void onClick(View view) {
                    //((DoneActivity)getActivity()).openChat();
                    dismiss();
                }
            });
        }
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();


        TextView promoNameTextView = (TextView) v.findViewById(R.id.textNamePromo);
        promoNameTextView.setText(promoName);

      //  new AsyncLogin().execute();
        //  dismiss(); - zamyka modal

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
