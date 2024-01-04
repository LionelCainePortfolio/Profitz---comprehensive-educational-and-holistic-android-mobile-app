package com.profitz.app.promos.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.promos.fragments.BottomSheetDialogDone;
import com.profitz.app.promos.data.DataDone;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDone extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final Context context;
    private final LayoutInflater inflater;
    List<DataDone> data= Collections.emptyList();
    private final List<DataDone> dataListFull;
    DataDone current;
    private final FragmentManager fragmentManager;

    int currentPos=0;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterDone(Context context, List<DataDone> data, FragmentManager fragmentManager){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.fragmentManager = fragmentManager;
        dataListFull = new ArrayList<>(data);

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_done, parent,false);
        MyHolder holder= new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataDone current=data.get(position);
        myHolder.textStatus.setText(current.doneStatus);
        Bundle args = new Bundle();
        if (current.doneStatus.equals("w trakcie weryfikacji")) {
            myHolder.ivStatus.setVisibility(View.VISIBLE);
            myHolder.textStatus.setVisibility(View.VISIBLE);
            myHolder.textStatus.setText(current.doneStatus);
            myHolder.ivStatus.setBackgroundResource(R.drawable.ic_baseline_error_outline_24);

        }
        else if  (current.doneStatus.equals("odrzucono")) {
            myHolder.ivStatus.setVisibility(View.VISIBLE);
            myHolder.textStatus.setVisibility(View.VISIBLE);
            myHolder.textStatus.setText(current.doneStatus);
            myHolder.ivStatus.setBackgroundResource(R.drawable.ic_baseline_error_outline_24_red);
        }
        else {
            myHolder.ivStatus.setVisibility(View.VISIBLE);
            myHolder.textStatus.setVisibility(View.GONE);
            myHolder.ivStatus.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_24_green);

        }
        myHolder.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current.doneStatus.equals("w trakcie weryfikacji")) {
                    BottomSheetDialogDone bottomSheet = new BottomSheetDialogDone();
                    args.putString("promoName", current.doneName);

                    args.putString("promoStatus", "under_review");
                    args.putString("promoBonus",  current.doneBonus );
                    args.putString("promoAdditionalInfo",  current.doneStatusAdditionalInfo);
                    bottomSheet.setArguments(args);
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
                }
                else  if (current.doneStatus.equals("zatwierdzono"))
                {
                    BottomSheetDialogDone bottomSheet = new BottomSheetDialogDone();
                    args.putString("promoName", current.doneName);
                    args.putString("promoStatus", "accepted");
                    args.putString("promoBonus",  current.doneBonus );
                    args.putInt("promoId", current.donePromoId);
                    args.putString("promoReceivedPoints",  current.doneReceivedPoints);
                    args.putString("promoAdditionalInfo",  current.doneStatusAdditionalInfo);

                    bottomSheet.setArguments(args);
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
                }
                else if  (current.doneStatus.equals("odrzucono")) {
                    BottomSheetDialogDone bottomSheet = new BottomSheetDialogDone();

                    args.putString("promoName", current.doneName);
                    args.putString("promoStatus", "declined");
                    args.putString("promoBonus",  current.doneBonus );
                    args.putString("promoAdditionalInfo",  current.doneStatusAdditionalInfo);

                    bottomSheet.setArguments(args);
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
                }
            }
        });
        myHolder.textName.setText(current.doneName);
        if (!current.doneBonus.equals("0"))
        {
            if (current.doneStatus.equals("w trakcie weryfikacji"))
            {
                myHolder.textEarnRefback.setText("Oczekujący bonus: " + current.doneBonus + " pkt");
            }
            else{
                myHolder.textEarnRefback.setText("Bonus: " + current.doneBonus + " pkt");
            }
        }
        else
        {
            myHolder.textEarnRefback.setVisibility(View.GONE);
        }
        myHolder.textEarn.setText("Zarobione: " + current.doneEarn + " zł");

        //myHolder.textType.setText("id: " + current.donePromoId);
        myHolder.textDate.setText("Wykonano: " + current.doneDate);
        myHolder.textEarn.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.textEarnRefback.setTextColor(ContextCompat.getColor(context, R.color.green));

        // load image into imageview using glide
       // Glide.with(context).load("http://192.168.1.7/test/images/" + current.doneImage)
          //      .placeholder(R.drawable.ic_img_error)
        //        .error(R.drawable.ic_img_error)
            //    .into(myHolder.ivFish);
        String imgUrl = current.doneImage;
        AnimationDrawable progressAnimation;
        progressAnimation = (AnimationDrawable) myHolder.animatedimageView.getDrawable();
        progressAnimation.setCallback(myHolder.animatedimageView);
        progressAnimation.setVisible(true, true);
        myHolder.animatedimageView.post(new Runnable() {
            @Override
            public void run() {
                progressAnimation.start();
            }
        });
        Picasso.with( context )
                .load(imgUrl)
                .fit()
                .config(Bitmap.Config.ARGB_4444)
                .centerCrop()
                .noFade()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(  myHolder.ivDone ,new Callback() {
                    @Override
                    public void onSuccess() {
                        myHolder.animatedimageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        myHolder.animatedimageView.setVisibility(View.GONE);
                        Picasso.with(context)
                                .load(imgUrl)
                                .into( myHolder.ivDone, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        myHolder.animatedimageView.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onError() {

                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });




    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }
    public Filter getFilter() {
        return dataFilter;
    }
    private final Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DataDone> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(dataListFull);

            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DataDone item : dataListFull) {
                    if (item.doneName.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data.clear();
            data.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    static class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textStatus;
        CircleImageView ivDone;
        TextView textEarn;
        TextView textEarnRefback;
        TextView textDate;
        TextView textPrice;
        RelativeLayout btnOpen;
        ImageView ivStatus;
        ImageView animatedimageView;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textStatus = (TextView) itemView.findViewById(R.id.textStatus);
            textName= (TextView) itemView.findViewById(R.id.textName);
            ivDone= (CircleImageView) itemView.findViewById(R.id.ivDone);
            textEarn = (TextView) itemView.findViewById(R.id.textEarn);
            textEarnRefback = (TextView) itemView.findViewById(R.id.textEarnRefback);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            btnOpen = (RelativeLayout) itemView.findViewById(R.id.rl_global_done);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
            animatedimageView = (ImageView)  itemView.findViewById(R.id.ivDoneImageAnim);


        }

    }

}
