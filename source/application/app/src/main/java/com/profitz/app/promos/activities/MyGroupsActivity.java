package com.profitz.app.promos.activities;

import static com.profitz.app.ProfitzApp.getContext;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.profitz.app.R;import com.profitz.app.data.model.GroupRequestsModel;
import com.profitz.app.data.model.GroupsModel;
import com.profitz.app.data.model.MessageModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.GroupRequestsAdapter;
import com.profitz.app.promos.async.SendFcmNotification;
import com.profitz.app.promos.enums.MessageType;
import com.profitz.app.promos.enums.RequestType;
import com.profitz.app.promos.fragments.BottomSheetDialogInvitationAcceptOrReject;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.util.NetworkUtil;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.profitz.app.ProfitzApp;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MyGroupsActivity extends AppCompatActivity implements BottomSheetDialogInvitationAcceptOrReject.BottomSheetListener,  BottomSheetDialogReportBug.BottomSheetListener  {
    private RelativeLayout main;
    private int groupInvites, groupCounts;
    private RelativeLayout rl_invitations;
    private TextView invitations_groups_text2, count_groups_text2;
    private String count_groups_text;
    final FragmentManager fm = getSupportFragmentManager();
    private RecyclerView rvRequestsParticipants;
    private HttpManager httpManager;
    private CustomLoadingDialog customLoadingDialog;
    private TextView noRecordFound;
    private ImageView back;
    private ArrayList<GroupRequestsModel> groupRequestsModels;
    private GroupRequestsAdapter groupRequestsAdapter;
    private static MyGroupsActivity instance = null;
    private Context context;
    private Runnable removeNotifier;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public void setRemoveNotifier(Runnable removeNotifier) {
        this.removeNotifier = removeNotifier;
    }

    private final View.OnClickListener onGroupSelected = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int index = rvRequestsParticipants.getChildAdapterPosition(view);
            Bundle args = new Bundle();
            BottomSheetDialogInvitationAcceptOrReject bottomSheetInvitationAcceptOrReject = new BottomSheetDialogInvitationAcceptOrReject();
            args.putInt("index",index);
            bottomSheetInvitationAcceptOrReject.setArguments(args);
            bottomSheetInvitationAcceptOrReject.show(getSupportFragmentManager(), bottomSheetInvitationAcceptOrReject.getTag());


        }
    };
    public static MyGroupsActivity getInstance() {
        return instance;
    }


    private final Runnable removeNotifierInvites = new Runnable() {
        @Override
        public void run() {
            View notifier = findViewById(R.id.notifier);
            notifier.setVisibility(View.GONE);
        }
    };

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
        setContentView(R.layout.activity_groups_my);
        rl_invitations = findViewById(R.id.rl_invitations);
        invitations_groups_text2 = findViewById(R.id.invitations_groups_text2);
        count_groups_text2 = findViewById(R.id.count_groups_text2);

        groupInvites= 0;
        groupCounts= 0;
        instance = this;
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
                    args.putString("source","MyGroupsActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////

        if (getIntent() != null) {
            groupInvites = getIntent().getIntExtra("groupInvites", 0);
            groupCounts = getIntent().getIntExtra("groupCounts", 0);

        }
        if (groupInvites==0){
            rl_invitations.setVisibility(View.GONE);
        }
        else{
            rl_invitations.setVisibility(View.VISIBLE);
            String invitations_groups_text;
            if (groupInvites==1){
               invitations_groups_text = "Masz "+groupInvites+" zaproszenie do grupy";
                invitations_groups_text2.setText(invitations_groups_text);
            }
            else if (groupInvites>=2 && groupInvites <=4){
                invitations_groups_text = "Masz "+groupInvites+" zaproszenia do grupy";
                invitations_groups_text2.setText(invitations_groups_text);
            }
            else{
                invitations_groups_text = "Masz "+groupInvites+" zaproszeń do grupy";
                invitations_groups_text2.setText(invitations_groups_text);
            }

        }

        if (groupCounts>0){
            String invitations_groups_text;
            if (groupInvites==1){
                count_groups_text = "Należysz do "+groupCounts+" grupy";
                count_groups_text2.setText(count_groups_text);
            }
            else{
                count_groups_text = "Należysz do "+groupCounts+" grup";
                count_groups_text2.setText(count_groups_text);
            }
        }
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        httpManager = new HttpManager(ProfitzApp.getContext());
        customLoadingDialog = new CustomLoadingDialog(this);
        context = ProfitzApp.getContext();
        rvRequestsParticipants = findViewById(R.id.rvRequestsParticipants);
        noRecordFound = findViewById(R.id.noRecordFound);
        noRecordFound.setVisibility(View.GONE);
        rvRequestsParticipants.setHasFixedSize(true);
        rvRequestsParticipants.setLayoutManager(new GridLayoutManager(context,2));

        customLoadingDialog.show();
        getUserGroupInvitations();

        main = findViewById(R.id.main);
        checkInternetConnection();
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


           // groupInvitationsFragment.setRemoveNotifier(removeNotifierInvites);

        }
    }



    private void getUserGroupInvitations() {
        httpManager.viewUserGroupInvitations(new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                Type type = new TypeToken<ArrayList<GroupRequestsModel>>() {}.getType();
                groupRequestsModels = new Gson().fromJson(response, type);
                groupRequestsAdapter = new GroupRequestsAdapter(context, groupRequestsModels, onGroupSelected, RequestType.INVITATION);
                rvRequestsParticipants.setAdapter(groupRequestsAdapter);

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

    public void respondToGroupRequest(final int index, final String status) {
        customLoadingDialog.show();

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
                          //  Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            Toasty.normal(context, message).show();

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



    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goBack();
        }
        return true;
    }

    private void goBack() {
        Utility.gotoActivity(this, ChatsActivity.class);
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
