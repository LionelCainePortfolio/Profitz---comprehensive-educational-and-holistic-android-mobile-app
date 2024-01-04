package com.profitz.app.promos.activities;


import android.annotation.SuppressLint;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.fragments.BottomSheetDialogAllNotifications;
import com.profitz.app.promos.fragments.BottomSheetDialogDisabledFuture;
import com.profitz.app.promos.fragments.BottomSheetDialogNeedHelp;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.util.ScrollViewPremium;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterNotification;
import com.profitz.app.promos.data.DataNotification;
import com.profitz.app.promos.fragments.EarnPointsFragment;
import com.profitz.app.promos.fragments.HistoryPointsFragment;
import com.profitz.app.promos.fragments.ReferPointsFragment;
import com.profitz.app.promos.fragments.SendPointsFragment;
import com.profitz.app.promos.fragments.TopUpPointsFragment;
import com.profitz.app.promos.fragments.TradePointsFragment;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class PointsActivity extends AppCompatActivity implements BottomSheetDialogAllNotifications.BottomSheetListener, BottomSheetDialogNeedHelp.BottomSheetListener, BottomSheetDialogDisabledFuture.BottomSheetListener,  BottomSheetDialogReportBug.BottomSheetListener  {
    Context context = this;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVNotificationList;
    private AdapterNotification mAdapter;
    User user2;
    RecyclerView NotificationList;
    String count_notifications;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TextView mView1;
    private TextView mView2;
    private RelativeLayout mView3;
    private RelativeLayout mView4;
    private RelativeLayout mView5;
    private RelativeLayout mView6;
    private RelativeLayout mView7;
    private RelativeLayout mView8;
    private TextView mView9;
    private TextView mView10;
    TextView textViewPoints;
    TextView textViewPointsEur;
    private String data_getPoints;
    RelativeLayout points_layout_history;
    MenuItem helpItem;
    ImageView arrow_back;
    ImageView help_button;
    private static final String SHOWCASE_ID = "Sequence Showcase";
    private ScrollViewPremium scrollView;
    LinearLayout nothings_here_points_lr;
    TextView see_all_notifications_points;
    @SuppressLint({"WrongViewCast", "CutPasteId"})
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
        setContentView( R.layout.activity_top_up_points);
        arrow_back = findViewById(R.id.arrow_back);
        help_button = findViewById(R.id.help_button);

        user2 = MyPreferenceManager.getInstance(context).getUser();
        points_layout_history = findViewById(R.id.points_layout_history);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //final androidx.fragment.app.Fragment global = new PointsFragment();
        final androidx.fragment.app.Fragment first = new TopUpPointsFragment();
        final androidx.fragment.app.Fragment second = new TradePointsFragment();
        final androidx.fragment.app.Fragment third = new SendPointsFragment();
        final androidx.fragment.app.Fragment fourth = new HistoryPointsFragment();
        final androidx.fragment.app.Fragment fifth = new EarnPointsFragment();
        final androidx.fragment.app.Fragment sixth = new ReferPointsFragment();


        nothings_here_points_lr = findViewById(R.id.nothings_here_points_lr);
        see_all_notifications_points = findViewById(R.id.see_all_notifications_points);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        help_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle argsSheet = new Bundle();
                BottomSheetDialogNeedHelp bottomSheetHelp = new BottomSheetDialogNeedHelp();
                argsSheet.putString("source","pointsActivity");
                bottomSheetHelp.setArguments(argsSheet);
                bottomSheetHelp.show(getSupportFragmentManager(), bottomSheetHelp.getTag());
                //showTutorSequence(500);
            }
        });

        scrollView = findViewById(R.id.scrollView);

        NotificationList = findViewById(R.id.NotificationListPoints);
        mRVNotificationList = findViewById(R.id.NotificationListPoints);
        getData();
        RelativeLayout relative_global = findViewById(R.id.relative_global);
        RelativeLayout layout_fragments = findViewById(R.id.layout_fragments);
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
                    args.putString("source","PointsActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        mView1 = findViewById(R.id.textpoints);
        mView2 = findViewById(R.id.texttradeeur2);
        mView3 = findViewById(R.id.relativepoints1);
        mView4 = findViewById(R.id.relativepoints2);
        mView5 = findViewById(R.id.relativepoints3);
        mView6 = findViewById(R.id.relativepoints4);
        mView7 = findViewById(R.id.relativepoints5);
        mView8 = findViewById(R.id.relativepoints6);
        mView9 = findViewById(R.id.textInfo6);
        mView10 = findViewById(R.id.see_all_notifications_points);
        findViewById(R.id.my_rounded_sign_in_button).setOnClickListener(mListener);
        textViewPoints = findViewById(R.id.textpoints);
        textViewPointsEur =  findViewById(R.id.texttradeeur2);
        init();
        if(getIntent().getIntExtra("openTopUp",0)==1){
            //getSupportActionBar().hide();
            relative_global.setVisibility(View.GONE);
            layout_fragments.setVisibility(View.VISIBLE);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.layout_fragments, first);
            fragmentTransaction.commit();
        }
        if(getIntent().getIntExtra("openWithdraw",0)==1){
            //getSupportActionBar().hide();
            relative_global.setVisibility(View.GONE);
            layout_fragments.setVisibility(View.VISIBLE);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.layout_fragments, second);
            fragmentTransaction.commit();
        }


        RelativeLayout TopUpButton = (RelativeLayout) findViewById(R.id.relativepoints1);
        RelativeLayout TradeButton = (RelativeLayout) findViewById(R.id.relativepoints2);
        RelativeLayout SendButton = (RelativeLayout) findViewById(R.id.relativepoints3);
        RelativeLayout HistoryButton = (RelativeLayout) findViewById(R.id.relativepoints4);
        RelativeLayout EarnButton = (RelativeLayout) findViewById(R.id.relativepoints5);
        RelativeLayout ReferButton = (RelativeLayout) findViewById(R.id.relativepoints6);

        TopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getSupportActionBar().hide();
                relative_global.setVisibility(View.GONE);
                layout_fragments.setVisibility(View.VISIBLE);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout_fragments, first);
                fragmentTransaction.commit();
            }
        });
        TradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                BottomSheetDialogDisabledFuture bottomSheetDialogDisabledFuture = new BottomSheetDialogDisabledFuture();
                args.putString("source","pointsActivity");
                bottomSheetDialogDisabledFuture.setArguments(args);
                bottomSheetDialogDisabledFuture.show(getSupportFragmentManager(), bottomSheetDialogDisabledFuture.getTag());               /* relative_global.setVisibility(View.GONE);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout_fragments, second);
                fragmentTransaction.commit();

                */
            }
        });
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                BottomSheetDialogDisabledFuture bottomSheetDialogDisabledFuture = new BottomSheetDialogDisabledFuture();
                args.putString("source","pointsActivity");
                bottomSheetDialogDisabledFuture.setArguments(args);
                bottomSheetDialogDisabledFuture.show(getSupportFragmentManager(), bottomSheetDialogDisabledFuture.getTag());

               /* relative_global.setVisibility(View.GONE);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout_fragments, third);
                fragmentTransaction.commit();

                */
            }
        });
        HistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                BottomSheetDialogDisabledFuture bottomSheetDialogDisabledFuture = new BottomSheetDialogDisabledFuture();
                args.putString("source","pointsActivity");
                bottomSheetDialogDisabledFuture.setArguments(args);
                bottomSheetDialogDisabledFuture.show(getSupportFragmentManager(), bottomSheetDialogDisabledFuture.getTag());
                /*relative_global.setVisibility(View.GONE);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout_fragments, fourth);
                fragmentTransaction.commit(); */
            }
        });
        EarnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                // Toast.makeText( PromosActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
                // hidecurrentfragment();
                Intent intent = new Intent(PointsActivity.this, PromosActivity.class);
                intent.putExtra("fromPoints","openAwards");
                startActivity(intent);

            }
        });
        ReferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PointsActivity.this, PromosActivity.class);
                intent.putExtra("fromPoints","openReferFriend");
                startActivity(intent);
                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                // Toast.makeText( PromosActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
                // hidecurrentfragment();
                //startActivity(new Intent(PointsActivity.this,ReferFriendActivity.class));

            }
        });
        TextView see_all_notifications_points = findViewById(R.id.see_all_notifications_points);
        see_all_notifications_points.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                BottomSheetDialogAllNotifications bottomSheet = new BottomSheetDialogAllNotifications();
                args.putString("notifType", "points");
                bottomSheet.setArguments(args);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });

    }
    public void getData(){

        final String url2 = "https://yoururl.com/api/check_notification_count.php?type=points&user_id="+user2.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            count_notifications = response.getString("count_notifications");
                            if (count_notifications.equals("0")) {
                                NotificationList.setVisibility(View.GONE);

                                see_all_notifications_points.setVisibility(View.GONE);
                                nothings_here_points_lr.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();

                            } else if (count_notifications.equals("1")) {
                                nothings_here_points_lr.setVisibility(View.GONE);
                                NotificationList.setVisibility(View.VISIBLE);
                                see_all_notifications_points.setVisibility(View.GONE);
                                new AsyncPoints().execute();

                            }
                            else if (count_notifications.equals("2")){
                                nothings_here_points_lr.setVisibility(View.GONE);
                                NotificationList.setVisibility(View.VISIBLE);
                                see_all_notifications_points.setVisibility(View.VISIBLE);
                                new AsyncPoints().execute();
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

    @SuppressLint("StaticFieldLeak")
    class AsyncPoints extends AsyncTask<String, String, String> {
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
                url = new URL("https://yoururl.com/api/get_notifications.php?user_id="+user2.getId()+"&limit=yes&notification_type=points");
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
            //    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(getApplicationContext(), "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       // getMenuInflater().inflate(R.menu.detail_points_menu, menu);
        // inflate menu from xml
       // MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.drawer, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_points_menu, menu);
        helpItem = menu.findItem(R.id.need_help_from_points);
        //helpItem.setIcon(ContextCompat.getDrawable(this, R.drawable.help_icon));

        return true;
    }

    public void showTutorSequence(int millis) {
        MaterialShowcaseView.resetSingleUse(PointsActivity.this, SHOWCASE_ID);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(millis);
        final int COLOR_MASK = getResources().getColor(R.color.dark_bg_opacity);
        final int COLOR_DISMISS = getResources().getColor(R.color.colorAccent);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                if((position==1) || (position==2) || (position == 3)|| (position == 5)|| (position == 6)|| (position == 7))
                {
                    scrollView.setScrolling(true); // to disable scrolling
                    scrollView.scrollTo(0, (scrollView.getTop()));
                    scrollView.setScrolling(false); // to disable scrolling
                }
                else if(position==4)
                {
                    scrollView.setScrolling(true); // to disable scrolling
                    scrollView.scrollTo(0, (scrollView.getTop()+70));
                    scrollView.setScrolling(false); // to disable scrolling

                }
                else if(position==8)
                {
                    scrollView.setScrolling(true); // to disable scrolling
                    scrollView.scrollTo(0, scrollView.getBottom());
                    scrollView.setScrolling(false); // to disable scrolling

                }
                else if(position==9)
                {
                    scrollView.setScrolling(true); // to disable scrolling
                    scrollView.scrollTo(0, scrollView.getBottom());
                    scrollView.setScrolling(false); // to disable scrolling

                }
                else{
                    scrollView.setScrolling(false); // to disable scrolling
                }
               // Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView itemView, int position) {
                scrollView.setScrolling(true); // to disable scrolling
                if(position==9)
                {
                    scrollView.scrollTo(0, scrollView.getTop());
                }
            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                .setTarget(mView1)
                .setDismissText("Dalej")
                .setMaskColour(COLOR_MASK)
                .setDismissTextColor(COLOR_DISMISS)
                .setContentText("Tu wyświetla się ilość punktów przypisanych do twojego konta, które udało ci się zdobyć.")
                .withRectangleShape(true)
                .build()
 );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView2)
                        .setDismissText("Rozumiem")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("W tym miejscu możesz zobaczyć ile twoje punkty mogą być warte w przeliczeniu na euro - w przypadku chęci wymiany ich. Puntky nie są walutą, także wirtualną i nie posiadają żadnej wartości, toteż nie można traktować ich jako pieniądz.")
                        .withRectangleShape(true)
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView3)
                        .setDismissText("Dalej")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Klikając na ten przycisk, przeniesiesz się do zakładki, w której możesz dokupić punkty do swojego konta.")
                        .withCircleShape()
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView4)
                        .setDismissText("Dalej")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Klikając na ten przycisk, przeniesiesz się do zakładki, w której możesz wymienić swoje punkty na jedną z nagród.")
                        .withCircleShape()
                        .build()
        );
        sequence.addSequenceItem(
        new MaterialShowcaseView.Builder(this)
                        .setTarget(mView5)
                        .setDismissText("Dalej")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Klikając na ten przycisk, przeniesiesz się do zakładki, w której możesz wysłać swoje punkty do swojego znajomego (w fazie rozwoju).")
                        .withCircleShape()
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView6)
                        .setDismissText("Dalej")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Klikając na ten przycisk, przeniesiesz się do zakładki, w której możesz zobaczyć swoją aktualną historię dot. punktów (wymiany, transfery itp.).")
                        .withCircleShape()
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView7)
                        .setDismissText("Dalej")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Klikając na ten przycisk, przeniesiesz się do zakładki, w której możesz zdobyć więcej punktów, wykonując zadania (w fazie rozwoju).")
                        .withCircleShape()
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView8)
                        .setDismissText("Dalej")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Klikając na ten przycisk, przeniesiesz się do zakładki, w której dowiesz się więcej o programie poleceń, dzięki któremu możesz zyskać więcej punktów.")
                        .withCircleShape()
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView9)
                        .setDismissText("Rozumiem")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("W tym miejscu znajdziesz najnowszą aktywność, w której udało Ci się uzyskać, lub wymienić punkty.")
                        .withCircleShape()
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mView10)
                        .setDismissText("Ok")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Kliknij tutaj, aby zobaczyć całą historię aktywności punktowej.")
                        .withCircleShape()
                        .build()
        );
        sequence.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.need_help_from_points) {//mMovieDetailPresenter.shareMovie();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void init(){
        User user = MyPreferenceManager.getInstance(this).getUser();
        final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            DecimalFormat precision = new DecimalFormat("0.00");
                            data_getPoints = response.getString("points");
                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                            textViewPoints.setText(data_getPoints);
                                double points_double = Double.parseDouble(data_getPoints);
                                double calculate_points_to_eur=(points_double/10);
                                String calculated = precision.format(calculate_points_to_eur);
                               String calculation = calculated + " eur";
                              textViewPointsEur.setText(calculation);

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
        //setting the values to the textviews
        // textViewId.setText(String.valueOf(user.getId()));
        // textViewUsername.setText(user.getUsername());
        // textViewEmail.setText(user.getEmail());

        //when the user presses logout button calling the logout method

    }
    private final View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (view.getId() == R.id.my_rounded_sign_in_button) {//  Intent intent = new Intent(PointsActivity.this, ReferFriendActivity.class);
                //  intent.putExtra("openReferFriend", 1); //for example
                //    startActivity(intent);
            }
        }
    };

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