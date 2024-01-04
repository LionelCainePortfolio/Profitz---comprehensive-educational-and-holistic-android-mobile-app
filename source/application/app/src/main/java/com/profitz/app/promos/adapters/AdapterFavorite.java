package com.profitz.app.promos.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promos.data.DataFavorite;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFavorite extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final Context context;
    private final LayoutInflater inflater;
    List<DataFavorite> data= Collections.emptyList();
    private final List<DataFavorite> dataListFull;
    DataFavorite current;
    int current_id;
    private List<Promo> mPromos;
    private final AdapterFavorite.PromoClickListener mOnClickListener;
    int currentPos=0;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterFavorite(Context context, AdapterFavorite.PromoClickListener listener, List<DataFavorite> data){
        this.context=context;
        mOnClickListener = listener;
        inflater= LayoutInflater.from(context);
        this.data=data;
        dataListFull = new ArrayList<>(data);


    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_favorite, parent,false);
        MyHolder holder=new MyHolder(view);
       // view.findViewById(R.id.favorite_container).setOnClickListener(mOnClickListener);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        current=data.get(position);
        current_id= current.favoritePromoId;
        myHolder.textName.setText(current.favoriteName);
        String imgUrl = current.favoriteImage;
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
                .into(  myHolder.ivFavorite ,new Callback() {
                    @Override
                    public void onSuccess() {
                        myHolder.animatedimageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        myHolder.animatedimageView.setVisibility(View.GONE);

                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(imgUrl)
                                .into( myHolder.ivFavorite, new Callback() {
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
/*
    @Override
    public Filter getFilter() {
        return dataFilter;
    }
    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            dataListFull.clear();
            final FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0)
                dataListFull.addAll(data);
            else {
                final String filterPattern = constraint.toString().toLowerCase().trim();


                for (DataFavorite item : data) {
                    if (item.favoriteName.toLowerCase().contains(filterPattern)) {
                        dataListFull.add(item);
                    }
                }
            }

            results.values = dataListFull;
            results.count = dataListFull.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                data.clear();
                data.addAll((List) results.values);
               notifyDataSetChanged();
            } else {
                data.clear();
                data.addAll(data);
                notifyDataSetChanged();
            }
        }
    };
    */
    @Override
    public Filter getFilter() {
        return dataFilter;
    }
    private final Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DataFavorite> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(dataListFull);

                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (DataFavorite item : dataListFull) {
                        if (item.favoriteName.toLowerCase().contains(filterPattern)) {
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

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }
    public List<Promo> getList() {
        return mPromos;
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textName;
        CircleImageView ivFavorite;
        ImageView animatedimageView;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.textName);
            ivFavorite= (CircleImageView) itemView.findViewById(R.id.ivFavorite);
            itemView.setOnClickListener(this);
            animatedimageView = (ImageView)  itemView.findViewById(R.id.ivFavoriteImageAnim);


        }
        @Override
        public void onClick(View view) {
            mOnClickListener.onPromoFavClick(data.get(getAdapterPosition()));
        }
    }

    public interface PromoClickListener {
        void onPromoFavClick(DataFavorite dataFavorite);
    }
}
