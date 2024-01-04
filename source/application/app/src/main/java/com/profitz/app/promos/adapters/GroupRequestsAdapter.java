package com.profitz.app.promos.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.GroupRequestsModel;
import com.profitz.app.promos.enums.RequestType;
import com.profitz.app.util.Utility;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupRequestsAdapter extends RecyclerView.Adapter<GroupRequestsAdapter.GroupRequestsViewHolder> {
    private final Context context;
    private final ArrayList<GroupRequestsModel> groupRequestsModels;
    private final View.OnClickListener onGroupSelected;
    private final RequestType requestType;

    public GroupRequestsAdapter(Context context, ArrayList<GroupRequestsModel> groupRequestsModels, View.OnClickListener onGroupSelected, RequestType requestType) {
        this.context = context;
        this.groupRequestsModels = groupRequestsModels;
        this.onGroupSelected = onGroupSelected;
        this.requestType = requestType;
    }

    @NonNull
    @Override
    public GroupRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (requestType == RequestType.INVITATION){
            view = LayoutInflater.from(context).inflate(R.layout.item_group_invitations_single, parent, false);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.item_group_requests_single, parent, false);
        }
        return new GroupRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRequestsViewHolder holder, int position) {
        if (requestType == RequestType.REQUEST) {
            holder.username.setText(groupRequestsModels.get(position).getUser().getUsername());

           // Utility.showImage(context, Utility.BASE_URL + "/" + groupRequestsModels.get(position).getUser().getPicture(), holder.image);
             Utility.showImageAnimated(context, groupRequestsModels.get(position).getUser().getPicture(), holder.memberImage, holder.imageAnim);
        } else if (requestType == RequestType.INVITATION) {
            holder.groupName.setText(groupRequestsModels.get(position).getGroup().getGroup_name());
          Utility.showImageAnimated(context, Utility.BASE_URL + "/" +groupRequestsModels.get(position).getGroup().getPicture(), holder.memberImage, holder.imageAnim);

         //   Utility.showImage(context, Utility.BASE_URL + "/" + groupRequestsModels.get(position).getGroup().getPicture(), holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return groupRequestsModels.size();
    }

    class GroupRequestsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAnim;
        CircleImageView memberImage;
        TextView username, groupName;

        GroupRequestsViewHolder(View itemView) {
            super(itemView);

            imageAnim = itemView.findViewById(R.id.imageAnim);
            memberImage = itemView.findViewById(R.id.memberImage);
            username = itemView.findViewById(R.id.username);
            groupName = itemView.findViewById(R.id.groupName);
            if (onGroupSelected != null) {
                itemView.setOnClickListener(onGroupSelected);
            }
        }
    }
}
