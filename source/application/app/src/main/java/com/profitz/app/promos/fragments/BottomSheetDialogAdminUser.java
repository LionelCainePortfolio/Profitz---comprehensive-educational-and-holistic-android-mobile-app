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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BottomSheetDialogAdminUser extends SuperBottomSheetFragment {
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
    int peopleUserId;
    String peopleUserName;
    String peopleUserNickname;
    int peopleUserLevel;
    double peopleUserPoints;
    double peopleUserTotalEarn;
    int peopleUserDones;
    int peopleUserFavorites;
    int peopleUserComments;
    int peopleUserReviews;
    int peopleUserLicences;
    int peopleUserBoughts;
    int peopleUserRefferals;
    int peopleUserActualDay;
    String peopleUserImageUrl;
    String peopleUserRegisterDate;
    String peopleUserLastLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle mArgs = getArguments();
        peopleUserId = mArgs.getInt("peopleUserId");
        peopleUserName = mArgs.getString("peopleUserName");
        peopleUserNickname = mArgs.getString("peopleUserNickname");
        peopleUserLevel = mArgs.getInt("peopleUserLevel");
        peopleUserPoints = mArgs.getDouble("peopleUserPoints");
        peopleUserTotalEarn = mArgs.getDouble("peopleUserTotalEarn");
        peopleUserDones = mArgs.getInt("peopleUserDones");
        peopleUserFavorites = mArgs.getInt("peopleUserFavorites");
        peopleUserComments = mArgs.getInt("peopleUserComments");
        peopleUserReviews = mArgs.getInt("peopleUserReviews");
        peopleUserLicences = mArgs.getInt("peopleUserLicences");
        peopleUserBoughts = mArgs.getInt("peopleUserBoughts");
        peopleUserRefferals = mArgs.getInt("peopleUserRefferals");
        peopleUserActualDay = mArgs.getInt("peopleUserActualDay");
        peopleUserImageUrl = mArgs.getString("peopleUserImageUrl");
        peopleUserRegisterDate = mArgs.getString("peopleUserRegisterDate");
        peopleUserLastLogin = mArgs.getString("peopleUserLastLogin");

        v= inflater.inflate(R.layout.dialog_admin_single_one_users, container, false);

        String my_date = peopleUserRegisterDate;

        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date datee = format.parse(my_date);
            //myHolder.textDateN.setText(getCountdownText(mContext, datee));

        } catch (ParseException e) {
            e.printStackTrace();
        }

            animationView = v.findViewById(R.id.anim_accepted);
            TextView accepted_text2 = v.findViewById(R.id.text_accepted_2);
            accepted_text2.setText("Dziękujemy za wykonanie tej promocji z naszego polecenia! Doceniamy naszych partnerów. Dlatego przyznaliśmy Ci aż " + promoBonus +" bonusowych punktów!");
            TextView text_accepted_5 = v.findViewById(R.id.text_accepted_5);
            text_accepted_5.setText("Jeżeli masz problemy z wypłatą zarobionej premii z aplikacji "+ promoName +", skontaktuj się z nami używając poniższego przycisku.");
            if (promoReceivedPoints.equals("no")){
            text_accepted_5.setText("Kliknij na poniższy przycisk aby odebrać swoje bonusowe punkty.");
            TextView buttonContact = v.findViewById(R.id.buttonContact);
            buttonContact.setVisibility(View.GONE);
            Button buttonGetPoints = v.findViewById(R.id.buttonGetPoints);
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

        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();


        TextView promoNameTextView = (TextView) v.findViewById(R.id.textNamePromo);
        promoNameTextView.setText(promoName);

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
