package com.profitz.app.promos.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.google.android.gms.ads.rewarded.RewardedAd;


public class MarketFragment extends Fragment  {
    User user2;
    String username;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RewardedAd rewardedGame;
    boolean isLoading;
    RelativeLayout loader_rl;
    RelativeLayout cant_load_rl;
    Button runGame;
    Button LetsStart;
    int IDuser;
    float random_bonus;
    Dialog dialog3;
    RelativeLayout rl_global_games;
    public MarketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_market, container, false);


            user2 = MyPreferenceManager.getInstance(mContext).getUser();
            IDuser = Integer.parseInt(user2.getId());
            username = user2.getName();

            dialog3 = new Dialog(mContext); // Context, this, etc.
            dialog3.setContentView(R.layout.dialog_disable_future);
            dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            CoordinatorLayout all_coord;
            all_coord = view.findViewById(R.id.all_coord);


            return view;

        }




}