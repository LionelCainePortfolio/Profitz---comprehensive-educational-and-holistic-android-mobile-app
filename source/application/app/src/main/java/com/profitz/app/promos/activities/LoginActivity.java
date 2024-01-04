package com.profitz.app.promos.activities;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.profitz.app.R;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.RequestHandler;
import com.profitz.app.promos.URLS;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements BottomSheetDialogReportBug.BottomSheetListener {

    EditText editTextUsername, editTextPassword;
    Dialog main_dialog;
    View DialogView;
    LayoutInflater factory;
    Context context;
    RelativeLayout global_login;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);
        View decorView = getWindow().getDecorView();

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_Profitz));

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

       getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));

        changeStatusBarColor();
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
                    args.putString("source","LoginActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        setContentView( R.layout.activity_login);
        global_login = findViewById(R.id.global_login);
        factory = LayoutInflater.from(LoginActivity.this);
        DialogView = factory.inflate(R.layout.custom_load_layout, null);
        main_dialog = new Dialog(LoginActivity.this);
        context = this;
        init();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    void init(){
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        //if user presses on login calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });
    }
    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Wprowadź login");
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Wprowadź hasło");
            editTextPassword.requestFocus();
            return;
        }
        //if everything is fine
        UserLogin ul = new UserLogin(username,password);
        ul.execute();
    }

    @Override
    public void onButtonClicked(String text) {

    }

    class UserLogin extends AsyncTask<Void, Void, String> {
       // ProgressBar progressBar;
        String username, password;
        UserLogin(String username,String password) {
            this.username = username;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressBar = findViewById(R.id.progressBar);
            //progressBar.setVisibility(View.VISIBLE);
        }//
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);
                Log.d("sww", String.valueOf(obj));
                //if no error in response
                if (!obj.getBoolean("error")) {
                    main_dialog.setContentView(DialogView);
                    main_dialog.setCancelable(false);
                    main_dialog.setCanceledOnTouchOutside(false);

                    main_dialog.show();
                    //main_dialog.getWindow().setLayout(300, 300);
                    main_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    //creating a new user object
                    User user = new User(
                            userJson.getInt("id"),
                            userJson.getString("name"),
                            userJson.getString("lastname"),
                            userJson.getString("username"),
                            userJson.getString("email"),
                            userJson.getInt("favourite_count"),
                            userJson.getInt("done_count"),
                            userJson.getDouble("points"),
                            userJson.getDouble("earned"),
                            userJson.getInt("power_level"),
                            userJson.getInt("premium"),
                            userJson.getBoolean("is_ban"),
                            userJson.getBoolean("is_online"),
                            userJson.getInt("unread_messages"),
                            userJson.getString("phone"),
                            userJson.getString("country"),
                            userJson.getString("fcm_token"),
                            userJson.getString("last_scene"),
                            userJson.getString("image_url"),
                            userJson.getString("last_msg"),
                            userJson.getString("last_msg_date"),
                            userJson.getString("last_msg_sent_by_me")
                            );
                    //storing the user in shared preferences
                    MyPreferenceManager.getInstance(getApplicationContext()).setUserLogin(user);
                    //starting the profile activity
                    //finish();
                    main_dialog.dismiss();

                    InputMethodManager inputMethodManager= null;
                    inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);

                    startActivity(new Intent(getApplicationContext(), PromosActivity.class));

                } else {
                    main_dialog.dismiss();
                    InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

                 //   Snackbar snackbar = Snackbar.make(global_login, Html.fromHtml("<font background-color=\"#ff1f1f\" color=\"#ffffff\">Nieprawidłowy login lub hasło.</font>"), Snackbar.LENGTH_LONG);
                 //   snackbar.show();
                    //Toast.makeText(context, "Nieprawidłowy login lub hasło", Toast.LENGTH_SHORT).show();
                    Toasty.error(context, "Nieprawidłowy login lub hasło.").show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_LOGIN, params);
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
