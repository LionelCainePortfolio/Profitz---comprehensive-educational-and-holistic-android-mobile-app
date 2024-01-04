package com.profitz.app.promos.fragments;

import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.data.model.GroupDetailModel;
import com.profitz.app.data.model.GroupsModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.GroupDetailActivity;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.interfaces.SocketListener;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class GroupsFragment extends Fragment {
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
    private boolean isIAmMember, isIAmBanned, isIAmAdmin;
    private GroupDetailModel groupDetailModel;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(layoutManager);

        groupsAdapter = new GroupsAdapter(getActivity(), groupsModels, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = rv.getChildAdapterPosition(view);
                if (position==0){


                    final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String premium = response.getString("premium");
                                        if (premium.equals("0")) {
                                            Activity activity = getActivity();
                                            new PromosActivity().showLockedContentOnlyPremiumDialog((AppCompatActivity) activity);
                                        }
                                        else{

                                            BottomSheetDialogCreateGroup bottomSheetCreateGroup = new BottomSheetDialogCreateGroup();
                                            bottomSheetCreateGroup.show(getParentFragmentManager(), bottomSheetCreateGroup.getTag());
                                           // Utility.gotoActivity(getActivity(), CreateGroupActivity.class);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error

                                }
                            });
                    Volley.newRequestQueue(context).add(jsonObjectRequest);

                }
                else{
                    httpManager.getGroupDetail(groupsModels.get(position).getId(), new HttpResponse() {
                        @Override
                        public void onSuccess(final String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equalsIgnoreCase("error")) {
                                    String message = jsonObject.getString("message");
                                   // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    Toasty.normal(context, message).show();


                                } else {
                                    groupDetailModel = new Gson().fromJson(response, GroupDetailModel.class);

                                    isIAmAdmin = groupDetailModel.getAdmin().getIdd().equalsIgnoreCase(String.valueOf(user.getId()));
                                    for (int a = 0; a < groupDetailModel.getMembers().size(); a++) {
                                        if (groupDetailModel.getMembers().get(a).getIdd().equalsIgnoreCase(String.valueOf(user.getId()))) {
                                            isIAmMember = true;
                                            isIAmBanned = groupDetailModel.getMembers().get(a).isIs_ban();
                                            break;
                                        }
                                    }
                                    if (isIAmMember || isIAmAdmin) {
                                        if (isIAmBanned) {

                                            Bundle args = new Bundle();
                                            BottomSheetDialogNoPermissions bottomSheetDialogNoPermissions = new BottomSheetDialogNoPermissions();
                                            args.putString("text", "Zostałeś zbanowany przez administratora grupy.");
                                            bottomSheetDialogNoPermissions.setArguments(args);
                                            bottomSheetDialogNoPermissions.show(getChildFragmentManager(), bottomSheetDialogNoPermissions.getTag());


                                        } else {
                                            Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
                                            intent.putExtra("groupId", groupsModels.get(position).getId());
                                            Utility.gotoActivity((AppCompatActivity) getActivity(), intent);
                                        }
                                    } else {
                                       //nie nalezy do grupy

                                        Bundle args = new Bundle();
                                        BottomSheetDialogJoinGroup bottomSheetDialogJoinGroup = new BottomSheetDialogJoinGroup();
                                        args.putString("groupId",groupsModels.get(position).getId());
                                        bottomSheetDialogJoinGroup.setArguments(args);
                                        bottomSheetDialogJoinGroup.show(getChildFragmentManager(), bottomSheetDialogJoinGroup.getTag());



                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            customLoadingDialog.dismiss();
                        }

                        @Override
                        public void onError(String error) {
                            //
                        }
                    });

                }

            }
        });
        rv.setAdapter(groupsAdapter);

        customLoadingDialog.show();
        getAllGroups();

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

    private void getAllGroups() {
        if (getActivity() != null) {
            httpManager.getAllGroups(new HttpResponse() {
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
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_single_group, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GroupsModel groupsModel = groups.get(position);
            if (!groupsModel.getGroup_name().equals("create_new_group_premium")){
                holder.groupName.setText(groupsModel.getGroup_name());
            }
            else{
                holder.groupName.setVisibility(View.GONE);
            }

            if (groupsModel.isAm_I_member()) {
                holder.iconLock.setVisibility(View.GONE);

                if (groupsModel.getNum_unread() > 0) {
                    String unread_text = String.valueOf(groupsModel.getNum_unread());
                    holder.numUnRead.setText(unread_text);
                    holder.numUnRead.setVisibility(View.VISIBLE);
                } else {
                    holder.numUnRead.setVisibility(View.GONE);
                }
            } else {
                if (position!=0){
                    holder.numUnRead.setVisibility(View.GONE);
                    holder.iconLock.setVisibility(View.VISIBLE);
                }
            }

            if (Utility.isEmpty(Utility.BASE_URL + "/" +groupsModel.getPicture())) {
                holder.groupImage.setVisibility(View.GONE);
            } else {
                if (position==0) {
                    // Utility.showImage(context, Utility.BASE_URL + "/" + groupsModel.getPicture(), holder.groupImage);
                    holder.card_view_groups_add.setVisibility(View.VISIBLE);
                    holder.groupRlBg.setVisibility(View.GONE);
                }
                else{
                    Utility.showImageAnimated(context, Utility.BASE_URL + "/" + groupsModel.getPicture(), holder.groupImage, holder.groupAnimatedImage);

                    // Utility.showImage(context, Utility.BASE_URL + "/" + groupsModel.getPicture(), holder.groupImage);
                    holder.groupRlBg.setVisibility(View.VISIBLE);
                    holder.card_view_groups_add.setVisibility(View.GONE);

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
            private final TextView numUnRead;
            private final ImageView iconLock;
            private final ImageView groupImage;
            private final ImageView groupAnimatedImage;
            private final ImageView groupImageAdd;
            private final RelativeLayout groupRlBg;
            private final RelativeLayout card_view_groups_add;
            public ViewHolder(View itemView) {
                super(itemView);
                groupImageAdd = itemView.findViewById(R.id.groupImageAdd);
                groupImage = itemView.findViewById(R.id.groupImage);
                card_view_groups_add = itemView.findViewById(R.id.card_view_groups_add);
                groupName = itemView.findViewById(R.id.groupName);
                numUnRead = itemView.findViewById(R.id.numUnRead);
                iconLock = itemView.findViewById(R.id.iconLock);
                groupRlBg = itemView.findViewById(R.id.card_view_groups_all);
                groupAnimatedImage = itemView.findViewById(R.id.groupAnimatedImage);

                itemView.setOnClickListener(onClickListener);
            }
        }
    }
}
