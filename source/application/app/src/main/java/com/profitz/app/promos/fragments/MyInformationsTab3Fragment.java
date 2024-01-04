package com.profitz.app.promos.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.profitz.app.R;public class MyInformationsTab3Fragment extends Fragment {
    String username;
    private Context mContext;
    Button buttonRefer;
    public MyInformationsTab3Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_informations_tab3_fragment_three, container, false);
      //  view.findViewById(R.id.my_rounded_sign_in_button).setOnClickListener(mListener);

        LottieAnimationView animationView
                = view.findViewById(R.id.anim_myinfo_working_on_it);
        animationView.setVisibility(View.VISIBLE);

        animationView
                .addAnimatorUpdateListener(
                        (animation) -> {
                            // Do something.

                        });
        animationView
                .playAnimation();
        return view;
    }
    private final View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (view.getId() == R.id.my_rounded_sign_in_button) {//    Intent intent = new Intent(getActivity(), ReferFriendActivity.class);
                //  intent.putExtra("openReferFriend", 1); //for example
                // startActivity(intent);
            }
        }
    };
}