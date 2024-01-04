package com.profitz.app.promos.activities;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterNotification;
import com.profitz.app.promos.data.DataNotification;
import com.profitz.app.promos.fragments.BottomSheetDialogAllNotifications;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.squareup.seismic.ShakeDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

public class AwardsActivity extends AppCompatActivity implements BottomSheetDialogAllNotifications.BottomSheetListener, View.OnClickListener,  BottomSheetDialogReportBug.BottomSheetListener  {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVNotificationList;
    private AdapterNotification mAdapter;
    User user2;
    RecyclerView NotificationList;
    String count_notifications;
    String done_task_ids;
    Context context = this;
    LinearLayout linear_awards;
    TextView textEarn7;
    MenuItem helpItem;
    LinearLayout task1;
    LinearLayout task2;
    LinearLayout task3;
    RelativeLayout all_task_completed_rl;
    Dialog dialog3;
    ImageView arrow_back;
    ImageView help_button;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
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
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.gold_gradient_shape, null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.transparent));
        // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        setContentView( R.layout.activity_awards);
        user2 = MyPreferenceManager.getInstance(context).getUser();

        NotificationList = findViewById(R.id.NotificationList);
        mRVNotificationList = findViewById(R.id.NotificationList);
        LottieAnimationView animationView;
        dialog3 = new Dialog(context); // Context, this, etc.
        dialog3.setContentView(R.layout.dialog_disable_future);
        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        arrow_back = findViewById(R.id.arrow_back);
        help_button = findViewById(R.id.help_button);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        help_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // startActivity(new Intent(ReferFriendActivity.this,RefferalsActivity.class));

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
                    args.putString("source","AwardsActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        TextView click3 = (TextView) dialog3.findViewById(R.id.button_understand_disable_future);
        click3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog3.dismiss();
            }
        });
        animationView = dialog3.findViewById(R.id.anim_declined);

        animationView.setVisibility(View.VISIBLE);

        animationView
                .addAnimatorUpdateListener(
                        (animation) -> {
                            // Do something.

                        });
        animationView
                .playAnimation();
        animationView.setVisibility(View.VISIBLE);
        getData();
        task1 = findViewById(R.id.task1);
        task2 = findViewById(R.id.task2);
        task3 = findViewById(R.id.task3);
        task1.setOnClickListener((View.OnClickListener)this);
        task2.setOnClickListener((View.OnClickListener)this);
        task3.setOnClickListener((View.OnClickListener)this);
        all_task_completed_rl = findViewById(R.id.all_task_completed_rl);
        linear_awards = findViewById(R.id.linear_awards);
        TextView see_all_notifications_awards = findViewById(R.id.see_all_notifications_awards);
        see_all_notifications_awards.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogAllNotifications bottomSheet = new BottomSheetDialogAllNotifications();
                args.putString("notifType", "tasks");
                bottomSheet.setArguments(args);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });
        TextView want_rules = findViewById(R.id.textViewTerms2);
        want_rules.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String url_to_rules = "https://profitz.app/awards_program";
                Intent ir = new Intent(Intent.ACTION_VIEW);
                ir.setData(Uri.parse(url_to_rules));
                startActivity(ir);
            }
        });





    }


    public void openDialogTaskInfo(String dialog_task){
        final Dialog dialogTaskInfo = new Dialog(context); // Context, this, etc.
        dialogTaskInfo.setContentView(R.layout.dialog_task_info);
        dialogTaskInfo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView dialog_task_img = dialogTaskInfo.findViewById(R.id.dialog_task_img);
        TextView task_title = dialogTaskInfo.findViewById(R.id.taskTitle);
        TextView task_desc = dialogTaskInfo.findViewById(R.id.taskDesc);
        TextView task_desc2 = dialogTaskInfo.findViewById(R.id.taskDesc2);

        if (dialog_task.equals("task1")){
            dialog_task_img.setImageDrawable(getResources().getDrawable(R.drawable.task1, getApplicationContext().getTheme()));

            task_title.setText("Pierwsza promocja!");
            task_desc.setText("Wykonaj swoją pierwszą promocję w aplikacji Profitz, by ukończyć to zadanie!");
            task_desc2.setText("Aby ukończyć to zadanie wykonaj jakąkolwiek promocję, korzystając z instrukcji tej aplikacji i zgłoś to naciskając na przycisk 'ptaszka', który znajuje się w instrukcji. Po pozytywnym zweryfikowaniu przez administratorów wykonania przez Ciebie tej promocji - zadanie zostanie ukończone i otrzymasz 0.10 punktów. ");

        }
        else if (dialog_task.equals("task2")){
            dialog_task_img.setImageDrawable(getResources().getDrawable(R.drawable.task2, getApplicationContext().getTheme()));

            task_title.setText("Poleć i zyskaj!");
            task_desc.setText("Poleć aplikację Profitz znajomemu i zyskaj dodatkowe punkty!");
            task_desc2.setText("Aby ukończyć to zadanienw wykonania przez Ciebie tej promocji poleć znajomemu aplikację (udostęnij swój kod, który znajdziesz w zakładce 'Zaproś znajomego' i poporś by podał go podczas rejestracji). Gdy Twój znajomy wykona promocję za pomocą aplikacji Profitz, otrzymasz 10 punktów za polecenie, oraz zadanie zostanie ukończone i otrzymasz za nie 0.10 punktów. ");

        }
        else if (dialog_task.equals("task3")){
            dialog_task_img.setImageDrawable(getResources().getDrawable(R.drawable.task3, getApplicationContext().getTheme()));

            task_title.setText("Pozytywne odczucia!");
            task_desc.setText("Powiedz co myślisz o aplikacji Profitz, by ukończyć to zadanie.");
            task_desc2.setText("Przekaż opinię w sklepie Play, korzystając z przycisku 'Oceń aplikację', znajdującego się w bocznym menu aplikacji, by ukończyć to zadanie i otrzymać 0.10 punktów.");
        }

        Button click_dialogTaskInfo = (Button) dialogTaskInfo.findViewById(R.id.button_understand_dialogTaskInfo);
        click_dialogTaskInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogTaskInfo.dismiss();
            }
        });
        dialogTaskInfo.show();
    }


    public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.task1) {
                openDialogTaskInfo("task1");
            }
            else if (id == R.id.task2) {
                openDialogTaskInfo("task2");
            }
           else if (id == R.id.task3) {
                openDialogTaskInfo("task3");
            }
    }
    public void getData(){

        final String url2 = "https://yoururl.com/api/check_notification_count.php?type=awards&user_id="+user2.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            count_notifications = response.getString("count_notifications");
                            if (count_notifications.equals("0")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                linear_awards.setVisibility(View.GONE);

                            } else if (count_notifications.equals("1")) {

                                new AsyncAwards().execute();

                            }
                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest);

        final String url4 = "https://yoururl.com/api/get_done_tasks.php?user_id="+user2.getId();
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url4, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            done_task_ids= response.getString("task_ids");
                            if (done_task_ids.equals("1")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                task1.setVisibility(View.GONE);
                            }
                            else if (done_task_ids.equals("2")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                task2.setVisibility(View.GONE);
                            }
                           else if (done_task_ids.equals("3")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                task3.setVisibility(View.GONE);
                            }
                            else if (done_task_ids.equals("12")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                task1.setVisibility(View.GONE);
                                task2.setVisibility(View.GONE);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 0, 5, 0);

                            }
                            else if (done_task_ids.equals("13")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                task1.setVisibility(View.GONE);
                                task3.setVisibility(View.GONE);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 0, 5, 0);
                               /* HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(
                                        HorizontalScrollView.LayoutParams.WRAP_CONTENT,
                                        HorizontalScrollView.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(0, 0, 0, 0);
                                horizontal_sv.setLayoutParams(params);*/
                            }
                            else if (done_task_ids.equals("23")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                task2.setVisibility(View.GONE);
                                task3.setVisibility(View.GONE);

                            }
                            else if (done_task_ids.equals("123")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                task1.setVisibility(View.GONE);
                                task2.setVisibility(View.GONE);
                                task3.setVisibility(View.GONE);
                                all_task_completed_rl.setVisibility(View.VISIBLE);
                            }
    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
} catch (JSONException e) {
        e.printStackTrace();
        }

        }
        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        Volley.newRequestQueue(this).add(jsonObjectRequest2);
    }

    @Override
    public void onButtonClicked(String text) {

    }

    class AsyncAwards extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

        LayoutInflater factory = LayoutInflater.from(context);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(context);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //gif_loader.setVisibility(View.VISIBLE);
            main_dialog.setContentView(DialogView);
            main_dialog.setCancelable(false);
            main_dialog.setCanceledOnTouchOutside(false);

            main_dialog.show();
            //main_dialog.getWindow().setLayout(300, 300);
            main_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //this method will be running on UI thread
            //pdLoading.setMessage("\tŁadowanie...");
            //   pdLoading.setCancelable(false);
            //   pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {



            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                User user = MyPreferenceManager.getInstance(context).getUser();
                url = new URL("https://yoururl.com/api/get_notifications.php?user_id="+user2.getId()+"&limit=yes&notification_type=tasks");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            // gif_loader.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    main_dialog.dismiss();
                    handler.postDelayed(this, 1500);
                }
            }, 1000);


            // pdLoading.dismiss();
            List<DataNotification> data=new ArrayList<>();
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataNotification notificationData = new DataNotification();
                    notificationData.notificationId= json_data.getInt("notification_id");
                    notificationData.notificationTitle= json_data.getString("notification_title");
                    notificationData.notificationDescription= json_data.getString("notification_description");
                    notificationData.notificationEarn= json_data.getString("notification_earn");
                    notificationData.notificationEarnType= json_data.getString("notification_earn_type");
                    notificationData.notificationImage= json_data.getString("notification_img");
                    notificationData.notificationDateAdd= json_data.getString("notification_date_add");
                    data.add(notificationData);
                }

                // Setup and Handover data to recyclerview
                //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterNotification(context, data);
                mRVNotificationList.setAdapter(mAdapter);
                mRVNotificationList.setLayoutManager(new GridLayoutManager(context,1));

            } catch (JSONException e) {
             //   Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(context, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_awards_menu, menu);
        helpItem = menu.findItem(R.id.need_help_from_awards);

      /*  new Handler().post(new Runnable() {
            @Override
            public void run() {

                final View viewhelpItem = findViewById(R.id.need_help_from_awards);
                viewhelpItem.setPadding(0,0,15,0);
                ActionMenuView.LayoutParams params = new ActionMenuView.LayoutParams
                        (

                                ActionMenuView.LayoutParams.WRAP_CONTENT,
                                ActionMenuView.LayoutParams.WRAP_CONTENT
                        );

                params.setMargins(0, 0, 15, 0);
                viewhelpItem.setLayoutParams(params);
            }
        });
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.need_help_from_awards) {
            dialog3.show();
            //mMovieDetailPresenter.shareMovie();
            return true;
        }
        return super.onOptionsItemSelected(item);
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