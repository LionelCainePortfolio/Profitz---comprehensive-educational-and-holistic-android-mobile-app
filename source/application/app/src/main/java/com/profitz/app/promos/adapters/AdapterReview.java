package com.profitz.app.promos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promos.data.DataReview;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterReview extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    List<DataReview> data= Collections.emptyList();
    private List<Promo> mPromos;

    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterReview(Context context, List<DataReview> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_review, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        DataReview current=data.get(position);
        myHolder.textName.setText(current.reviewName);
        myHolder.textDescription.setText(current.reviewDescription);
        myHolder.textDateN.setText(current.reviewDateAdd);
        myHolder.rating_score.setRating((float) current.reviewStars);
        String imgUrl = current.reviewImage;
        Picasso.with( context )
                .load( imgUrl )
                .placeholder( context.getDrawable(R.drawable.user_placeholder_avatar ) )
                .error( context.getDrawable( R.drawable.user_placeholder_avatar ) )
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into( myHolder.ivReview );

    }



    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }
    public List<Promo> getList() {
        return mPromos;
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textDescription;
        TextView textName;
        TextView textDateN;
        ImageView ivReview;
        RatingBar rating_score;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textDescription= (TextView) itemView.findViewById(R.id.text_content);
            textName= (TextView) itemView.findViewById(R.id.text_author);
            textDateN= (TextView) itemView.findViewById(R.id.text_date);
            ivReview= (ImageView) itemView.findViewById(R.id.ivReview);
            rating_score = (RatingBar) itemView.findViewById(R.id.rating_score);

        }

    }

}
