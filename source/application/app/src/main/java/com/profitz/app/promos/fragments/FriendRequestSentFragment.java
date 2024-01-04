package com.profitz.app.promos.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.profitz.app.R;import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.ProfileActivity;
import com.profitz.app.promos.adapters.FriendRequestAdapter;
import com.profitz.app.promos.enums.FriendRequestType;
import com.profitz.app.promos.interfaces.FriendRequestListener;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class FriendRequestSentFragment extends Fragment {
    private RecyclerView rv;
    private TextView noRecordFound;

    private ArrayList<User> userModels;
    private HttpManager httpManager;
    private CustomLoadingDialog customLoadingDialog;
    private FriendRequestAdapter friendRequestAdapter;

    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_request_received, container, false);
        userModels = new ArrayList<>();
        httpManager = new HttpManager(getActivity());
        customLoadingDialog = new CustomLoadingDialog(getActivity());
        friendRequestAdapter = new FriendRequestAdapter(getActivity(), userModels, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = rv.getChildAdapterPosition(view);
                String userId = String.valueOf(userModels.get(index).getId());
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("fromAddContact", "1");
                Utility.gotoActivity((AppCompatActivity) getActivity(), intent);
            }
        }, new FriendRequestListener() {
            @Override
            public void onAccept(String userId, final int position) {
                //
            }

            @Override
            public void onDecline(String userId, final int position) {
                customLoadingDialog.show();
                httpManager.respondToSentFriendRequest(
                        userId,
                        "reject",
                        new HttpResponse() {
                            @Override
                            public void onSuccess(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                    Toasty.normal(getActivity(), jsonObject.getString("message")).show();
                                    userModels.remove(position);
                                    friendRequestAdapter.setFilter(userModels);
                                    if (userModels.size() == 0) {
                                        noRecordFound.setVisibility(View.VISIBLE);
                                    } else {
                                        noRecordFound.setVisibility(View.GONE);
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
                        }
                );
            }
        }, FriendRequestType.SENT);

        rv = view.findViewById(R.id.rv);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(friendRequestAdapter);

        noRecordFound = view.findViewById(R.id.noRecordFound);
        noRecordFound.setVisibility(View.GONE);

        customLoadingDialog.show();
        getSentFriendRequests();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSentFriendRequests();
            }
        });

        return view;
    }

    private void getSentFriendRequests() {
        httpManager.getSentFriendRequests(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                    userModels = gson.fromJson(response, listType);
                    friendRequestAdapter.setFilter(userModels);

                    if (userModels.size() == 0) {
                        noRecordFound.setVisibility(View.VISIBLE);
                    } else {
                        noRecordFound.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                swipeRefresh.setRefreshing(false);
                customLoadingDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                //
            }
        });
    }
}
