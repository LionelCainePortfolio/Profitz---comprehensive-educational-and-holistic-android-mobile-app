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
import com.profitz.app.promos.enums.FriendRequestType;
import com.profitz.app.promos.interfaces.FriendRequestListener;
import com.profitz.app.util.Utility;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private final Context context;
    private final View.OnClickListener onUserSelected;
    private final ArrayList<User> users;
    private final FriendRequestListener friendRequestListener;
    private final FriendRequestType friendRequestType;

    public FriendRequestAdapter(Context context, ArrayList<User> users, View.OnClickListener onUserSelected, FriendRequestListener friendRequestListener, FriendRequestType friendRequestType) {
        this.context = context;
        this.users = users;
        this.onUserSelected = onUserSelected;
        this.friendRequestListener = friendRequestListener;
        this.friendRequestType = friendRequestType;
    }

    @NonNull
    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_request_single, parent, false);
        return new FriendRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendRequestAdapter.ViewHolder holder, final int position) {
        final User userModel = users.get(position);
        holder.username.setText(userModel.getName());
        Utility.showImageAnimated(context, userModel.getPicture(), holder.image, holder.imageAnim);
        Utility.toggleUserStatus(context, holder.onlineNotifier, userModel.isIs_online());
        holder.btnActionAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendRequestType == FriendRequestType.RECEIVED) {
                    friendRequestListener.onAccept(String.valueOf(userModel.getId()), position);
                } else {
                   // friendRequestListener.onDecline(String.valueOf(userModel.getId()), position);
                }
            }
        });
        holder.btnActionReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friendRequestType == FriendRequestType.RECEIVED) {
                    friendRequestListener.onDecline(String.valueOf(userModel.getId()), position);
                }
            }
        });
    }

    public void setFilter(ArrayList<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView image;
        ImageView imageAnim;

        View onlineNotifier;
        TextView btnActionAccept;
        TextView btnActionReject;

        ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            image = itemView.findViewById(R.id.image);
            imageAnim = itemView.findViewById(R.id.imageAnim);
            btnActionAccept = itemView.findViewById(R.id.btnActionAccept);
            btnActionReject = itemView.findViewById(R.id.btnActionReject);
            onlineNotifier = itemView.findViewById(R.id.onlineNotifier);
            itemView.setOnClickListener(onUserSelected);
        }
    }
}
