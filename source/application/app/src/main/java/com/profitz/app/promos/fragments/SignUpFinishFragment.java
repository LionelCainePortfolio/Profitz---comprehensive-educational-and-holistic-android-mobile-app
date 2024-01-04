package com.profitz.app.promos.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class SignUpFinishFragment extends Fragment {

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step_finish, container, false);
        mContext = inflater.getContext();

        final KonfettiView konfettiView = view.findViewById(R.id.konfettiView);
        konfettiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
            /*    konfettiView.build()
                        .addColors(ContextCompat.getColor(mContext, R.color.orange_confetti), ContextCompat.getColor(mContext, R.color.blue_confetti), ContextCompat.getColor(mContext, R.color.purple_confetti))
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 3f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12, 5f))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        .streamFor(300, 5000L);
*/
                konfettiView.build()
                        .addColors(ContextCompat.getColor(mContext, R.color.orange_confetti), ContextCompat.getColor(mContext, R.color.blue_confetti), ContextCompat.getColor(mContext, R.color.purple_confetti))
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(14, 5f))
                        .setPosition(konfettiView.getWidth() / 2, -450 + konfettiView.getHeight() / 2)
                        .burst(100);


            }
        });
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                konfettiView.build()
                        .addColors(ContextCompat.getColor(mContext, R.color.orange_confetti), ContextCompat.getColor(mContext, R.color.blue_confetti), ContextCompat.getColor(mContext, R.color.purple_confetti))
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(14, 5f))
                        .setPosition(konfettiView.getWidth() / 2, -450 + konfettiView.getHeight() / 2)
                        .burst(100);

                              konfettiView.build()
                                      .addColors(ContextCompat.getColor(mContext, R.color.orange_confetti), ContextCompat.getColor(mContext, R.color.blue_confetti), ContextCompat.getColor(mContext, R.color.purple_confetti))
                    .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(14, 5f))
                    .setPosition(konfettiView.getWidth() / 2, -450 + konfettiView.getHeight() / 2)
                    .burst(100);

                konfettiView.build()
                        .addColors(ContextCompat.getColor(mContext, R.color.orange_confetti), ContextCompat.getColor(mContext, R.color.blue_confetti), ContextCompat.getColor(mContext, R.color.purple_confetti))
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(14, 5f))
                        .setPosition(konfettiView.getWidth() / 2, -450 + konfettiView.getHeight() / 2)
                        .burst(100);

            }
        }, 500);



        return view;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    OnEditTextChanged editListener;
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            editListener=(OnEditTextChanged) getActivity();
        }catch(ClassCastException e){
            throw new ClassCastException(activity +"must implemnt onEditPressed");
        }
    }

}
