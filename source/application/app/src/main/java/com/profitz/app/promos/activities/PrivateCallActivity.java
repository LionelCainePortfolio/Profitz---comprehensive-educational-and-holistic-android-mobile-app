package com.profitz.app.promos.activities;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.profitz.app.R;import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;

import com.skyfishjy.library.RippleBackground;
import com.squareup.seismic.ShakeDetector;

public class PrivateCallActivity extends AppCompatActivity implements ShakeDetector.Listener, BottomSheetDialogReportBug.BottomSheetListener {
    private ImageView userImage, back;
    private String userId;

    private HttpManager httpManager;
    private CustomLoadingDialog customLoadingDialog;
    private User user;
    public BottomSheetDialogReportBug report_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //goFullScreen();
        setContentView(R.layout.private_call_activity);

        httpManager = new HttpManager(this);
        customLoadingDialog = new CustomLoadingDialog(this);
        customLoadingDialog.show();
        /////Shake to report bug
        report_dialog = new BottomSheetDialogReportBug();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        int sensorDelay = SensorManager.SENSOR_DELAY_GAME;
        sd.start(sensorManager, sensorDelay);

        //////
        RippleBackground rippleBackground = findViewById(R.id.rippleBackground);
        rippleBackground.startRippleAnimation();

        userImage = findViewById(R.id.userImage);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        if (getIntent() != null) {
            userId = getIntent().getStringExtra("userId");
            httpManager.getUserProfile(userId, new HttpResponse() {
                @Override
                public void onSuccess(String response) {
                    customLoadingDialog.dismiss();
                    user = new Gson().fromJson(response, User.class);

                    Utility.showImage(getApplicationContext(), Utility.BASE_URL + "/" + user.getPicture(), userImage);
                }

                @Override
                public void onError(String error) {
                    //
                }
            });
        }
    }

    private void goBack() {
        Intent intent = new Intent(PrivateCallActivity.this, PrivateChatActivity.class);
        intent.putExtra("userId", userId);
        Utility.gotoActivity(PrivateCallActivity.this, intent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
    @Override public void hearShake() {
        report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
