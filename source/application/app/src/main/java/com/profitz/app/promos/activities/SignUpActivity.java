package com.profitz.app.promos.activities;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.RequestHandler;
import com.profitz.app.promos.ShakeDetector;
import com.profitz.app.promos.URLS;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogAdminUser;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.BottomSheetDialogTerms;
import com.profitz.app.promos.fragments.SignUpEightFragment;
import com.profitz.app.promos.fragments.SignUpFifthFragment;
import com.profitz.app.promos.fragments.SignUpFinishFragment;
import com.profitz.app.promos.fragments.SignUpSixthFragment;
import com.profitz.app.promos.fragments.SignUpFirstFragment;
import com.profitz.app.promos.fragments.SignUpFourthFragment;
import com.profitz.app.promos.fragments.SignUpSecondFragment;
import com.profitz.app.promos.fragments.SignUpSeventhFragment;
import com.profitz.app.promos.fragments.SignUpThirdFragment;
import com.profitz.app.promos.fragments.SignUpZeroFragment;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity implements OnEditTextChanged, BottomSheetDialogTerms.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener {
    EditText editTextUsername, editTextEmail, editTextPassword;
    int actualstep = 0;
    String dat0;
    String dat1;
    String dat12;
    String dat2;
    String dat3;
    String dat4;
    String dat5;
    String dat51;
    String dat6;
    String dat7;
    String dat81;
    String dat84;
    Context context = this;
    Dialog main_dialog;
    View DialogView;
    LayoutInflater factory;
    SignUpZeroFragment zero;
    SignUpFirstFragment first;
    SignUpSecondFragment second;
    SignUpThirdFragment third;
    SignUpFourthFragment fourth;
    SignUpFifthFragment fifth;
    SignUpSixthFragment sixth;
    SignUpSeventhFragment seventh;
    SignUpEightFragment eight;
    SignUpFinishFragment finish;
    public BottomSheetDialogReportBug report_dialog;
    public static SignUpActivity instance;
    static Button registerButton;
    static Button nextButton;
    static Button startButton;

    Button backButton;
    RelativeLayout global_signup_rl;
    private Swipe swipe;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_Profitz));

        View decorView = getWindow().getDecorView();

        setContentView(R.layout.activity_sign_up);
        // ShakeDetector initialization


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
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
                    args.putString("source","SignUpActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        swipe = new Swipe();
        swipe.setListener(new SwipeListener() {
            @Override public void onSwipingLeft(final MotionEvent event) {
            }

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                nextFragment();

                return false;
            }

            @Override
            public void onSwipingRight(MotionEvent event) {

            }

            @Override public boolean onSwipedRight(final MotionEvent event) {
                prevFragment();

                return false;
            }

            @Override
            public void onSwipingUp(MotionEvent event) {

            }

            @Override
            public boolean onSwipedUp(MotionEvent event) {
                return false;
            }

            @Override
            public void onSwipingDown(MotionEvent event) {

            }

            @Override
            public boolean onSwipedDown(MotionEvent event) {
                return false;
            }

        });


        registerButton = (Button) findViewById(R.id.btn_register);
        backButton = (Button) findViewById(R.id.textViewBack);
        nextButton = (Button) findViewById(R.id.next_step);
        startButton = (Button) findViewById(R.id.start_step);

        factory = LayoutInflater.from(SignUpActivity.this);
        DialogView = factory.inflate(R.layout.custom_load_layout, null);
        main_dialog = new Dialog(SignUpActivity.this);

        zero = new SignUpZeroFragment();
        first = new SignUpFirstFragment();
        second = new SignUpSecondFragment();
        third = new SignUpThirdFragment();
        fourth = new SignUpFourthFragment();
        fifth = new SignUpFifthFragment();
        sixth = new SignUpSixthFragment();
        Bundle arguments = new Bundle();
        arguments.putString( "phone_number" , dat5);
        sixth.setArguments(arguments);
        seventh = new SignUpSeventhFragment();
        eight = new SignUpEightFragment();
        finish = new SignUpFinishFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        changeStatusBarColor();

        global_signup_rl = findViewById(R.id.global_signup_rl);

        if (actualstep == 0) {
            fragmentTransaction.replace(R.id.layout, zero);
            fragmentTransaction.commit();

        }

        editTextUsername = findViewById(R.id.editTextUsername);
       // editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Context context = this;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

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
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actualstep==0)
                {
                    finish();
                    startActivity(new Intent(SignUpActivity.this, WelcomeActivity.class));
                }
                if (actualstep==1)
                {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, zero);
                    fragmentTransaction.commit();

                }
                if (actualstep == 2) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, first);
                    fragmentTransaction.commit();


                    actualstep = 1;
                } else if (actualstep == 3) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, second);
                    fragmentTransaction.commit();
                    actualstep = 2;
                } else if (actualstep == 4) {
                    nextButton.setText("Kontynuuj");

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, third);
                    fragmentTransaction.commit();
                    actualstep = 3;
                } else if (actualstep == 5) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, fourth);
                    fragmentTransaction.commit();
                    actualstep = 4;
                } else if (actualstep == 6) {
                    nextButton.setText("Dodaj numer telefonu");

                    registerButton.setVisibility(View.GONE);

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, fifth);
                    fragmentTransaction.commit();
                    actualstep = 5;
                }
                else if (actualstep == 7) {
                    nextButton.setText("Zweryfikuj numer");

                    registerButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, sixth);
                    fragmentTransaction.commit();
                    actualstep = 6;
                }
                else if (actualstep == 8) {
                    nextButton.setText("Pomiń");

                    registerButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                            R.anim.slide_right_in, R.anim.slide_left_out);
                    fragmentTransaction.replace(R.id.layout, seventh);
                    fragmentTransaction.commit();
                    actualstep = 7;
                }
                else if (actualstep == 9){

                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextFragment();


            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    registerUser();

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runPromosActivity();
            }
        });
    }
    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }



public void prevFragment(){

    if (actualstep == 1) {
        nextButton.setText("Kontynuuj");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, zero);
        fragmentTransaction.commit();


        actualstep = 0;
    }
    else if (actualstep == 2) {
        nextButton.setText("Kontynuuj");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, first);
        fragmentTransaction.commit();


        actualstep = 1;
    } else if (actualstep == 3) {
        nextButton.setText("Kontynuuj");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, second);
        fragmentTransaction.commit();
        actualstep = 2;
    } else if (actualstep == 4) {
        nextButton.setText("Kontynuuj");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, third);
        fragmentTransaction.commit();
        actualstep = 3;
    } else if (actualstep == 5) {
        nextButton.setText("Kontynuuj");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, fourth);
        fragmentTransaction.commit();
        actualstep = 4;
    } else if (actualstep == 6) {
        nextButton.setText("Dodaj numer telefonu");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, fifth);
        fragmentTransaction.commit();
        actualstep = 5;
    }
    else if (actualstep == 7) {
        nextButton.setText("Zweryfikuj numer");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, sixth);
        fragmentTransaction.commit();
        actualstep = 6;
    }
    else if (actualstep == 8) {
        nextButton.setText("Zatwierdź kod");
        nextButton.setVisibility(View.VISIBLE);

        registerButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out,
                R.anim.slide_right_in, R.anim.slide_left_out);
        fragmentTransaction.replace(R.id.layout, seventh);
        fragmentTransaction.commit();
        actualstep = 7;
    }
    else if (actualstep == 9){

    }
}
    public void nextFragment() {
        if (actualstep == 0) {
            nextButton.setText("Kontynuuj");

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                fragmentTransaction.replace(R.id.layout, first);
                fragmentTransaction.commit();
                actualstep = 1;
        }
        else if (actualstep == 1) {

            if (TextUtils.isEmpty(dat1)) {

                first.setData("step_1_empty");
            } else if (dat1.equals("wrong_name")) {
                first.setData("wrong_name");
            }
            else if (dat1.equals("wrong_name_signs")) {
                first.setData("wrong_name_signs");
            }
            else if (TextUtils.isEmpty(dat12)){
                first.setData("step_1_lastname_empty");
            }
            else if (dat12.equals("wrong_last_name")) {
                first.setData("wrong_last_name");
            }
            else if (dat12.equals("wrong_lastname_signs")) {
                first.setData("wrong_lastname_signs");

            }
            else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                fragmentTransaction.replace(R.id.layout, second);
                fragmentTransaction.commit();
                actualstep = 2;
                nextButton.setText("Kontynuuj");

            }
        } else if (actualstep == 2) {

            if (TextUtils.isEmpty(dat2)) {
                second.setData("step_2_empty");
            } else if (dat2.equals("wrong_email")) {
                second.setData("wrong_email");
            } else if (dat2.equals("wrong_email_db")) {
                second.setData("wrong_email_db");
            } else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                fragmentTransaction.replace(R.id.layout, third);
                fragmentTransaction.commit();
                actualstep = 3;
                nextButton.setText("Kontynuuj");

            }

        } else if (actualstep == 3) {

            if (TextUtils.isEmpty(dat3)) {
                third.setData("step_3_empty");
            } else if (dat3.equals("wrong_username")) {
                third.setData("wrong_username");
            } else if (dat3.equals("wrong_username_name")) {
                third.setData("wrong_username_name");
            } else if (dat3.equals("wrong_username_max")) {
                third.setData("wrong_username_max");
            } else if (dat3.equals("wrong_username_signs")) {
                third.setData("wrong_username_signs");

            } else if (dat3.equals("wrong_username_db")) {
                third.setData("wrong_username_db");
            } else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                fragmentTransaction.replace(R.id.layout, fourth);
                fragmentTransaction.commit();
                actualstep = 4;
                nextButton.setText("Kontynuuj");

            }

        } else if (actualstep == 4) {

            if (TextUtils.isEmpty(dat4)) {
                fourth.setData("step_4_empty");
            } else if (dat4.equals("wrong_password")) {
                fourth.setData("wrong_password");
            } else {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                fragmentTransaction.replace(R.id.layout, fifth);
                fragmentTransaction.commit();
                actualstep = 5;
                nextButton.setText("Dodaj numer telefonu");

            }
        }
            else if (actualstep == 5) {
            if (TextUtils.isEmpty(dat5)) {
                    fifth.setData("step_5_empty");
                } else if (dat5.equals("wrong_number")) {
                    fifth.setData("wrong_number");
                }
            else if (dat5.equals("wrong_phone_db")){
                fifth.setData("wrong_phone_db");
            }
            else {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.addToBackStack(null);


                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                            R.anim.slide_left_in, R.anim.slide_right_out);
                    fragmentTransaction.replace(R.id.layout, sixth);
                    fragmentTransaction.commit();
                    actualstep = 6;
                nextButton.setText("Zweryfikuj numer");

            }


        } else if (actualstep == 6) {

            if (dat6.equals("wrong_code")) {
                sixth.setData("wrong_code");
            } else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                fragmentTransaction.replace(R.id.layout, seventh);
                fragmentTransaction.commit();
                actualstep = 7;
                nextButton.setText("Zatwierdź kod");


            }

        }
        else if (actualstep == 7) {

            if (dat7.equals("wrong_code")) {
                seventh.setData("wrong_code");
            } else {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                        R.anim.slide_left_in, R.anim.slide_right_out);
                fragmentTransaction.replace(R.id.layout, eight);
                fragmentTransaction.commit();
                actualstep = 8;
                nextButton.setText("Zarejestruj się");
                nextButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);


            }

        }

    }
    public static void visibleButton(){
        nextButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
    }
    public static void hideButton(){
        registerButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);

    }
    private void registerUser() {
        final String name = dat1;
        final String lastname = dat12;
        final String email = dat2;
        final String username = dat3;
        final String password = dat4;
        final String phone_number = dat5;
        final String country = dat0;
        final String phone_number_mVerificationId = dat6;
        final String refferal_code = dat7;


        RegisterUser ru = new RegisterUser(name, lastname, email, username, password, phone_number, country, phone_number_mVerificationId, refferal_code);
         ru.execute();
    }

    @Override
    public void onButtonClicked(String text) {

    }

    private class RegisterUser extends AsyncTask<Void, Void, String> {
        private ProgressBar progressBar;
        private final String name;
        private final String lastname;

        private final String email;
        private final String username;
        private final String password;
        private final String phone_number;
        private final String country;
        private final String phone_number_mVerificationId;
        private final String refferal_code;
        RegisterUser(String name, String lastname,String email, String username,  String password, String phone_number, String country, String phone_number_mVerificationId, String refferal_code){
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.username = username;
            this.password = password;
            this.phone_number = phone_number;
            this.country = country;
            this.phone_number_mVerificationId = phone_number_mVerificationId;
            this.refferal_code = refferal_code;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressBar = findViewById(R.id.progressBar);
           // progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("lastname", lastname);
            params.put("email", email);
            params.put("username", username);
            params.put("password", password);
            params.put("phone_number", phone_number);
            params.put("country", country);
            params.put("phone_number_mVerificationId", phone_number_mVerificationId);
            params.put("refferal_code", refferal_code);
            //returing the responsecountry
            return requestHandler.sendPostRequest(URLS.URL_REGISTER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           //Log.i("SignUp","sfdsds : "+s);
            //hiding the progressbar after completion
           // progressBar.setVisibility(View.GONE);
            try {
                main_dialog.setContentView(DialogView);
                main_dialog.setCancelable(false);
                main_dialog.setCanceledOnTouchOutside(false);

                main_dialog.show();
                //main_dialog.getWindow().setLayout(300, 300);
                main_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //converting response to json object
                JSONObject obj = new JSONObject(s);
               // Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(),refferal_code, Toast.LENGTH_SHORT).show();
                //if no error in response
                if (!obj.getBoolean("error")) {
                   // Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                    //Log.i("TAGG", String.valueOf(user));
                    //storing the user in shared preferences
                    MyPreferenceManager.getInstance(getApplicationContext()).setUserLogin(user);
                    //starting the profile activity
                    main_dialog.dismiss();

                    showConfetti();


                } else {
                    main_dialog.dismiss();
                    Log.d("error", obj.getString("message"));
                    //Toast.makeText(getApplicationContext(), "Wystąpił błąd. Sprawdź podane dane i spróbuj ponownie.", Toast.LENGTH_SHORT).show();
                    Toasty.error(getApplicationContext(), "Wystąpił błąd. Sprawdź podane dane i spróbuj ponownie.").show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void skip(){
        nextButton.setText("Pomiń");

    }
    public void approve_code(){
        nextButton.setText("Zatwierdź kod");

    }

    public void showConfetti(){

        global_signup_rl.setBackgroundResource(R.drawable.white_full);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out);
        fragmentTransaction.replace(R.id.layout, finish);
        fragmentTransaction.commit();
        actualstep = 9;
        startButton.setText("Rozpocznij");
        nextButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);

    }

   public void runPromosActivity(){
       finish();
       startActivity(new Intent(getApplicationContext(), PromosActivity.class));
   }

    @Override
    public void onEditPressed0(String edit0) {
        dat0=edit0;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed1(String edit1) {
            dat1=edit1;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
            //bread.show();
    }
    @Override
    public void onEditPressed12(String edit12) {
        dat12=edit12;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed2(String edit2) {
        dat2=edit2;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed3(String edit3) {
        dat3=edit3;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed4(String edit4) {
        dat4=edit4;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed5(String edit5) {
        dat5=edit5;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed51(String edit51) {
        dat51=edit51;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }

    public void phoneVerified(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out);
        fragmentTransaction.replace(R.id.layout, seventh);
        fragmentTransaction.commit();
        actualstep = 7;
        nextButton.setText("Pomiń");

    }

    public String getPhoneNumberString() {
        return dat5;
    }
    public String getPhoneNumberStringFormatted() {
        return dat51;
    }
    @Override
    public void onEditPressed6(String edit6) {
        dat6=edit6;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed7(String edit7) {
        dat7=edit7;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }

    @Override
    public void onEditPressed81(String edit81) {
        dat81=edit81;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    @Override
    public void onEditPressed84(String edit84) {
        dat84=edit84;
        // Toast bread = Toast.makeText(getApplicationContext(), dat1, Toast.LENGTH_LONG);
        //bread.show();
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
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
    protected void onDestroy() {
        super.onDestroy();
        instance=null;
    }

}
