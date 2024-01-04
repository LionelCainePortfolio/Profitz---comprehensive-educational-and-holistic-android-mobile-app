package com.profitz.app.promos.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.profitz.app.R;import com.profitz.app.data.model.GroupDetailModel;
import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.interfaces.HttpResponse;
import com.profitz.app.promos.managers.HttpManager;
import com.profitz.app.util.Utility;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class EditGroupActivity extends AppCompatActivity implements  BottomSheetDialogReportBug.BottomSheetListener  {
    private String groupId;
    private EditText groupName;
    private Button editGroup;
    private ImageView image;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private HttpManager httpManager;
    private GroupDetailModel groupDetailModel;
    private CustomLoadingDialog customLoadingDialog;

    private Uri filePath;
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_group);

        httpManager = new HttpManager(this);
        groupDetailModel = new GroupDetailModel();
        customLoadingDialog = new CustomLoadingDialog(EditGroupActivity.this);
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
                    args.putString("source","EditGroupActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        groupName = findViewById(R.id.groupName);
        image = findViewById(R.id.image);
        editGroup = findViewById(R.id.editGroup);
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customLoadingDialog.show();
                httpManager.editGroup(groupId, groupName.getText().toString(), Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture(), new HttpResponse() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String filePath = jsonObject.getString("file_path");
                            groupDetailModel.getDetails().setPicture(filePath);
                           // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Toasty.normal(getApplicationContext(), message).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        customLoadingDialog.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        Log.i("my_log", error);
                    }
                });
            }
        });

        if (getIntent() != null) {
            groupId = getIntent().getStringExtra("groupId");
            customLoadingDialog.show();
            httpManager.getGroupMetaDetail(groupId, new HttpResponse() {
                @Override
                public void onSuccess(String response) {
                    groupDetailModel = new Gson().fromJson(response, GroupDetailModel.class);
                    groupName.setText(groupDetailModel.getDetails().getGroup_name());
                    Utility.showImage(EditGroupActivity.this,Utility.BASE_URL + "/" + groupDetailModel.getDetails().getPicture(), image);
                    customLoadingDialog.dismiss();
                }

                @Override
                public void onError(String error) {
                    //
                }
            });
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
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
        Intent intent = new Intent(getApplicationContext(), GroupMetaDetailActivity.class);
        intent.putExtra("groupId", groupId);
        Utility.gotoActivity(EditGroupActivity.this, intent);
    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Wybierz zdjęcie grupy"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                groupDetailModel.getDetails().setPicture(Utility.getStringImage(bitmap));
                byte[] bytesImage = Base64.decode(Utility.BASE_URL + "/" +groupDetailModel.getDetails().getPicture(), Base64.DEFAULT);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(bytesImage)
                        .placeholder(R.drawable.placeholder_logo)
                        .into(image);
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(), "Błąd: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Toasty.error(getApplicationContext(), "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

                e.printStackTrace();
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

    @Override
    public void onButtonClicked(String text) {

    }
}
