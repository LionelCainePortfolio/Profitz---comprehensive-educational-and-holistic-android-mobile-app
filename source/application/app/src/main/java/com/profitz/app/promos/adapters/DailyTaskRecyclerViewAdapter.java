package com.profitz.app.promos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import java.util.List;

public class


DailyTaskRecyclerViewAdapter extends RecyclerView.Adapter<DailyTaskRecyclerViewAdapter.ViewHolder> {
    private LinearLayoutManager manager;

    public DailyTaskRecyclerViewAdapter(LinearLayoutManager manager)
    {
        this.manager=manager;
    }

    private List<Integer> mViewColors;
    private List<String> mAnimals;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    int mactual_day;
    private Context mContext;
    String day1;
    Boolean do_once = false;

    // data is passed into the constructor
    public DailyTaskRecyclerViewAdapter(Context context, List<Integer> colors, List<String> animals, int actual_day) {
        this.mInflater = LayoutInflater.from(context);
        this.mViewColors = colors;
        this.mAnimals = animals;
        this.mactual_day = actual_day;
        mContext = context;

    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.hidden_day, parent, false);
        //getData();

        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row


    // total number of rows
    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rl_img_day_bg;
        ImageView img_day;
        View myView;
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            try {
            rl_img_day_bg = itemView.findViewById(R.id.rl_img_day_bg);
            img_day = itemView.findViewById(R.id.img_day);
            itemView.findViewById(R.id.img_day).setBackgroundResource(R.drawable.prezent_task);
           // myView = itemView.findViewById(R.id.colorView);
            myTextView = itemView.findViewById(R.id.text_day);
            itemView.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int color = mViewColors.get(position);
        String animal = mAnimals.get(position);
        //holder.myView.setBackgroundColor(color);
        holder.myTextView.setText(animal);
        //Log.e("Animal", String.valueOf(position));
        //Log.e("Actual day", actual_day);
        int scroll_to_day = mactual_day+1;
        //Toast.makeText(mContext, mactual_day, Toast.LENGTH_SHORT).show();
        if (position < mactual_day){
            holder.rl_img_day_bg.setBackgroundResource(0);
            holder.rl_img_day_bg.setPadding(15,15,15,15);
            holder.rl_img_day_bg.setBackgroundResource(R.drawable.circle_shape_done);
            holder.img_day.setBackgroundResource(0);
            holder.img_day.setBackgroundResource(R.drawable.ic_done_green_24dp);

        }
        else if ((position == 31) && (position==mactual_day)){
            holder.rl_img_day_bg.setBackgroundResource(0);
            holder.rl_img_day_bg.setPadding(15,15,15,15);
            holder.rl_img_day_bg.setBackgroundResource(R.drawable.circle_shape_done);
            holder.img_day.setBackgroundResource(0);
            holder.img_day.setBackgroundResource(R.drawable.ic_done_green_24dp);
        }
        else if ((position == 31) && (position<mactual_day)){
            holder.rl_img_day_bg.setBackgroundResource(R.drawable.circle_shape);
            holder.rl_img_day_bg.setPadding(15,15,15,15);
            holder.img_day.setBackgroundResource(R.drawable.prezent_task);
        }
        else if(position==mactual_day)
        {

                    holder.rl_img_day_bg.setBackgroundResource(R.drawable.circle_shape);
                    holder.rl_img_day_bg.setPadding(15,15,15,15);
                    holder.img_day.setBackgroundResource(R.drawable.prezent_task);
                    final Animation animResize= AnimationUtils.loadAnimation(mContext, R.anim.resize);
                    holder.rl_img_day_bg.startAnimation(animResize);


        }
        else{
            holder.rl_img_day_bg.setBackgroundResource(R.drawable.circle_shape);
            holder.rl_img_day_bg.setPadding(15,15,15,15);
            holder.img_day.setBackgroundResource(R.drawable.prezent_task);
        }
       // manager.scrollToPosition(scroll_to_day);

    }
    // convenience method for getting data at click position
    public String getItem(int id) {
        return mAnimals.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}