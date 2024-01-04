package com.profitz.app.promos.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.GroupsModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.GroupDetailActivity;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.interfaces.SocketListener;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class YourGroupsFragment extends Fragment {
    private View view;
    public ArrayList<GroupsModel> groupsModels;
    public RecyclerView rv;
    public GroupsAdapter groupsAdapter;
    private CustomLoadingDialog customLoadingDialog;
    private HttpManager httpManager;
    private User user;
    private TextView someoneTyping;
    private TextView noRecordFound;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_groups_layout, container, false);
        groupsModels = new ArrayList<>();
        httpManager = new HttpManager(getActivity());

        SocketManager.getInstance();
        context = getContext();
        customLoadingDialog = new CustomLoadingDialog(getActivity());
        noRecordFound = view.findViewById(R.id.noRecordFound);
        noRecordFound.setVisibility(View.GONE);
        someoneTyping = view.findViewById(R.id.someoneTyping);
        someoneTyping.setVisibility(View.GONE);

        user = MyPreferenceManager.getInstance(context).getUser();

        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(context,2));

        groupsAdapter = new GroupsAdapter(getActivity(), groupsModels, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = rv.getChildAdapterPosition(view);

                    Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                    intent.putExtra("groupId", groupsModels.get(position).getId());
                    Utility.gotoActivity((AppCompatActivity) getActivity(), intent);

            }
        });
        rv.setAdapter(groupsAdapter);

        customLoadingDialog.show();
        getAllGroupsWhenImMember();

        SocketManager.attachGroupDeletedListener(new SocketListener() {
            @Override
            public void onResponse(String response) {
                for (int a = 0; a < groupsModels.size(); a++) {
                    if (groupsModels.get(a).getId().equalsIgnoreCase(response)) {
                        groupsModels.remove(a);
                        break;
                    }
                }

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupsAdapter.setFilter(groupsModels);
                            if (groupsModels.size() == 0) {
                                noRecordFound.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });

        SocketManager.attachGroupCreatedListener(new SocketListener() {
            @Override
            public void onResponse(String response) {
                GroupsModel groupsModel = new Gson().fromJson(response, GroupsModel.class);
                groupsModels.add(groupsModel);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupsAdapter.setFilter(groupsModels);
                            noRecordFound.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        return view;
    }

    private void getAllGroupsWhenImMember() {
        if (getActivity() != null) {
            httpManager.getAllGroupsWhenImMember(new HttpResponse() {
                @Override
                public void onSuccess(String response) {
                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<GroupsModel>>() {}.getType();
                        groupsModels = gson.fromJson(response, listType);
                        groupsAdapter.setFilter(groupsModels);

                        if (groupsModels.size() == 0) {
                            noRecordFound.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    customLoadingDialog.dismiss();
                }

                @Override
                public void onError(String error) {
                    Log.i("my_log", error);
                }
            });
        } else {
            customLoadingDialog.dismiss();
        }
    }

    public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
        private final ArrayList<GroupsModel> groups;
        private final Context context;
        private final View.OnClickListener onClickListener;

        public GroupsAdapter(Context context, ArrayList<GroupsModel> groups, View.OnClickListener onClickListener) {
            this.groups = groups;
            this.context = context;
            this.onClickListener = onClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_single_your_group, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GroupsModel groupsModel = groups.get(position);
            if (groupsModel.isAm_I_member()) {

              //  holder.groupMembers.setText(groupsModel.getMembers().size() + 1);

                holder.your_group_view.setVisibility(View.VISIBLE);
                if (!Objects.equals(groupsModel.getGroup_name(), "add_group_button")){
                    holder.groupName.setText(groupsModel.getGroup_name());
                }
                if (Utility.isEmpty(Utility.BASE_URL + "/" +groupsModel.getPicture())) {
                    holder.groupImage.setVisibility(View.GONE);
                } else {

                    Utility.showImageAnimated(context, Utility.BASE_URL + "/" + groupsModel.getPicture(), holder.groupImage, holder.groupAnimatedImage);

                    // Utility.showImage(context, Utility.BASE_URL + "/" + groupsModel.getPicture(), holder.groupImage);
                    holder.groupRlBg.setVisibility(View.VISIBLE);

                }

            }
            else{
                holder.your_group_view.setVisibility(View.GONE);
                if (!Objects.equals(groupsModel.getGroup_name(), "add_group_button")){
                    holder.groupName.setText(groupsModel.getGroup_name());
                }
            }


        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        public void setFilter(ArrayList<GroupsModel> groups) {
            this.groups.clear();
            this.groups.addAll(groups);
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView groupName;
            private final TextView groupMembers;
            private final ImageView groupImage;
            private final ImageView groupAnimatedImage;
            private final ImageView groupImageAdd;
            private final RelativeLayout groupRlBg;
            private final RelativeLayout your_group_view;
            public ViewHolder(View itemView) {
                super(itemView);
                groupImageAdd = itemView.findViewById(R.id.groupImageAdd);
                groupImage = itemView.findViewById(R.id.groupImage);
                groupName = itemView.findViewById(R.id.groupName);
                groupMembers = itemView.findViewById(R.id.groupMembers);
                your_group_view = itemView.findViewById(R.id.your_group_view);
                groupRlBg = itemView.findViewById(R.id.card_view_groups_all);
                groupAnimatedImage = itemView.findViewById(R.id.groupAnimatedImage);

                itemView.setOnClickListener(onClickListener);
            }
        }
    }
}
