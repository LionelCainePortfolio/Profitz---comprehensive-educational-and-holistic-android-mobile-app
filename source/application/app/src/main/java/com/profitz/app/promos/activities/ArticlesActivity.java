package com.profitz.app.promos.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.profitz.app.R;import com.profitz.app.promos.adapters.AdapterArticles;
import com.profitz.app.promos.adapters.ExpandableListViewAdapter;
import com.profitz.app.promos.data.DataArticles;
import com.profitz.app.promos.fragments.BottomSheetDialogHelp;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.squareup.seismic.ShakeDetector;

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

public class ArticlesActivity extends AppCompatActivity implements BottomSheetDialogHelp.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private List<String> listDataGroup;
    Context context = this;
    ArrayList<DataArticles> data;
    RelativeLayout rl_layout_1;
    RelativeLayout rl_layout_2;
    RelativeLayout rl_layout_3;
    RelativeLayout rl_layout_4;
    TextView rl_layout_1_title;
    TextView rl_layout_2_title;
    TextView rl_layout_3_title;
    TextView rl_layout_4_title;
    ImageView arrow_back;
    ImageView search_button;
    Dialog dialog3;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    String categories_ids;
    String topic_name;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRVArticlesList;
    private AdapterArticles mAdapter;
    private HashMap<String, List<String>> listDataChild;
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
        setContentView(R.layout.activity_help_articles);
        // initializing the views
       // initViews();
        // initializing the listeners
       // initListeners();
        // initializing the objects
       // initObjects();
        // preparing list data
      //  initListData();
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
                    args.putString("source","ArticlesActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        Intent myIntent = getIntent(); // gets the previously created intent
        categories_ids = myIntent.getStringExtra("categories_ids"); // will return "FirstKeyValue"
        topic_name= myIntent.getStringExtra("topic_name");

        TextView topic_name_txt = findViewById(R.id.topic_name);
        topic_name_txt.setText(topic_name);
        rl_layout_1 = findViewById(R.id.rl_layout_1);
        rl_layout_2 = findViewById(R.id.rl_layout_2);
        rl_layout_3 = findViewById(R.id.rl_layout_3);
        rl_layout_4 = findViewById(R.id.rl_layout_4);
        rl_layout_1_title = findViewById(R.id.rl_layout_1_title);
        rl_layout_2_title = findViewById(R.id.rl_layout_2_title);
        rl_layout_3_title = findViewById(R.id.rl_layout_3_title);
        rl_layout_4_title = findViewById(R.id.rl_layout_4_title);

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



        if (categories_ids.contains("Podstawy")){
            getArticles("Podstawy"); }
        if (categories_ids.contains("Konto")){
            getArticles("Konto"); }
        if (categories_ids.contains("Promocje")){
            getArticles("Promocje"); }
        //
        if (categories_ids.contains("Punkty")){
            getArticles("Punkty"); }
        if (categories_ids.contains("Nagrody")){
            getArticles("Nagrody"); }
        if (categories_ids.contains("Osiagniecia")){
            getArticles("Osiagniecia"); }
        //
        if (categories_ids.contains("Premium")){
            getArticles("Premium");}
        //
        if (categories_ids.contains("Program partnerski")){
            getArticles("Program_partnerski"); }
        if (categories_ids.contains("Licencje")){
            getArticles("Licencje"); }
        //
        if (categories_ids.contains("Platnosci")){
            getArticles("Platnosci"); }
        //
        if (categories_ids.contains("Prywatnosc_i_bezpieczenstwo")){
            getArticles("Prywatnosc_i_bezpieczenstwo"); }
        arrow_back = findViewById(R.id.arrow_back);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });



    }
    public void getArticles(String article_category_name)
    {

        class AsyncArticles extends AsyncTask<String, String, String> {
            // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
            HttpURLConnection conn;
            URL url = null;

            final LayoutInflater factory = LayoutInflater.from(ArticlesActivity.this);
            final View DialogView = factory.inflate(R.layout.custom_load_layout, null);
            final Dialog main_dialog = new Dialog(ArticlesActivity.this);


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
                   // User user = PrefManager.getInstance(ArticlesActivity.this).getUser();
                    url = new URL("https://yoururl.com/api/get_articles.php?category="+article_category_name);

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
                        handler.postDelayed(this, 2500);
                    }
                }, 2000);


                // pdLoading.dismiss();
                // pdLoading.dismiss();
                data = new ArrayList<>();

                FragmentManager fragmentManager = getSupportFragmentManager();

                // Setup and Handover data to recyclerview
                mAdapter = new AdapterArticles(ArticlesActivity.this, data,fragmentManager);
                if (article_category_name.equals("Podstawy"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_1_list);
                    rl_layout_1.setVisibility(View.VISIBLE);
                    rl_layout_1_title.setText("Podstawy");
                }
                else if (article_category_name.equals("Konto"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_2_list);
                    rl_layout_2.setVisibility(View.VISIBLE);
                    rl_layout_2_title.setText("Konto");


                }
                else if (article_category_name.equals("Promocje"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_3_list);
                    rl_layout_3.setVisibility(View.VISIBLE);
                    rl_layout_3_title.setText("Promocje");


                }
                else if (article_category_name.equals("Punkty"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_1_list);
                    rl_layout_1.setVisibility(View.VISIBLE);
                    rl_layout_1_title.setText("Punkty");


                }
                else if (article_category_name.equals("Nagrody"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_2_list);
                    rl_layout_2.setVisibility(View.VISIBLE);
                    rl_layout_2_title.setText("Nagrody");


                }
                else if (article_category_name.equals("Osiagniecia"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_3_list);
                    rl_layout_3.setVisibility(View.VISIBLE);
                    rl_layout_3_title.setText("Osiągnięcia");


                }
                else if (article_category_name.equals("Program_partnerski"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_1_list);
                    rl_layout_1.setVisibility(View.VISIBLE);
                    rl_layout_1_title.setText("Program partnerski");


                }
                else if (article_category_name.equals("Premium"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_1_list);
                    rl_layout_1.setVisibility(View.VISIBLE);
                    rl_layout_1_title.setText("Premium");


                }
                else if (article_category_name.equals("Licencje"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_2_list);
                    rl_layout_2.setVisibility(View.VISIBLE);
                    rl_layout_2_title.setText("Licencje");

                }
                else if (article_category_name.equals("Platnosci"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_1_list);
                    rl_layout_1.setVisibility(View.VISIBLE);
                    rl_layout_1_title.setText("Płatności");


                }
                else if (article_category_name.equals("Prywatnosc_i_bezpieczenstwo"))
                {
                    mRVArticlesList = (RecyclerView)findViewById(R.id.rl_layout_1_list);
                    rl_layout_1.setVisibility(View.VISIBLE);
                    rl_layout_1_title.setText("Prywatność i bezpieczeństwo");


                }
                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        DataArticles articlesData = new DataArticles();
                        articlesData.articleIdd = json_data.getInt("article_id");
                        articlesData.articleName= json_data.getString("article_name");
                        articlesData.articleCategory= json_data.getString("article_category");
                        articlesData.articleDesc= json_data.getString("article_description");
                        articlesData.articleCategoryId= json_data.getInt("article_category_id");
                        articlesData.articleShowAs= json_data.getInt("article_show_as");
                        articlesData.articleClicable= json_data.getInt("article_clicable");
                        articlesData.articleVisibility= json_data.getInt("article_visibility");
                        articlesData.articleClicks= json_data.getInt("article_clicks");
                        articlesData.articleWasHelpfulYes = json_data.getInt("article_was_helpful_yes");
                        articlesData.articleWasHelpfulNo = json_data.getInt("article_was_helpful_no");


                        data.add(articlesData);
                    }

                    mRVArticlesList.setAdapter(mAdapter);

                    mRVArticlesList.setLayoutManager(new GridLayoutManager(ArticlesActivity.this,1));
                    mRVArticlesList.setNestedScrollingEnabled(false);


                } catch (JSONException e) {
                    //Toast.makeText(ArticlesActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }
        new AsyncArticles().execute();

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
