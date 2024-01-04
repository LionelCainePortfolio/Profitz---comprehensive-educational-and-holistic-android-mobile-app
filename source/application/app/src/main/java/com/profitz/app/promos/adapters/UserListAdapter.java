package com.profitz.app.promos.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.promos.User;
import com.profitz.app.util.Utility;

import java.util.ArrayList;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private final ArrayList<User> users;
    private final Context context;
    private final View.OnClickListener onUserSelected;
    private final View.OnLongClickListener onLongClickListener;

    public UserListAdapter(ArrayList<User> users, Context context, View.OnClickListener onUserSelected, View.OnLongClickListener onLongClickListener) {
        this.users = users;
        this.context = context;
        this.onUserSelected = onUserSelected;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member_list_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        User userModel = users.get(position);
        holder.username.setText(userModel.getName());
        Utility.showImage(context, Utility.BASE_URL + "/" + userModel.getPicture(), holder.image);
        Utility.toggleUserStatus(context, holder.onlineNotifier, userModel.isIs_online());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView image;
        View onlineNotifier;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            image = itemView.findViewById(R.id.image);
            onlineNotifier = itemView.findViewById(R.id.onlineNotifier);
            if (onUserSelected != null) {
                itemView.setOnClickListener(onUserSelected);
            }
            if (onLongClickListener != null) {
                itemView.setOnLongClickListener(onLongClickListener);
            }
        }
    }

    public void setFilter(ArrayList<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }
}
