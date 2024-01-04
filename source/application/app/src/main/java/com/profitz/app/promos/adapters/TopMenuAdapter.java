package com.profitz.app.promos.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.TopMenuModel;

import java.util.List;



public class TopMenuAdapter extends RecyclerView.Adapter<TopMenuAdapter.LeftMenuViewHolder> {
    private final List<TopMenuModel> topMenuItems;
    private final View.OnClickListener topMenuItemClickListener;

    public TopMenuAdapter(List<TopMenuModel> topMenuItems, View.OnClickListener topMenuItemClickListener) {
        this.topMenuItems = topMenuItems;
        this.topMenuItemClickListener = topMenuItemClickListener;
    }

    @NonNull
    @Override
    public LeftMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_top_menu_item, parent, false);
        return new LeftMenuViewHolder(v, topMenuItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LeftMenuViewHolder holder, int position) {
        TopMenuModel topMenuModel = topMenuItems.get(position);

        holder.itemImage.setImageResource(topMenuModel.getImage());
      //  holder.itemTitle.setText(topMenuModel.getTitle());

        if (topMenuModel.getNotification() > 0) {
            holder.notification.setVisibility(View.VISIBLE);
        } else {
            holder.notification.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return topMenuItems.size();
    }

    static class LeftMenuViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView itemTitle;
        private final View notification;

        LeftMenuViewHolder(final View itemView, final View.OnClickListener topMenuItemClickListener) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            notification = itemView.findViewById(R.id.notification);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topMenuItemClickListener.onClick(itemView);
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
