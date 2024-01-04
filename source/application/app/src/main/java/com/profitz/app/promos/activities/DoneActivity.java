package com.profitz.app.promos.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterDone;
import com.profitz.app.promos.data.DataDone;
import com.profitz.app.promos.data.ReviewSender;
import com.profitz.app.promos.fragments.BottomSheetDialogDone;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DoneActivity extends AppCompatActivity implements BottomSheetDialogDone.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVDonePrice;
    private AdapterDone mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context = this;
    RecyclerView fishPriceList;
    ImageView arrow_back;
    ImageView help_button;
    RelativeLayout hide_if_zero;
    String count_done;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    float ratingg;
    private RatingBar ratingBar;
    List<DataDone> data=new ArrayList<>();
    EditText search_edit_text;
    static User user;
    static HashMap<String, Object> attributesMap;
    TextWatcher textWatcher;
    Dialog dialog3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBarDetail);
        super.onCreate(savedInstanceState);
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
        //getWindow().setStatusBarColor(Color.parseColor("#FFE45A"));
        setContentView(R.layout.activity_done);
        //Make call to AsyncTask
        arrow_back = findViewById(R.id.arrow_back);
        help_button = findViewById(R.id.help_button);

        LottieAnimationView animationView;
        dialog3 = new Dialog(context); // Context, this, etc.
        dialog3.setContentView(R.layout.dialog_disable_future);
        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                    args.putString("source","DoneActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
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


        hide_if_zero = findViewById(R.id.hide_if_zero);
        fishPriceList = findViewById(R.id.fishPriceList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        getData();
                    }
                }
        );

getData();

        search_edit_text = findViewById(R.id.search_edit_text);
        textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                mAdapter.getFilter().filter(editable.toString());

            }
        };



    }
   /* public void openChat(){
        Customerly.openSupport (this);
        Customerly.registerUser(
                user.getEmail(),
                String.valueOf(user.getId()),                //OPTIONALLY you can pass the user ID or null
                user.getUsername(),             //OPTIONALLY you can pass the user name or null
                attributesMap
        );
    }*/
    public void getData(){
        mSwipeRefreshLayout.setRefreshing(false);

        user = MyPreferenceManager.getInstance(DoneActivity.this).getUser();

        attributesMap = new HashMap<String, Object>();
        attributesMap.put("punkty", user.getPoints());
        attributesMap.put("wykonane", user.getDone_count());
        attributesMap.put("zarobione", user.getEarned());
        final String url2 = "https://yoururl.com/api/check_done_count.php?user_id="+user.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            count_done = response.getString("count_done");
                            if (count_done.equals("0")) {
                                hide_if_zero.setVisibility(View.VISIBLE);
                                search_edit_text.removeTextChangedListener(textWatcher);

                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();

                            } else {
                                search_edit_text.addTextChangedListener(textWatcher);
                                hide_if_zero.setVisibility(View.GONE);
                                new AsyncDone().execute();

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
        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }
public void openDialogReview(String promoName, int promo_id, String promo_earn_points){
    final Dialog dialog_review = new Dialog(context); // Context, this, etc.
    dialog_review.setContentView(R.layout.dialog_add_review);
    dialog_review.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    TextView dialog_review_name = (TextView) dialog_review.findViewById(R.id.dialog_review_name);
    dialog_review_name.setText(promoName);
    User user = MyPreferenceManager.getInstance(DoneActivity.this).getUser();

    Context context_dialog = dialog_review.getContext();
    TextView ratingbar_text = dialog_review.findViewById(R.id.rating_score_text);
    TextView dialog_promo_info_points = dialog_review.findViewById(R.id.dialog_promo_info_points);
    dialog_promo_info_points.setVisibility(View.VISIBLE);
    String dialog_text = "Dodaj opinię by otrzymać " + promo_earn_points +" bonusowych punktów.";
    dialog_promo_info_points.setText(dialog_text);


    EditText dialog_review_text = (EditText) dialog_review.findViewById(R.id.dialog_review_text);
    dialog_review_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (dialog_review_text.getText().toString().trim().length() < 10) {
                    dialog_review_text.setError("Wprowadź minimum 10 znaków.");
                } else {
                    // your code here
                    dialog_review_text.setError(null);
                }
            } else {
                if (dialog_review_text.getText().toString().trim().length() < 10) {
                    dialog_review_text.setError("Wprowadź minimum 10 znaków.");
                } else {
                    // your code here
                    dialog_review_text.setError(null);
                }
            }

        }
    });
    ratingBar = (RatingBar) dialog_review.findViewById(R.id.rating_score);
    ImageView iv_dialog_promo = (ImageView) dialog_review.findViewById(R.id.iv_dialog_promo);
    ratingBar.setRating(Float.parseFloat("5.0"));
    ratingbar_text.setText("5.0");
    ratingg= (float) 5.0;
    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            ratingg = ratingBar.getRating();
            String rating_string = String.valueOf(ratingg);
            ratingbar_text.setText(rating_string);
        }
                                     });
    TextView add_review = dialog_review.findViewById(R.id.button_review_confirm);
    add_review.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
                if (dialog_review_text.length() < 10 ||
                        dialog_review_text.length() > 175) {

                    dialog_review_text.setError("Ilość znaków powinna wynosić od 1 do 175.");

                    dialog_review_text.requestFocus();
                } else {
                 String urlAddress = "https://yoururl.com/api/insert_review.php?add_points=true";
                 ReviewSender s = new ReviewSender(DoneActivity.this, urlAddress, String.valueOf(user.getId()), String.valueOf(promo_id), promoName, promo_earn_points, String.valueOf(ratingg), dialog_review_text.getText().toString());
                 s.execute();
                    dialog_review.dismiss();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final Dialog dialog_received_points_from_review = new Dialog(context); // Context, this, etc.
                            dialog_received_points_from_review.setContentView(R.layout.dialog_received_points_from_review);
                            TextView text_receive_points = dialog_received_points_from_review.findViewById(R.id.dialog_received_points_info2);
                            Button button_received_points = dialog_received_points_from_review.findViewById(R.id.button_received_points);
                            String text_string = "Otrzymujesz "+ promo_earn_points +" bonusowych punktów za wykonanie promocji "+promoName;
                            text_receive_points.setText(text_string);
                            dialog_received_points_from_review.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog_received_points_from_review.show();
                            button_received_points.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog_received_points_from_review.dismiss();
                                }
                            });
                        }
                    }, 1500);

            }
        }
    });

        Picasso.with(context_dialog).load("https://yoururl.com/api/images/"+promoName+".png").into(iv_dialog_promo);
    dialog_review.show();
}


    @Override
    public void onButtonClicked(String text) {

    }

    private class AsyncDone extends AsyncTask<String, String, String> {
       // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

        LayoutInflater factory = LayoutInflater.from(DoneActivity.this);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(DoneActivity.this);


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
                User user = MyPreferenceManager.getInstance(DoneActivity.this).getUser();
                url = new URL("https://yoururl.com/api/get_dones.php?user_id="+user.getId());

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
                Log.d("resultt", user.getId());

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
            hide_if_zero.setVisibility(View.GONE);
            //this method will be running on UI thread
           // gif_loader.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    main_dialog.dismiss();
                    handler.postDelayed(this, 2500);
                }
            }, 2000);


            // pdLoading.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
           // pdLoading.dismiss();
            ArrayList<DataDone> data  = new ArrayList<>();

            FragmentManager fragmentManager = getSupportFragmentManager();

            // Setup and Handover data to recyclerview
            mRVDonePrice = (RecyclerView)findViewById(R.id.fishPriceList);
            mAdapter = new AdapterDone(DoneActivity.this, data,fragmentManager);
            try {

                JSONArray jArray = new JSONArray(result);
                Log.d("resultt", result);
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataDone doneData = new DataDone();
                    doneData.doneImage= json_data.getString("promo_img");
                    doneData.doneName= json_data.getString("promo_name");
                    doneData.doneEarn= json_data.getString("promo_earn");
                    doneData.doneBonus= json_data.getString("promo_bonus");
                    doneData.donePromoId= json_data.getInt("promo_id");
                    doneData.doneDate= json_data.getString("promo_date");
                    doneData.doneStatus= json_data.getString("promo_status");
                    doneData.doneReceivedPoints = json_data.getString("promo_received_points");
                    doneData.doneStatusAdditionalInfo=json_data.getString("promo_status_additional_info");

                    data.add(doneData);
                }



                mRVDonePrice.setAdapter(mAdapter);
                mRVDonePrice.setLayoutManager(new GridLayoutManager(DoneActivity.this,2));

            } catch (JSONException e) {
                mSwipeRefreshLayout.setRefreshing(false);
            //    Toast.makeText(DoneActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(DoneActivity.this, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.need_help_from_done) {//mMovieDetailPresenter.shareMovie();
            dialog3.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
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
