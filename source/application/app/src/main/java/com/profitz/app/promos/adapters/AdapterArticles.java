package com.profitz.app.promos.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.promos.activities.ArticleActivity;
import com.profitz.app.promos.data.DataArticles;
import com.profitz.app.promos.data.DataTopics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterArticles extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    List<DataArticles> data= Collections.emptyList();
    private final List<DataArticles> dataListFull;
    DataTopics current;
    private final FragmentManager fragmentManager;

    int currentPos=0;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterArticles(Context context, List<DataArticles> data, FragmentManager fragmentManager){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.fragmentManager = fragmentManager;
        dataListFull = new ArrayList<>(data);

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_articles, parent,false);
        MyHolder holder=new MyHolder(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataArticles current=data.get(position);

        myHolder.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, ArticleActivity.class);
                myIntent.putExtra("article_name",current.articleName);
                myIntent.putExtra("article_description",current.articleDesc);
                myIntent.putExtra("article_id",current.articleIdd);
                myIntent.putExtra("article_was_helpful_yes",current.articleWasHelpfulYes);
                myIntent.putExtra("article_was_helpful_no",current.articleWasHelpfulNo);

                context.startActivity(myIntent);
/*
                    BottomSheetDialogDone bottomSheet = new BottomSheetDialogDone();
                    args.putInt("topicCategoryId", current.topicCategoryId);

                    bottomSheet.setArguments(args);
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
*/
            }
        });

        myHolder.textName.setText(current.articleName);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textName;
        RelativeLayout btnOpen;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.text_article_name);
            btnOpen = (RelativeLayout) itemView.findViewById(R.id.rl_global_article_one);


        }

    }

}
