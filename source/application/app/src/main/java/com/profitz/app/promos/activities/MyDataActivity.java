package com.profitz.app.promos.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogChangeEmailOrPhone;
import com.profitz.app.promos.fragments.BottomSheetDialogDisabledChangeEdit;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.BottomSheetDialogUpgradeLevel;
import com.squareup.seismic.ShakeDetector;

public class MyDataActivity extends AppCompatActivity implements BottomSheetDialogUpgradeLevel.BottomSheetListener, BottomSheetDialogDisabledChangeEdit.BottomSheetListener, BottomSheetDialogChangeEmailOrPhone.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener  {
    String username, name, lastname, email, phone, country;
    private Context mContext;
    TextView textViewNickname, textViewName, textViewLastname, textViewPhone, textViewEmail, textViewCountry;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    View view;
    ImageView editPhone;
    ImageView editEmail;
    ImageView editCountry;
    Context context;
    ImageView arrow_back;
    ImageView edit_name;
    ImageView edit_lastname;
    ImageView edit_username;
    ImageView edit_email;
    ImageView edit_phone;
    ImageView edit_country;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public MyDataActivity() {
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
                    args.putString("source","MyDataActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));
        getWindow().setBackgroundDrawableResource(R.drawable.white);
        setContentView(R.layout.activity_my_data);
            user2 = MyPreferenceManager.getInstance(mContext).getUser();

            textViewNickname = findViewById(R.id.username);
            textViewName = findViewById(R.id.name);
            textViewLastname = findViewById(R.id.lastname);
            textViewPhone = findViewById(R.id.phone);
            textViewEmail = findViewById(R.id.email);
            textViewCountry = findViewById(R.id.country);


            username = user2.getUsername();
            name = user2.getName();
            lastname = user2.getLastname();
            phone = user2.getPhone();
            email = user2.getEmail();
            country = user2.getCountry();


            textViewNickname.setText(username);
            textViewName.setText(name);
            textViewLastname.setText(lastname);
            textViewPhone.setText(phone);
            textViewEmail.setText(email);
            textViewCountry.setText(country);

            edit_name = findViewById(R.id.edit_name);
            edit_lastname = findViewById(R.id.edit_lastname);
            edit_username = findViewById(R.id.edit_username);
            edit_phone = findViewById(R.id.edit_phone);
            edit_email = findViewById(R.id.edit_email);
            edit_country = findViewById(R.id.edit_country);

        edit_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogDisabledChangeEdit bottomSheetDialogDisabledChangeEdit = new BottomSheetDialogDisabledChangeEdit();
                args.putString("source","editName");
                bottomSheetDialogDisabledChangeEdit.setArguments(args);
                bottomSheetDialogDisabledChangeEdit.show(getSupportFragmentManager(), bottomSheetDialogDisabledChangeEdit.getTag());
            }
        });

        edit_lastname.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogDisabledChangeEdit bottomSheetDialogDisabledChangeEdit = new BottomSheetDialogDisabledChangeEdit();
                args.putString("source","editLastname");
                bottomSheetDialogDisabledChangeEdit.setArguments(args);
                bottomSheetDialogDisabledChangeEdit.show(getSupportFragmentManager(), bottomSheetDialogDisabledChangeEdit.getTag());
            }
        });

        edit_username.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogDisabledChangeEdit bottomSheetDialogDisabledChangeEdit = new BottomSheetDialogDisabledChangeEdit();
                args.putString("source","editUsername");
                bottomSheetDialogDisabledChangeEdit.setArguments(args);
                bottomSheetDialogDisabledChangeEdit.show(getSupportFragmentManager(), bottomSheetDialogDisabledChangeEdit.getTag());
            }
        });

        edit_email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogChangeEmailOrPhone bottomSheetDialogChangeEmailOrPhone = new BottomSheetDialogChangeEmailOrPhone();
                args.putString("source","editEmail");
                bottomSheetDialogChangeEmailOrPhone.setArguments(args);
                bottomSheetDialogChangeEmailOrPhone.show(getSupportFragmentManager(), bottomSheetDialogChangeEmailOrPhone.getTag());
            }
        });

        edit_phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogChangeEmailOrPhone bottomSheetDialogChangeEmailOrPhone = new BottomSheetDialogChangeEmailOrPhone();
                args.putString("source","editPhone");
                bottomSheetDialogChangeEmailOrPhone.setArguments(args);
                bottomSheetDialogChangeEmailOrPhone.show(getSupportFragmentManager(), bottomSheetDialogChangeEmailOrPhone.getTag());
            }
        });

        edit_country.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogDisabledChangeEdit bottomSheetDialogDisabledChangeEdit = new BottomSheetDialogDisabledChangeEdit();
                args.putString("source","editCountry");
                bottomSheetDialogDisabledChangeEdit.setArguments(args);
                bottomSheetDialogDisabledChangeEdit.show(getSupportFragmentManager(), bottomSheetDialogDisabledChangeEdit.getTag());
            }
        });


        arrow_back = findViewById(R.id.arrow_back);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

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
