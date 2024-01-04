package com.profitz.app.promos.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;

import java.util.concurrent.ThreadLocalRandom;

public class AdsFragment extends Fragment  {
    User user2;
    String username;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RewardedAd rewardedAd;
    boolean isLoading;
    RelativeLayout loader_rl;
    RelativeLayout cant_load_rl;
    Button runAd;
    Button loadAd;
    int IDuser;
    float random_bonus;
    RelativeLayout rl_global_ads;
    public AdsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_ads, container, false);

            user2 = MyPreferenceManager.getInstance(mContext).getUser();
            IDuser = Integer.parseInt(user2.getId());
            username = user2.getName();
            loader_rl = view.findViewById(R.id.loader_rl);
            cant_load_rl = view.findViewById(R.id.cant_load_rl);
            loadAd = view.findViewById(R.id.loadAd);
            rl_global_ads = view.findViewById(R.id.rl_global_ads);
            MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            runAd= view.findViewById(R.id.runAd);
            runAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  showAd();
                }
            });
            loadAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //loadAd();
                }
            });
           // loadAd();



            return view;

        }
/*
    private void loadAd(){
        this.rewardedAd = new RewardedAd(mContext, getString(R.string.rewarded_ad_unit_id));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback(){

            @Override
            public void onRewardedAdLoaded() {
                super.onRewardedAdLoaded();
                cant_load_rl.setVisibility(View.GONE);
                loader_rl.setVisibility(View.GONE);
                runAd.setVisibility(View.VISIBLE);
                Log.i("AdStatus", "Load Succesfull");
                runAd.setEnabled(true);
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
                loader_rl.setVisibility(View.GONE);
                cant_load_rl.setVisibility(View.VISIBLE);
                Log.i("AdStatus", "Falied to load ad");

            }
        };
        this.rewardedAd.loadAd(new AdRequest.Builder().build(),adLoadCallback);



    }
  */

    public static float generateRandomFloat(float min, float max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        float result = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
        if (result >= max) // correct for rounding
            result = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
        return result;
    }
    /*private void showAd(){
        if (this.rewardedAd.isLoaded())
        {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                    random_bonus =generateRandomFloat(0.01f,0.1f);
                    final String url = "https://yoururl.com/api/add_points_from_ads.php?safety_code=A4SAarK4aCasmfk2aDNA441a5sdaKASD401KA56DLARO2rJDAOSDLCNZ&user_id="+IDuser+"&bonus="+random_bonus;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    Snackbar snackbar = Snackbar.make(rl_global_ads, "Otrzymujesz "+random_bonus+" PUNKTÓW w ramach nagrody za obejrzenie reklamy!", Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error

                                }
                            });
                    Volley.newRequestQueue(mContext).add(jsonObjectRequest);
                    Log.i("AdStatus", "User rewarded");

                }

                @Override
                public void onRewardedAdOpened() {
                    super.onRewardedAdOpened();

                    Log.i("AdStatus", "Ad opened");

                }

                @Override
                public void onRewardedAdClosed() {
                    super.onRewardedAdClosed();
                    Log.i("AdStatus", "Ad closed");
                   // Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Nagroda nie zostanie przyznana. Reklama została zamknięta.", Snackbar.LENGTH_LONG);
                    //snackbar.show();
                    loadAd();

                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    super.onRewardedAdFailedToShow(adError);
                    Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas wyświetlenia reklamy. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    Log.i("AdStatus", "Falied to show");

                }
            };
            this.rewardedAd.show(getActivity(),adCallback);
        }
        else{
            Log.i("AdStatus", "Falied to load");

        }
    }
*/


}