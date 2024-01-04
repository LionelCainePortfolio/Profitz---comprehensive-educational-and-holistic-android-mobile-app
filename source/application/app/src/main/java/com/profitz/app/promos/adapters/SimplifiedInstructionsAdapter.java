/*
 * Copyright 2018 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.profitz.app.promos.adapters;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.profitz.app.ProfitzApp;
import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promodetail.PromoDetailActivity;
import com.profitz.app.promos.data.DataInstructions;
import com.profitz.app.promos.fragments.SimplifyInstructionFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class SimplifiedInstructionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SimplifiedInstructionsAdapter.class.getSimpleName();
    private final LayoutInflater inflater;
    private static final int HIDE_THRESHOLD = 20;
    private final int scrolledDistance = 0;
    private final boolean controlsVisible = true;
    private List<Promo> mInstructions;
    private final Context mContext;
    private final FragmentManager fragmentManager;
    private final Fragment fragment;
    DataInstructions current;

    LottieAnimationView lottieView;
    private int anim =1;
    private boolean display = false;
    List<DataInstructions> data= Collections.emptyList();

    private int mViewIdAdapter;

    public void changeView(int viewId)
    {
        mViewIdAdapter= viewId;
        notifyDataSetChanged();
    }

    public SimplifiedInstructionsAdapter(Context context, List<DataInstructions> data, int viewIdAdapter, FragmentManager fragmentManager, SimplifyInstructionFragment fragment) {
        mContext = context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        mViewIdAdapter = viewIdAdapter;
        this.fragmentManager = fragmentManager;
        this.fragment = fragment;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.instruction_single, parent,false);
        ViewHolder holder=new ViewHolder(view);
        lottieView = view.findViewById(R.id.anim_move_slides);
        runAnim();
        return holder;
    }
    public void runAnim(){

        lottieView.setAnimation("swipe_right_to_left.json");
        lottieView.playAnimation();
        lottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //first animation has ended
                if (anim==1){
                    lottieView.pauseAnimation();
                    lottieView.setAnimation("swipe_left_to_right.json"); //will show only the first frame

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lottieView.playAnimation();
                            anim=2;
                        }
                    }, 0);
                }
                else if (anim==2){
                    lottieView.pauseAnimation();
                    lottieView.setAnimation("swipe_right_to_left.json"); //will show only the first frame

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lottieView.playAnimation();
                            anim=1;
                        }
                    }, 0);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder myHolder= (ViewHolder) holder;

        current=data.get(position);
        if (current.instructionsStep.equals("1")) {
            if (!display) {
                display = true;
                myHolder.lottieView.setVisibility(View.VISIBLE);
            } else {
                myHolder.lottieView.setVisibility(View.GONE);
            }
        } else {
            myHolder.anim_move_slides_text.setVisibility(View.GONE);
            myHolder.buttonRefer.setVisibility(View.GONE);

            myHolder.lottieView.setVisibility(View.GONE);
        }
        Context context = ProfitzApp.getContext();

        myHolder.mTextTitle.setText(current.instructionsTitle);
        String step = "Krok "+current.instructionsStep;
        myHolder.mTextStepInfo.setText(step);
        //holder.mTextReleaseDate.setText(promo.getReleaseDate());
        myHolder.mTextShortDesc.setText(Html.fromHtml(current.instructionsStepDescription));
        if ((current.instructionsStepDescription2 == null) || (current.instructionsStepDescription2.isEmpty()|| (current.instructionsStepDescription2 == "null"))){
            myHolder.mTextShortDesc2.setVisibility(View.GONE);
        }
        else{
            myHolder.mTextShortDesc2.setText(Html.fromHtml(current.instructionsStepDescription2));
        }
        if ((current.instructionsStepDescription3 == null) || (current.instructionsStepDescription3.isEmpty()|| (current.instructionsStepDescription3 == "null"))){            myHolder.mTextShortDesc3.setVisibility(View.GONE);
        }
        else{
            myHolder.mTextShortDesc3.setText(Html.fromHtml(current.instructionsStepDescription3));
        }
        if (current.instructionsStepDescription2.contains("http")) {
            myHolder.buttonRefer.setVisibility(View.VISIBLE);
            myHolder.mTextShortDesc2.setVisibility(View.GONE);
            myHolder.buttonRefer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String url = current.instructionsStepDescription2;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;



                    if (mContext instanceof PromoDetailActivity) {
                        ((PromoDetailActivity)mContext).openPromoUrl(url);
                    }
                }
            });
        }
        else if (current.instructionsStepDescription2.contains("zgłosić ukończenie")) {
            if ((current.instructionsStep.equals("24")) && (current.instructionsName.equals("bitFlyer"))) {
                myHolder.buttonDone.setVisibility(View.VISIBLE);

            } else {
                myHolder.buttonDone.setVisibility(View.GONE);

            }            /*myHolder.buttonDone.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                }
            });*/
        }
        Picasso.with( context )
                .load( current.instructionsUrlScreen1 )
                .placeholder(R.drawable.progress_animation)
                .error( context.getDrawable( R.mipmap.empty_view ) )
                .into( myHolder.parallaxImage );
        myHolder.fab_c.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                ((SimplifyInstructionFragment)fragment).openChat();

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


  class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextTitle;
        TextView mTextStepInfo;
        TextView mTextShortDesc;
        TextView mTextShortDesc2;
        TextView mTextShortDesc3;
        TextView mWhichPromo;
        TextView textName;
        TextView textStatus;
        ImageView ivError;
      Button buttonDone;
      Button buttonRefer;
      TextView heading;
      TextView anim_move_slides_text;
      LottieAnimationView lottieView;
      ImageView parallaxImage;
      NestedScrollView scrollView;
      ExtendedFloatingActionButton fab_c;
      FloatingActionButton fab_done;

        private Toast mToast;
        public ViewHolder(View itemView) {
            super(itemView);
           heading = itemView.findViewById(R.id.textStepInfo);

            mTextTitle = (TextView) itemView.findViewById(R.id.text_step);
            mTextShortDesc= (TextView) itemView.findViewById(R.id.text_desc);
            mTextShortDesc2= (TextView) itemView.findViewById(R.id.text_desc_2);
            mTextShortDesc3= (TextView) itemView.findViewById(R.id.text_desc_3);
            buttonDone = (Button) itemView.findViewById(R.id.buttonDone);
            buttonRefer = (Button) itemView.findViewById(R.id.buttonRefer);
            anim_move_slides_text = itemView.findViewById(R.id.anim_move_slides_text);
            lottieView = itemView.findViewById(R.id.anim_move_slides);
            //ivError= (ImageView) itemView.findViewById(R.id.ivDone);
            parallaxImage = itemView.findViewById(R.id.background);
            scrollView =  itemView.findViewById(R.id.scrollView);
            fab_c = itemView.findViewById(R.id.fab);

            fab_done = itemView.findViewById(R.id.fab_done);
            mTextStepInfo = itemView.findViewById(R.id.textStepInfo);
        }


    }
}
