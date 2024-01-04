package com.profitz.app.promos.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.FriendRequestAdapter;
import com.profitz.app.promos.async.SendFcmNotification;
import com.profitz.app.promos.enums.FriendRequestType;
import com.profitz.app.promos.enums.GotoType;
import com.profitz.app.promos.enums.MessageType;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.BottomSheetDialogSendContactRequestConfirmation;
import com.profitz.app.promos.interfaces.FriendRequestListener;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.NetworkUtil;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.seismic.ShakeDetector;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddUserInContactListActivity extends AppCompatActivity implements BottomSheetDialogSendContactRequestConfirmation.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener  {
    private ArrayList<User> users;
    private HttpManager httpManager;
    private RecyclerView rvRefers;
    private TextView noRecordFound;
    private CustomLoadingDialog customLoadingDialog;
    private MembersListAdapter membersListAdapter;
    private MyPreferenceManager myPreferenceManager;
    private RelativeLayout main;
    private TextView pending_contacts_text;
    private int friendRequests;
    private TextView add_contacts_text;
    private Context context;
    private ImageView back;
    private User user;
    TextWatcher textWatcher;
    private EditText search_to_add_contacts;

    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private RecyclerView rvFriendRequests;
    private ArrayList<User> userModels;
    private FriendRequestAdapter friendRequestAdapter;

    private Runnable removeNotifier;
    public void setRemoveNotifier(Runnable removeNotifier) {
        this.removeNotifier = removeNotifier;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBarDetail);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setStatusBarColor(Color.gold_gradient_shape);
        //Drawable background = MyInformationsActivity.getResources().getDrawable(R.drawable.gold_gradient_shape);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));
        getWindow().setBackgroundDrawableResource(R.drawable.white);
        //enableBackButton();
        //goFullScreen();
        setContentView(R.layout.activity_add_user_in_contact_list);
        pending_contacts_text = findViewById(R.id.pending_contacts_text);
        add_contacts_text = findViewById(R.id.add_contacts_text);
        SocketManager.getInstance();
        userModels = new ArrayList<>();

        users = new ArrayList<>();
        context = this;
        user = MyPreferenceManager.getInstance(this).getUser();
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        httpManager = new HttpManager(this);
        myPreferenceManager = new MyPreferenceManager(this);
        customLoadingDialog = new CustomLoadingDialog(this);
        membersListAdapter = new MembersListAdapter(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = rvRefers.getChildAdapterPosition(view);
                String userId = String.valueOf(users.get(index).getId());
                Intent intent = new Intent(AddUserInContactListActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("fromAddContact", "1");
                Utility.gotoActivity(AddUserInContactListActivity.this, intent);
            }
        });
        /////Shake to report bug
        report_dialog = new BottomSheetDialogReportBug();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new com.profitz.app.promos.ShakeDetector();
        mShakeDetector.setOnShakeListener(new com.profitz.app.promos.ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                //tvShake.setText("Shake Action is just detected!!");

                if (report_dialog != null
                        && report_dialog.getDialog() != null
                        && report_dialog.getDialog().isShowing()
                        && !report_dialog.isRemoving()) {
                    //dialog is showing so do something
                } else {
                    Bundle args = new Bundle();
                    args.putString("source","AddUserInContactListActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        friendRequests = 0;

        if (getIntent() != null) {
            friendRequests = getIntent().getIntExtra("friendRequests", 0);
        }
        if (friendRequests==0){
            pending_contacts_text.setVisibility(View.GONE);
        }
        else{
            pending_contacts_text.setVisibility(View.VISIBLE);
        }
        rvRefers = findViewById(R.id.rvRefers);
        search_to_add_contacts = findViewById(R.id.search_to_add_contacts);

        rvRefers.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRefers.setLayoutManager(layoutManager);
        rvRefers.setAdapter(membersListAdapter);

        noRecordFound = findViewById(R.id.noRecordFound);
        main = findViewById(R.id.main);

        customLoadingDialog.show();
        httpManager.getUsersNotInContacts(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<User>>() {}.getType();
                    users = gson.fromJson(response, listType);
                    membersListAdapter.notifyDataSetChanged();

                    if (users.size() == 0) {
                        noRecordFound.setVisibility(View.VISIBLE);
                    } else {
                        noRecordFound.setVisibility(View.GONE);
                    }

                    final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

                    JsonObjectRequest jsonObjectRequestRefferals = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                       String data_refferals = response.getString("refferal_count");
                                        String add_contacts_text_string_text = "Poleceni znajomi ("+data_refferals+")";
                                        add_contacts_text.setText(add_contacts_text_string_text);
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
                    Volley.newRequestQueue(context).add(jsonObjectRequestRefferals);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                customLoadingDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                //
            }
        });

        checkInternetConnection();
        textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                membersListAdapter.getFilter().filter(editable.toString());
            }
        };
        search_to_add_contacts.addTextChangedListener(textWatcher);



          friendRequestAdapter = new FriendRequestAdapter(this, userModels, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = rvFriendRequests.getChildAdapterPosition(view);
                String userId = String.valueOf(userModels.get(index).getId());
                Intent intent = new Intent(AddUserInContactListActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("fromAddContact", "1");
                Utility.gotoActivity(AddUserInContactListActivity.this, intent);
            }
        }, new FriendRequestListener() {
            @Override
            public void onAccept(String userId, final int position) {
                respondFriendRequest(position, userId, "accept");
            }

            @Override
            public void onDecline(String userId, final int position) {
                respondFriendRequest(position, userId, "reject");
            }
        }, FriendRequestType.RECEIVED);

        rvFriendRequests = findViewById(R.id.rvFriendRequests);
        rvFriendRequests.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rvFriendRequests.setLayoutManager(layoutManager2);
        rvFriendRequests.setAdapter(friendRequestAdapter);

        getFriendRequests();


  }


    private void getFriendRequests() {
        httpManager.getFriendRequests(new HttpResponse() {
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

                customLoadingDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                //
            }
        });
    }

    private void respondFriendRequest(final int position, String userId, final String status) {
        customLoadingDialog.show();
        httpManager.respondToFriendRequest(
                userId,
                status,
                new HttpResponse() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                           // Toast.makeText(AddUserInContactListActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            Toasty.normal(AddUserInContactListActivity.this,  jsonObject.getString("message")).show();

                            MessageModel messageModel = new MessageModel();
                            messageModel.setMessage_type(MessageType.FRIEND_REQUEST_RESPONSE.getType());
                            if (status.equalsIgnoreCase("accept")) {
                                messageModel.setContent("Twoja prośba o kontakt z użytkownikiem " + myPreferenceManager.getUser().getName() + " została zaakceptowana.");
                            } else if (status.equalsIgnoreCase("reject")) {
                                messageModel.setContent("Twoja prośba o kontakt z użytkownikiem " + myPreferenceManager.getUser().getName() + " została odrzucona.");
                            }

                            new SendFcmNotification(userModels.get(position).getFcm_token(), messageModel, null).execute();

                            userModels.remove(position);
                            friendRequestAdapter.setFilter(userModels);
                            if (userModels.size() == 0) {
                                noRecordFound.setVisibility(View.VISIBLE);

                                if (removeNotifier != null) {
                                    removeNotifier.run();
                                }
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

    private void checkInternetConnection() {
        int status = NetworkUtil.getConnectivityStatusString(this);
        if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            Utility.showNoInternetMessage(this, main, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkInternetConnection();
                }
            });
        } else {
            String pending_contacts_string_text = "Prośby kontaktu ("+friendRequests+")";
            pending_contacts_text.setText(pending_contacts_string_text);


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goBack();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, ChatsActivity.class);
        intent.putExtra(Utility.GOTO_TYPE_KEY, GotoType.PRIVATE_TAB.getType());
        Utility.gotoActivity(AddUserInContactListActivity.this, intent);
    }

public void respondToSendContactRequestConfirmation(int position, int userId){
    httpManager.sendFriendRequest(String.valueOf(userId), new HttpResponse() {
        @Override
        public void onSuccess(String response) {
            customLoadingDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
           //     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Toasty.normal(getApplicationContext(), message).show();

                if (status.equalsIgnoreCase("success")) {
                    MessageModel messageModel = new MessageModel();
                    messageModel.setMessage_type(MessageType.FRIEND_REQUEST_SENT.getType());
                    messageModel.setContent("Użytkownik " + myPreferenceManager.getUser().getName() + " chce się z Tobą skontaktować.");
                    new SendFcmNotification(user.getFcm_token(), messageModel, null).execute();

                    users.remove(position);
                    membersListAdapter.notifyItemRemoved(position);
                    if (users.size() == 0) {
                        noRecordFound.setVisibility(View.VISIBLE);
                    } else {
                        noRecordFound.setVisibility(View.GONE);
                    }

                    SocketManager.sendRequest(String.valueOf(user.getId()), MessageType.FRIEND_REQUEST_SENT.getType());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {
            //
        }
    });
}

    @Override
    public void onButtonClicked(String text) {

    }


    private class MembersListAdapter extends RecyclerView.Adapter<MembersListAdapter.ViewHolder> {
        private final Context context;
        private final View.OnClickListener onUserSelected;
        private ArrayList<User> dataListFull;

        MembersListAdapter(Context context, View.OnClickListener onUserSelected) {
            this.context = context;
            this.onUserSelected = onUserSelected;

        }

        @NonNull
        @Override
        public MembersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_add_contact_user_list_single, parent, false);
            return new MembersListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MembersListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            final User user = users.get(position);
            holder.username.setText(user.getUsername());
            Utility.showImageAnimated(context, user.getPicture(), holder.image, holder.imageAnim);
            Utility.toggleUserStatus(context, holder.onlineNotifier, user.isIs_online());

            String titledialog = "Wyśłać prośbę o kontakt do użytkownika " + user.getUsername() +"?";
            holder.btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    BottomSheetDialogSendContactRequestConfirmation bottomSheetDialogSendContactRequestConfirmation = new BottomSheetDialogSendContactRequestConfirmation();
                    args.putInt("position",position);
                    args.putInt("userId", Integer.parseInt(user.getId()));
                    args.putString("username",user.getUsername());
                    bottomSheetDialogSendContactRequestConfirmation.setArguments(args);
                    bottomSheetDialogSendContactRequestConfirmation.show(getSupportFragmentManager(), bottomSheetDialogSendContactRequestConfirmation.getTag());
                }
            });
        }
        public Filter getFilter() {
            return dataFilter;
        }
        private final Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<User> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(dataListFull);

                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (User item : dataListFull) {
                        if (item.getUsername().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                users.clear();
                users.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
        @Override
        public int getItemCount() {
            return users.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView username;
            ImageView image, imageAnim;
            View onlineNotifier;
            TextView btnRequest;

            ViewHolder(View itemView) {
                super(itemView);
                username = itemView.findViewById(R.id.username);
                image = itemView.findViewById(R.id.image);
                imageAnim = itemView.findViewById(R.id.imageAnim);
                btnRequest = itemView.findViewById(R.id.btnRequest);
                onlineNotifier = itemView.findViewById(R.id.onlineNotifier);
                if (onUserSelected != null) {
                    itemView.setOnClickListener(onUserSelected);
                }
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}
