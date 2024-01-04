package com.profitz.app.promos.fragments;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.profitz.app.R;import com.profitz.app.data.model.GroupRequestsModel;
import com.profitz.app.data.model.GroupsModel;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.MyGroupsActivity;
import com.profitz.app.promos.adapters.GroupRequestsAdapter;
import com.profitz.app.promos.async.SendFcmNotification;
import com.profitz.app.promos.enums.MessageType;
import com.profitz.app.promos.enums.RequestType;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MyGroupRequestsFragment extends Fragment {
    private RecyclerView rv;
    private HttpManager httpManager;
    private CustomLoadingDialog customLoadingDialog;
    private TextView noRecordFound;
    private static final MyGroupsActivity instance = null;

    private ArrayList<GroupRequestsModel> groupRequestsModels;
    private GroupRequestsAdapter groupRequestsAdapter;
    private Context context;
    private Runnable removeNotifier;

    public void setRemoveNotifier(Runnable removeNotifier) {
        this.removeNotifier = removeNotifier;
    }
    public static MyGroupsActivity getInstance() {
        return instance;
    }

    private final View.OnClickListener onGroupSelected = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int index = rv.getChildAdapterPosition(view);

            Bundle args = new Bundle();
            BottomSheetDialogGroupRequestAcceptOrReject bottomSheetGroupRequestAcceptOrReject = new BottomSheetDialogGroupRequestAcceptOrReject();
            args.putInt("index",index);
            bottomSheetGroupRequestAcceptOrReject.setArguments(args);
            bottomSheetGroupRequestAcceptOrReject.show(getChildFragmentManager(), bottomSheetGroupRequestAcceptOrReject.getTag());
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group_requests, container, false);
        httpManager = new HttpManager(getContext());
        customLoadingDialog = new CustomLoadingDialog(getActivity());
        context = inflater.getContext();

        rv = view.findViewById(R.id.rvRequestsParticipants);
        noRecordFound = view.findViewById(R.id.noRecordFound);
        noRecordFound.setVisibility(View.GONE);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(context,4));

        customLoadingDialog.show();
        getUserGroupRequests();



        return view;
    }

    private void getUserGroupRequests() {
        httpManager.viewUserGroupRequests(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                Type type = new TypeToken<ArrayList<GroupRequestsModel>>() {}.getType();
                groupRequestsModels = new Gson().fromJson(response, type);
                groupRequestsAdapter = new GroupRequestsAdapter(getActivity(), groupRequestsModels, onGroupSelected, RequestType.REQUEST);
                rv.setAdapter(groupRequestsAdapter);

                customLoadingDialog.dismiss();

                if (groupRequestsModels.size() == 0) {
                    noRecordFound.setVisibility(View.VISIBLE);
                } else {
                    noRecordFound.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
                Log.i("my_log", error);
            }
        });
    }

    public void respondToGroupRequestFragment(final int index, final String status) {
        customLoadingDialog.show();

        GroupRequestsModel groupRequestsModel = groupRequestsModels.get(index);
        final User user = groupRequestsModel.getUser();
        final GroupsModel groupsModel = groupRequestsModel.getGroup();

        httpManager.respondToGroupRequest(
                groupsModel.getId(),
                String.valueOf(user.getId()),
                status,
                new HttpResponse() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                                    String status = jsonObject.getString("status");
                            final String message = jsonObject.getString("message");
                         //   Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            Toasty.normal(getActivity(), message).show();

                            groupRequestsModels.remove(index);
                            groupRequestsAdapter.notifyDataSetChanged();

                            if (groupRequestsModels.size() == 0) {
                                noRecordFound.setVisibility(View.VISIBLE);

                                if (removeNotifier != null) {
                                    removeNotifier.run();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        customLoadingDialog.dismiss();

                        MessageModel messageModel = new MessageModel();
                        if (status.equalsIgnoreCase("decline")) {
                            messageModel.setContent("Twoja prośba o dołączenie do grupy " + groupsModel.getGroup_name() + " została odrzucona.");
                        } else if (status.equalsIgnoreCase("accept")) {
                            messageModel.setContent("Twoja prośba o dołączenie do grupy " + groupsModel.getGroup_name() + " została zaakceptowana.");
                        }

                        messageModel.setMessage_type(MessageType.GROUP_REQUEST_RESPONSE.getType());
                        new SendFcmNotification(user.getFcm_token(), messageModel, null).execute();
                    }

                    @Override
                    public void onError(String error) {
                        //
                    }
                }
        );
    }
}
