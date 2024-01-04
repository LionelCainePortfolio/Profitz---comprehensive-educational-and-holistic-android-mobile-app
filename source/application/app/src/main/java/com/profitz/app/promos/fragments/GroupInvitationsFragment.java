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
import com.profitz.app.promos.User;
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

public class GroupInvitationsFragment extends Fragment {
    private RecyclerView rv;
    private HttpManager httpManager;
    private TextView noRecordFound;

    private ArrayList<GroupRequestsModel> groupRequestsModels;
    private GroupRequestsAdapter groupRequestsAdapter;
    private static GroupInvitationsFragment instance = null;
    private Context context;
    private Runnable removeNotifier;

    public void setRemoveNotifier(Runnable removeNotifier) {
        this.removeNotifier = removeNotifier;
    }

    private final View.OnClickListener onGroupSelected = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int index = rv.getChildAdapterPosition(view);
            Bundle args = new Bundle();
            BottomSheetDialogInvitationAcceptOrReject bottomSheetInvitationAcceptOrReject = new BottomSheetDialogInvitationAcceptOrReject();
            args.putInt("index",index);
            bottomSheetInvitationAcceptOrReject.setArguments(args);
            bottomSheetInvitationAcceptOrReject.show(getChildFragmentManager(), bottomSheetInvitationAcceptOrReject.getTag());


        }
    };
    public static GroupInvitationsFragment getInstance() {
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_group_requests, container, false);
        httpManager = new HttpManager(getContext());
        context = inflater.getContext();
        rv = view.findViewById(R.id.rv);
        noRecordFound = view.findViewById(R.id.noRecordFound);
        noRecordFound.setVisibility(View.GONE);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(context,2));

        getUserGroupInvitations();

        return view;
    }

    private void getUserGroupInvitations() {
        httpManager.viewUserGroupInvitations(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                Type type = new TypeToken<ArrayList<GroupRequestsModel>>() {}.getType();
                groupRequestsModels = new Gson().fromJson(response, type);
                groupRequestsAdapter = new GroupRequestsAdapter(getActivity(), groupRequestsModels, onGroupSelected, RequestType.INVITATION);
                rv.setAdapter(groupRequestsAdapter);


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

    public void respondToGroupRequest(final int index, final String status) {

        final GroupRequestsModel groupRequestsModel = groupRequestsModels.get(index);
        final User user = groupRequestsModel.getUser();
        final GroupsModel groupsModel = groupRequestsModel.getGroup();

        httpManager.respondToGroupInvite(
                groupsModel.getId(),
                groupsModel.getCreated_by(),
                status,
                new HttpResponse() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                                    String status = jsonObject.getString("status");
                            final String message = jsonObject.getString("message");
                          //  Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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

                        MessageModel messageModel = new MessageModel();
                        if (status.equalsIgnoreCase("accept")) {
                            messageModel.setContent("Twoje zaproszenie do grupy " + groupsModel.getGroup_name() + " zostało zaakceptowane.");
                        } else if (status.equalsIgnoreCase("decline")) {
                            messageModel.setContent("Twoje zaproszenie do grupy " + groupsModel.getGroup_name() + " zostało odrzucone.");
                        }
                        messageModel.setMessage_type(MessageType.GROUP_INVITE_RESPONSE.getType());
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
