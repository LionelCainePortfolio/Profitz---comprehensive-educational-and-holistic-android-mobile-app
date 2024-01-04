package com.profitz.app.promos.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.util.SegmentedButtonGroup;
import com.squareup.seismic.ShakeDetector;
import com.suke.widget.SwitchButton;

public class SettingsActivity extends AppCompatActivity implements BottomSheetDialogReportBug.BottomSheetListener  {
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    SwitchButton txt_hide_balance_points2;
    SwitchButton txt_hide_profile3;
    SwitchButton txt_hide_comments3;
    SwitchButton txt_hide_reviews3;
    SwitchButton txt_new_promo_notifications3;
    SwitchButton txt_new_airdrop_notifications3;
    SwitchButton txt_new_message_notifications3;
    SwitchButton txt_new_comment_notifications3;
    SwitchButton txt_new_comment_reply_notifications3;
    SwitchButton txt_upadate_promos_notifications3;
    SwitchButton txt_upadate_airdrop_notifications3;
    SwitchButton txt_received_points_notifications3;
    SegmentedButtonGroup segmentedButtonGroup;
    ImageView arrow_back;
    Context context;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    public SettingsActivity() {
        // Required empty public constructor
    }

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBarDetail);
        super.onCreate(savedInstanceState);
        context = this;
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
        setContentView(R.layout.activity_settings);
            user2 = MyPreferenceManager.getInstance(mContext).getUser();

        arrow_back = findViewById(R.id.arrow_back);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
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
                    args.putString("source","SettingsActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////

        txt_hide_balance_points2 = findViewById(R.id.txt_hide_balance_points2);
        txt_hide_profile3 = findViewById(R.id.txt_hide_profile3);
        txt_hide_comments3 = findViewById(R.id.txt_hide_comments3);
        txt_hide_reviews3 = findViewById(R.id.txt_hide_reviews3);
        txt_new_promo_notifications3 = findViewById(R.id.txt_new_promo_notifications3);
        txt_new_airdrop_notifications3 = findViewById(R.id.txt_new_airdrop_notifications3);
        txt_new_message_notifications3 = findViewById(R.id.txt_new_message_notifications3);
        txt_new_comment_notifications3 = findViewById(R.id.txt_new_comment_notifications3);
        txt_new_comment_reply_notifications3 = findViewById(R.id.txt_new_comment_notifications3);
        txt_upadate_promos_notifications3 = findViewById(R.id.txt_upadate_promos_notifications3);
        txt_upadate_airdrop_notifications3 = findViewById(R.id.txt_upadate_airdrop_notifications3);
        txt_received_points_notifications3 = findViewById(R.id.txt_received_points_notifications3);


        txt_hide_balance_points2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });

        txt_hide_profile3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){

                    txt_hide_comments3.setChecked(true);
                    txt_hide_reviews3.setChecked(true);
                    txt_hide_comments3.setEnabled(false);//disable button
                    txt_hide_reviews3.setEnabled(false);//disable button

                }
                else{
                    txt_hide_comments3.setChecked(false);
                    txt_hide_reviews3.setChecked(false);
                    txt_hide_comments3.setEnabled(true);//disable button
                    txt_hide_reviews3.setEnabled(true);//disable button
                }



            }
        });

        txt_hide_comments3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
        txt_hide_reviews3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
        txt_new_promo_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
        txt_new_airdrop_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });

        txt_new_message_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });

        txt_new_comment_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });

        txt_new_comment_reply_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
        txt_upadate_promos_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
        txt_upadate_airdrop_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
        txt_received_points_notifications3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });




        segmentedButtonGroup = findViewById(R.id.buttonGroup_default_currency);
        segmentedButtonGroup.setPosition(1, false);
        segmentedButtonGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(final int position) {


            }
        });

     //   segmentedButtonGroup.getPosition();



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
    @Override
    public void onButtonClicked(String text) {

    }
}
