package com.profitz.app.promos.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class RefferalsActivity extends AppCompatActivity implements BottomSheetDialogReportBug.BottomSheetListener  {
Context context = this;
    String data_getRefferal_count;
    String data_getRefferal_success_count;
    String data_getRefferal_earn;
    double user_id;
    String username;
    TextView refferal_count;
    TextView refferal_success_count;
    TextView refferal_earn;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_refferals);
        setTitle("Partnerzy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refferal_count = findViewById(R.id.pending_refferals);
        refferal_success_count = findViewById(R.id.success_refferals);
        refferal_earn = findViewById(R.id.tx2);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container_refferals);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        getData();
                    }
                }
        );
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
                    args.putString("source","RefferalsActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        TextView learn_more = findViewById(R.id.tx4);
        learn_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String url_to_rules = "https://profitz.app/invite_friends";
                Intent ir = new Intent(Intent.ACTION_VIEW);
                ir.setData(Uri.parse(url_to_rules));
                startActivity(ir);
            }
        });
        getData();
    }
    public void getData(){
        mSwipeRefreshLayout.setRefreshing(false);
        User user = MyPreferenceManager.getInstance(this).getUser();
        final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            DecimalFormat df = new DecimalFormat("##.00");
                            data_getRefferal_count = response.getString("refferal_count");
                            data_getRefferal_success_count= response.getString("refferal_success_count");
                            data_getRefferal_earn = response.getString("refferal_earn");
                            if (data_getRefferal_earn.equals("0"))
                            {
                                refferal_earn.setText("0.00");

                            }
                            else {
                                double data_getRefferal_earn_double = Double.parseDouble(data_getRefferal_earn);
                                //  username = response.getString("username");
                                // String user_id_string = response.getString("user_id");
                                //user_id = Double.parseDouble(user_id_string);
                                double new_count = (data_getRefferal_earn_double/10)*4.29;
                                String decimal_data_getRefferal_earn_string = df.format(new_count);
                                refferal_earn.setText(decimal_data_getRefferal_earn_string);

                            }

                            refferal_count.setText(data_getRefferal_count);
                            refferal_success_count.setText(data_getRefferal_success_count);

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
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public void setTitle(String title){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        // textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        textView.setGravity(Gravity.CENTER);
        params.setMargins(0,0,50,0);
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_my_refferals_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.need_help_from_refferals) {//mMovieDetailPresenter.shareMovie();
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

    @Override
    public void onButtonClicked(String text) {

    }
}