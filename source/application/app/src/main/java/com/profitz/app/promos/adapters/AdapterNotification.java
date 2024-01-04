package com.profitz.app.promos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promos.data.DataNotification;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    List<DataNotification> data= Collections.emptyList();
    private List<Promo> mNotifications;

    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterNotification(Context context, List<DataNotification> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_notification, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        DataNotification current=data.get(position);
        myHolder.textTitle.setText(current.notificationTitle);
        myHolder.textDescription.setText(current.notificationDescription);
        myHolder.textEarnN.setText(current.notificationEarn);
        myHolder.textEarnType.setText(current.notificationEarnType);
        myHolder.textDateN.setText(current.notificationDateAdd);

        String imgUrl = current.notificationImage;

        Picasso mPicasso = Picasso.with(context);
        mPicasso.setIndicatorsEnabled(false);
        mPicasso.load(imgUrl).
                placeholder(R.drawable.placeholder_logo)
                .error(R.drawable.placeholder_logo)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into( myHolder.ivNotification);


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }
    public List<Promo> getList() {
        return mNotifications;
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textDescription;
        TextView textTitle;
        TextView textEarnN;
        TextView textEarnType;
        TextView textDateN;
        ImageView ivNotification;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textTitle= (TextView) itemView.findViewById(R.id.textTitle);
            textDescription= (TextView) itemView.findViewById(R.id.textDescription);
            textEarnN= (TextView) itemView.findViewById(R.id.textEarnN);
            textEarnType= (TextView) itemView.findViewById(R.id.textEarnType);
            textDateN= (TextView) itemView.findViewById(R.id.textDateN);

            ivNotification= (ImageView) itemView.findViewById(R.id.ivNotification);

        }

    }

}
