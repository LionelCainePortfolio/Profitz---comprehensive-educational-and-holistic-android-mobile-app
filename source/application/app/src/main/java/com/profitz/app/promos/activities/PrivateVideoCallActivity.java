package com.profitz.app.promos.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;

import com.profitz.app.R;import com.profitz.app.promos.CameraPreview;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.User;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;


public class PrivateVideoCallActivity extends PromosActivity {
    private ImageView userImage, back;

    private String userId;
    private static final int REQUEST_CAMERA = 104;
    private boolean cameraFront = false;

    private HttpManager httpManager;
    private CustomLoadingDialog customLoadingDialog;
    private User user;

    private static Camera mCamera = null;
    private CameraPreview mPreview;
    private LinearLayout cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goFullScreen();
        setContentView(R.layout.activity_private_video_call);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    PrivateVideoCallActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    REQUEST_CAMERA
            );
        } else {
            showMyCamera();
        }

        httpManager = new HttpManager(this);
        customLoadingDialog = new CustomLoadingDialog(this);
        customLoadingDialog.show();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllowed = true;
        for (int grantResult : grantResults) {
            if (grantResult < 0) {
                isAllowed = false;
                break;
            }
        }
        if (isAllowed) {
            if (requestCode == REQUEST_CAMERA) {
                showMyCamera();
            }
        } else {
        //    Toast.makeText(getApplicationContext(), "Permission denied to download", Toast.LENGTH_LONG).show();
            Toasty.error(getApplicationContext(), "Brak uprawnień by móc pobrać ten plik.").show();

        }
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        //Search for the back facing camera
        //get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        //for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    private void showMyCamera() {
        int cameraId = findFrontFacingCamera();
        if (cameraId >= 0) {
            mCamera = Camera.open(cameraId);
            mCamera.setDisplayOrientation(90);
            cameraPreview = findViewById(R.id.cPreview);
            mPreview = new CameraPreview(this, mCamera);
            cameraPreview.addView(mPreview);
            mCamera.startPreview();
        }
    }

    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        } else {
            Log.d("nu", "no null");
        }
    }

    private void goBack() {
        Intent intent = new Intent(PrivateVideoCallActivity.this, PrivateChatActivity.class);
        intent.putExtra("userId", userId);
        Utility.gotoActivity(PrivateVideoCallActivity.this, intent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
