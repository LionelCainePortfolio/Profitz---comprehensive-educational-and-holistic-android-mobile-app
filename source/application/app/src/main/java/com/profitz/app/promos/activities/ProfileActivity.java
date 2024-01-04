package com.profitz.app.promos.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.profitz.app.R;import com.profitz.app.promos.CustomLoadingDialog;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
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

/

public class ProfileActivity extends AppCompatActivity implements BottomSheetDialogReportBug.BottomSheetListener {
    private Uri filePath;
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 71;

    private HttpManager httpManager;
    private MyPreferenceManager preferenceManager;
    private User user;
    private CustomLoadingDialog customLoadingDialog;

    private ImageView image, removePhoto;
    private Button btnUpdate;
    private TextView nameTv;

    private EditText name, phone, email;
    private String userId, groupId, fromAddContact;
    private boolean isMyProfile;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        httpManager = new HttpManager(this);
        customLoadingDialog = new CustomLoadingDialog(this);
        preferenceManager = new MyPreferenceManager(this);

        image = findViewById(R.id.image);
        removePhoto = findViewById(R.id.removePhoto);
        btnUpdate = findViewById(R.id.btn_update);
        nameTv = findViewById(R.id.username);

        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(getApplicationContext())
                        .load(R.drawable.user_placeholder)
                        .into(image);
                user.setPicture("-1");
            }
        });

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
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
                    args.putString("source","ProfileActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        if (getIntent() != null) {
            userId = getIntent().getStringExtra("userId");
            groupId = getIntent().getStringExtra("groupId");
            fromAddContact = getIntent().getStringExtra("fromAddContact");
            isMyProfile = userId.equalsIgnoreCase(String.valueOf(preferenceManager.getUser().getId()));
            if (isMyProfile) {
                user = preferenceManager.getUser();
                showUserDetail();
            } else {
                removePhoto.setVisibility(View.GONE);
                getUserDetail();
            }
        }

        if (!isMyProfile) {
            btnUpdate.setVisibility(View.GONE);
            removePhoto.setVisibility(View.GONE);
            name.setEnabled(false);
            phone.setEnabled(false);
            email.setEnabled(false);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    final String nameStr = name.getText().toString();
                    final String phoneStr = phone.getText().toString();
                    final String emailStr = email.getText().toString();

                    if (Utility.isEmpty(nameStr) || Utility.isEmpty(phoneStr)) {
                   //     Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                        Toasty.error(getApplicationContext(), "Uzupełnij wszystkie pola.").show();

                    } else {
                        customLoadingDialog.show();
                        btnUpdate.setEnabled(false);

                        user.setName(nameStr);
                        user.setPhone(phoneStr);
                        user.setEmail(emailStr);

                        httpManager.updateProfile(user, new HttpResponse() {
                            @Override
                            public void onSuccess(String response) {
                                customLoadingDialog.dismiss();
                                btnUpdate.setEnabled(true);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String filePath = jsonObject.getString("file_path");
                                    user.setPicture(filePath);

                                    preferenceManager.setUserLogin(user);
                                    nameTv.setText(user.getName());
                                    //Toast.makeText(getApplicationContext(), "Profile has been updated", Toast.LENGTH_LONG).show();
                                    Toasty.success(getApplicationContext(), "Profil został zaaktualizowany.").show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                customLoadingDialog.dismiss();
                                btnUpdate.setEnabled(true);
                            }
                        });
                    }
                } else {
                  //  Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    Toasty.error(getApplicationContext(), "Hasła nie są takie same.").show();

                    customLoadingDialog.dismiss();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMyProfile) {
                    choosePicture();
                }
            }
        });
    }

    private void getUserDetail() {
        customLoadingDialog.show();
        httpManager.getUserProfile(userId, new HttpResponse() {
            @Override
            public void onSuccess(String response) {
                customLoadingDialog.dismiss();
                user = new Gson().fromJson(response, User.class);
                showUserDetail();
            }

            @Override
            public void onError(String error) {
                customLoadingDialog.dismiss();
            }
        });
    }

    private void showUserDetail() {
        nameTv.setText(user.getName());
        name.setText(user.getName());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());
        Utility.showImage(ProfileActivity.this, Utility.BASE_URL + "/" + user.getPicture(), image);
    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                user.setPicture(Utility.getStringImage(bitmap));
                byte[] bytesImage = Base64.decode(user.getPicture(), Base64.DEFAULT);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(bytesImage)
                        .placeholder(R.drawable.user_placeholder)
                        .into(image);
            } catch (IOException e) {
              //  Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Toasty.error(getApplicationContext(), "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;

            case R.id.item_change_password:
                Utility.gotoActivity(ProfileActivity.this, ChangePasswordActivity.class);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        if (!Utility.isEmpty(fromAddContact)) {
            Utility.gotoActivity(this, AddUserInContactListActivity.class);
        } else {
            if (groupId.isEmpty()) {
                Utility.gotoActivity(this, ChatsActivity.class);
            } else {
                Intent intent = new Intent(getApplicationContext(), GroupMetaDetailActivity.class);
                intent.putExtra("groupId", groupId);
                Utility.gotoActivity(this, intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMyProfile) {
            getMenuInflater().inflate(R.menu.profile_activity, menu);
        }
        return true;
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
