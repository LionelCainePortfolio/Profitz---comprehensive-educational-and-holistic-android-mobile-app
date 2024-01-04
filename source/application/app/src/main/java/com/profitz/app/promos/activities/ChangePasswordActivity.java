package com.profitz.app.promos.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.profitz.app.R;import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.util.Utility;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;


public class ChangePasswordActivity extends AppCompatActivity implements  BottomSheetDialogReportBug.BottomSheetListener {
    private EditText password, new_password;
    private CustomLoadingDialog customLoadingDialog;
    private User user;
    private HttpManager httpManager;
    private MyPreferenceManager preferenceManager;
    private Button btnUpdate;
    private Context context;
    private String newPasswordText, passwordText;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        httpManager = new HttpManager(this);
        preferenceManager = new MyPreferenceManager(this);
        user = preferenceManager.getUser();
        context = this;
        password = findViewById(R.id.password);
        new_password = findViewById(R.id.new_password);
        btnUpdate = findViewById(R.id.btn_update);
        customLoadingDialog = new CustomLoadingDialog(ChangePasswordActivity.this);
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
                    args.putString("source","ChangePasswordActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {

                    passwordText = password.getText().toString().trim();
                    newPasswordText = new_password.getText().toString().trim();

                    if (!passwordText.isEmpty()) {
                       // user.setPassword(newPasswordText);

                        customLoadingDialog.show();
                        btnUpdate.setEnabled(false);

                        httpManager.updatePassword(passwordText, newPasswordText, new HttpResponse() {
                            @Override
                            public void onSuccess(String response) {
                                customLoadingDialog.dismiss();
                                btnUpdate.setEnabled(true);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if (status.equalsIgnoreCase("success")) {
                                        preferenceManager.setUserLogin(user);
                                    }
                              //      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    Toasty.normal(getApplicationContext(), message).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                customLoadingDialog.dismiss();
                                btnUpdate.setEnabled(true);
                               // Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                Toasty.error(getApplicationContext(), error).show();

                            }
                        });
                    }
                }
            }
        });
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
        goBack();
    }

    private void goBack() {
        User user2 = MyPreferenceManager.getInstance(this).getUser();

        String userId = user2.getId();
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("groupId", "");
        Utility.gotoActivity(this, intent);
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
