package com.profitz.app.promos.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.data.model.TopMenuModel;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.TopMenuAdapter;
import com.profitz.app.promos.enums.GotoType;
import com.profitz.app.promos.enums.LeftMenuItems;
import com.profitz.app.promos.enums.MessageType;
import com.profitz.app.promos.fragments.BottomSheetDialogCreateGroup;
import com.profitz.app.promos.fragments.BottomSheetDialogJoinGroup;
import com.profitz.app.promos.fragments.BottomSheetDialogNoPermissions;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.ContactsListFragment;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.interfaces.SocketListener;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.promos.managers.SocketManager;
import com.profitz.app.util.NetworkUtil;
import com.profitz.app.util.Utility;
import com.squareup.seismic.ShakeDetector;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChatsActivity extends AppCompatActivity implements BottomSheetDialogCreateGroup.BottomSheetListener, BottomSheetDialogJoinGroup.BottomSheetListener, BottomSheetDialogNoPermissions.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener  {
    private MyPreferenceManager myPreferenceManager;
    private HttpManager httpManager;
    private TopMenuAdapter topMenuAdapter;
    private List<TopMenuModel> topMenuItems;
    private ConstraintLayout constraintLayout;
    private ImageView userImage;
    private ImageView userImageAnim;
    private RecyclerView topMenuRV;
    private TextView request_contact;
    private ImageView arrow_back;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private User user;
    private int groupRequests, groupInvites, groupCounts, friendRequests;

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
        setContentView(R.layout.activity_chats);

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
                    args.putString("source","ChatsActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////

        myPreferenceManager = new MyPreferenceManager(this);
        httpManager = new HttpManager(this);
        groupRequests = groupInvites = friendRequests = 0;
        groupCounts = 0;
        constraintLayout = findViewById(R.id.constraint_layout_chats);
        topMenuRV = findViewById(R.id.top_menu);
        arrow_back = findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ContactsListFragment fragment = new ContactsListFragment();
                fragment.stopHandler();
                finish();
            }
        });
        if (myPreferenceManager.isLoggedIn()) {
            initializeView();
        } else {
            Utility.gotoActivity(ChatsActivity.this, LoginActivity.class);
        }
    }

    public void initializeView() {
        userImage = findViewById(R.id.userImage);
        userImageAnim = findViewById(R.id.userImageAnim);

        user = MyPreferenceManager.getInstance(this).getUser();

        final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String user_picture = response.getString("image_url");
                            Utility.showImageAnimated(ChatsActivity.this, user_picture, userImage, userImageAnim);

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
        Volley.newRequestQueue(ChatsActivity.this).add(jsonObjectRequest);

       // Utility.showImageAnimated(this, user.getPicture(), userImage, userImageAnim);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        topMenuRV.setLayoutManager(llm);


        setTopMenuItems();
        topMenuAdapter = new TopMenuAdapter(topMenuItems, topMenuItemClickListener);
        topMenuRV.setAdapter(topMenuAdapter);

        checkInternetConnection();



    }

    private void checkInternetConnection() {
        int status = NetworkUtil.getConnectivityStatusString(this);
        if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            Utility.showNoInternetMessage(this, constraintLayout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkInternetConnection();
                }
            });
        } else {
            SocketManager.getInstance();

            //ViewPager viewPager = findViewById(R.id.viewpager);
            // Create new fragment and transaction

            request_contact = findViewById(R.id.request_contact);

            request_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContactsListFragment fragment = new ContactsListFragment();
                    fragment.stopHandler();
                    Utility.gotoActivity(ChatsActivity.this, AddUserInContactListActivity.class);
                }
            });




           // HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager());
           // adapter.addFragment(new GroupsFragment(), "Groups");
           // adapter.addFragment(new ContactsListFragment(), "Private");
          //  viewPager.setAdapter(adapter);


            if (getIntent() != null) {
                String gotoType = getIntent().getStringExtra(Utility.GOTO_TYPE_KEY);
                if (gotoType != null && gotoType.equalsIgnoreCase(GotoType.PRIVATE_TAB.getType())) {
                   // viewPager.setCurrentItem(1);
                }
            }

            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    String userId = String.valueOf(user.getId());
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("groupId", "");
                    Utility.gotoActivity(ChatsActivity.this, intent);
                     */
                }
            });

            httpManager.getHomeMetaData(new HttpResponse() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        groupRequests = jsonObject.getInt("group_requests");
                        groupInvites = jsonObject.getInt("group_invites");
                        groupCounts = jsonObject.getInt("group_counts");
                        friendRequests = jsonObject.getInt("friend_requests");

                        updateTopMenu();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    //
                }
            });

            SocketManager.attachIncomingRequestListener(new SocketListener() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase(MessageType.FRIEND_REQUEST_SENT.getType())) {
                        friendRequests = 1;
                    }
                    if (response.equalsIgnoreCase(MessageType.GROUP_REQUEST.getType())) {
                        groupRequests = 1;
                    }
                    if (response.equalsIgnoreCase(MessageType.GROUP_INVITE.getType())) {
                        groupInvites = 1;
                    }
                    updateTopMenu();
                }
            });
        }
    }

    private void updateTopMenu() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                topMenuItems.get(0).setNotification(groupRequests);
                if (groupInvites > 0) {
                    topMenuItems.get(0).setNotification(1);
                }
                topMenuItems.get(1).setNotification(friendRequests);
                topMenuAdapter.notifyDataSetChanged();
            }
        });
    }

    public View.OnClickListener topMenuItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = topMenuRV.getChildLayoutPosition(view);
            String title = topMenuItems.get(position).getTitle();
            ContactsListFragment fragment = new ContactsListFragment();
            fragment.stopHandler();

            if (title.equalsIgnoreCase(LeftMenuItems.GROUP_REQUESTS.getItem())) {
                Intent intent = new Intent(ChatsActivity.this, MyGroupsActivity.class);
                intent.putExtra("groupInvites", groupInvites);
                intent.putExtra("groupCounts", groupCounts);
                intent.putExtra("groupRequests", groupRequests);
                Utility.gotoActivity(ChatsActivity.this, intent);
            } else if (title.equalsIgnoreCase(LeftMenuItems.FRIEND_REQUESTS.getItem())) {
                Intent intent = new Intent(ChatsActivity.this, AddUserInContactListActivity.class);
                intent.putExtra("friendRequests", friendRequests);
                intent.putExtra("groupCounts", groupCounts);

                Utility.gotoActivity(ChatsActivity.this, intent);
            }
        }
    };

    public void setTopMenuItems() {
        topMenuItems = new ArrayList<>();
        topMenuItems.add(new TopMenuModel(R.drawable.ic_groups_new, LeftMenuItems.GROUP_REQUESTS.getItem(), 0));
        topMenuItems.add(new TopMenuModel(R.drawable.ic_refferals_new, LeftMenuItems.FRIEND_REQUESTS.getItem(), 0));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onButtonClicked(String text) {

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
