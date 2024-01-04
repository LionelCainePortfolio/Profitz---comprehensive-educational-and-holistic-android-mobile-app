package com.profitz.app.promos.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterFavorite;
import com.profitz.app.promos.data.DataFavorite;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FavoriteActivity extends AppCompatActivity implements AdapterFavorite.PromoClickListener, BottomSheetDialogReportBug.BottomSheetListener {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFavorites;
    private AdapterFavorite mAdapter;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Context context = this;
    AdapterFavorite.PromoClickListener dis = this;
    RecyclerView fishPriceList;
    RelativeLayout hide_if_zero;
    ImageView arrow_back;
    ImageView help_button;
    String count_favorite;
    List<DataFavorite> data=new ArrayList<>();
    EditText search_edit_text;
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
       // getWindow().setStatusBarColor(Color.parseColor("#FFE45A"));
        setContentView(R.layout.activity_favorite);
        arrow_back = findViewById(R.id.arrow_back);
        help_button = findViewById(R.id.help_button);

        //Make call to AsyncTask
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

        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        LottieAnimationView animationView;
        dialog3 = new Dialog(context); // Context, this, etc.
        dialog3.setContentView(R.layout.dialog_disable_future);
        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
        search_edit_text = findViewById(R.id.search_edit_text);
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
                    args.putString("source","FavoriteActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
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
public void getData(){
    mSwipeRefreshLayout.setRefreshing(false);

    User user = MyPreferenceManager.getInstance(FavoriteActivity.this).getUser();
    final String url2 = "https://yoururl.com/api/check_favorite_count.php?user_id="+user.getId();
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        count_favorite = response.getString("count_favorite");
                        if (count_favorite.equals("0")) {
                            hide_if_zero.setVisibility(View.VISIBLE);
                            search_edit_text.removeTextChangedListener(textWatcher);

                           // Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();

                        } else if (count_favorite.equals("1")) {
                            hide_if_zero.setVisibility(View.GONE);
                            search_edit_text.addTextChangedListener(textWatcher);

                            FavoriteActivity.AsyncFavorites asyncFavorites = new FavoriteActivity.AsyncFavorites();
                            asyncFavorites.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    @Override
    public void onButtonClicked(String text) {

    }


    class AsyncFavorites extends AsyncTask<String, String, String> {
       // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

        LayoutInflater factory = LayoutInflater.from(FavoriteActivity.this);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(FavoriteActivity.this);


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
                    User user = MyPreferenceManager.getInstance(FavoriteActivity.this).getUser();
                    url = new URL("https://yoururl.com/api/get_favorites.php?user_id=" + user.getId());

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
                      //  hide_if_zero.setVisibility(View.VISIBLE);
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
                    handler.postDelayed(this, 1500);
                }
            }, 1000);


            // pdLoading.dismiss();
            List<DataFavorite> data=new ArrayList<>();
            mSwipeRefreshLayout.setRefreshing(false);
           // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFavorite favoriteData = new DataFavorite();
                    favoriteData.favoriteImage= json_data.getString("promo_img");
                    favoriteData.favoriteName= json_data.getString("promo_name");
                    favoriteData.favoriteEarn= json_data.getString("promo_earn");
                    favoriteData.favoritePromoId= json_data.getInt("promo_id");
                    favoriteData.favoriteDate= json_data.getString("promo_date");
                    data.add(favoriteData);
                }

                // Setup and Handover data to recyclerview
                mRVFavorites = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFavorite(FavoriteActivity.this, dis, data);
                mRVFavorites.setAdapter(mAdapter);
                mRVFavorites.setLayoutManager(new GridLayoutManager(FavoriteActivity.this,3));

            } catch (JSONException e) {
             //   Toast.makeText(FavoriteActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(FavoriteActivity.this, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }
    @Override
    public void onPromoFavClick(DataFavorite dataFavorite) {
       // Intent DetailActivityIntent = new Intent(this, PromoDetailActivity.class);
        //DetailActivityIntent.putExtra(PromoDetailActivity.INTENT_EXTRA_PROMO, promo);
        //startActivity(DetailActivityIntent);

      /*  Log.d("TAGDD", String.valueOf(dataFavorite));
        Intent DetailActivityIntent = new Intent(this, PromoDetailActivity.class);
        DetailActivityIntent.putExtra("intent_fav","1");
        startActivity(DetailActivityIntent);
*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_favorite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.need_help_from_favorite) {
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
