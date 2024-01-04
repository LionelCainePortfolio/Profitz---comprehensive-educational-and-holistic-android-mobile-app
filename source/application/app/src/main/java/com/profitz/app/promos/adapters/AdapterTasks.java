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
import androidx.viewpager2.widget.ViewPager2;

import com.profitz.app.R;import com.profitz.app.promos.data.DataTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterTasks extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final Context context;
    private final LayoutInflater inflater;
    List<DataTask> data= Collections.emptyList();
    private List<DataTask> dataListFull;
    DataTask current;
    int current_id;
    private final ViewPager2 viewPager2;
    private List<DataTask> mTasks;
    String showUndoneDone;
    int currentPos=0;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterTasks(Context context, List<DataTask> data, ViewPager2 viewPager2, String showUndoneDone) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.viewPager2 = viewPager2;
        //dataListFull = new ArrayList<>(data);
        this.showUndoneDone = showUndoneDone;


    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_task, parent,false);
        MyHolder holder=new MyHolder(view);
       // view.findViewById(R.id.favorite_container).setOnClickListener(mOnClickListener);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (showUndoneDone.equals("undone")){
            if (position == data.size() - 1)
            {
                viewPager2.post(runnable);
            }
        }

        MyHolder myHolder= (MyHolder) holder;

            current=data.get(position);
            current_id= current.taskId;
        Log.d("imgg", current.taskIsDone);

        if (current.taskIsActive.equals("1")){
            myHolder.textName.setText(current.taskImage);

            String imgUrl = current.taskImage;
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
                    .into(  myHolder.ivTask ,new Callback() {
                        @Override
                        public void onSuccess() {
                            myHolder.animatedimageView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(context)
                                    .load(imgUrl)
                                    .into( myHolder.ivTask, new Callback() {
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
            List<DataTask> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(dataListFull);

                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (DataTask item : dataListFull) {
                        if (item.taskTitle.toLowerCase().contains(filterPattern)) {
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
        return data==null?0:data.size();

    }
    public List<DataTask> getList() {
        return mTasks;
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textName;
        ImageView ivTask;
        ImageView animatedimageView;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.textName);
            ivTask= itemView.findViewById(R.id.ivTask);
            itemView.setOnClickListener(this);
            animatedimageView = (ImageView)  itemView.findViewById(R.id.ivTaskImageAnim);


        }
        @Override
        public void onClick(View view) {
          //  mOnClickListener.onPromoFavClick(data.get(getAdapterPosition()));
        }
    }

    public interface TaskClickListener {
        void onTaskClick(DataTask dataTask);
    }
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            data.addAll(data);
            notifyDataSetChanged();
        }
    };
}
