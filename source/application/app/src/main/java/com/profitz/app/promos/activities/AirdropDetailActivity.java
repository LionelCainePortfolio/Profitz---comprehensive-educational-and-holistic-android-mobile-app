package com.profitz.app.promos.activities;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.ProfitzApp;
import com.profitz.app.R;import com.profitz.app.data.model.Comment;
import com.profitz.app.data.model.Reply;
import com.profitz.app.promos.CustomLinearLayoutManager;
import com.profitz.app.promos.adapters.AdapterReply;
import com.profitz.app.promos.data.SenderAirdrop;
import com.profitz.app.promos.fragments.BottomSheetDialogAddComment;
import com.profitz.app.promos.fragments.BottomSheetDialogDeleteCommentConfirm;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.BottomSheetDialogReportComment;
import com.profitz.app.promos.fragments.BottomSheetDialogReportIssue;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.seismic.ShakeDetector;

import org.jetbrains.annotations.NotNull;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.profitz.app.ProfitzApp.getContext;

public class AirdropDetailActivity extends AppCompatActivity implements  BottomSheetDialogAddComment.BottomSheetListener, BottomSheetDialogReportComment.BottomSheetListener, BottomSheetDialogReportIssue.BottomSheetListener, BottomSheetDialogDeleteCommentConfirm.BottomSheetListener,  BottomSheetDialogReportBug.BottomSheetListener  {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    TextView add_review;
    String count_comments;
    ImageView no_comments_yet;
    TextView textInfo;
    TextView textInfoComment;
    int IDuser;
    private InterstitialAd mInterstitialAd;
    static Context context;
    LayoutInflater factory;
    View DialogView;
    Dialog main_dialog;
    String  data_getLevel;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    AdapterComments adapterComment;
    static String COMMENT_KEY = "Comment" ;
    public ArrayList<Reply> listReply = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ProfitzApp globalVariable;
    @BindView(R.id.rv_comment)
    RecyclerView RvComment;

    @BindView(R.id.report_issue_airdrop)
    LinearLayout report_issue_airdrop;

    @BindView(R.id.text_tutorial_title)
    TextView text_tutorial_title;

    @BindView(R.id.global_const_bg)
    ConstraintLayout global_const_bg;

    @BindView(R.id.text_PriceZero)
    TextView text_PriceZero;

    @BindView(R.id.arrow_back)
    ImageView arrow_back;

    @BindView(R.id.img_poster)
    CircleImageView mImgPoster;

    @BindView(R.id.fab_add_favorite)
    FloatingActionButton mFabAddFavorite;

    @BindView(R.id.text_title)
    TextView mTextTitle;

    @BindView(R.id.text_Price)
    TextView text_Price;

    @BindView(R.id.text_synopsis)
    TextView mTextSynopsis;

    @BindView(R.id.instruction)
    TextView instruction;

    @BindView(R.id.kyc_airdrop_required)
    RelativeLayout kyc_airdrop_required;

    @BindView(R.id.mail_airdrop_required)
    RelativeLayout mail_airdrop_required;

    @BindView(R.id.fb_airdrop_required)
    RelativeLayout fb_airdrop_required;

    @BindView(R.id.twitter_airdrop_required)
    RelativeLayout twitter_airdrop_required;

    @BindView(R.id.telegram_airdrop_required)
    RelativeLayout telegram_airdrop_required;

    @BindView(R.id.airdrop_website)
    TextView airdrop_website;

    @BindView(R.id.airdrop_token)
    TextView airdrop_token;

    @BindView(R.id.airdrop_exchanges)
    TextView airdrop_exchanges;

    @BindView(R.id.lr_twitter)
    LinearLayout lr_twitter;

    @BindView(R.id.lr_coinmarketcap)
    LinearLayout lr_coinmarketcap;

    @BindView(R.id.lr_telegram)
    LinearLayout lr_telegram;

    @BindView(R.id.lr_medium)
    LinearLayout lr_medium;

    @BindView(R.id.lr_reddit)
    LinearLayout lr_reddit;

    @BindView(R.id.lr_discord)
    LinearLayout lr_discord;


    HashMap<String, Object> attributesMap;

    private Toast mToast;
    static FloatingActionButton mFabAddDone;

    private String status_check_insert_favorite;
    double user_balance;
    double user_id;
    private static AirdropDetailActivity mInstance;
    private NestedScrollView nestedScrollView;
    User user;
    static ConstraintLayout constraint_detail;
    String username;
    String username2;
    String airdropId;
    String airdropTitle;
    String airdropEarn;
    String airdropInstruction;
    String airdropImgUrl;
    String airdropUrl;
    String airdropShortDesc;
    String airdropWhitepaper;
    String airdropWebsite;
    String airdropExchanges;
    String airdropToken;
    String airdropPlatform;
    String airdropTotalSupply;
    String airdropIsInactive;
    String airdropIsUpline;
    String airdropIsHot;
    String airdropIsNew;
    String airdropIsExclusive;
    String airdropTwitter;
    String airdropTelegram;
    String airdropMedium;
    String airdropCoinmarketCap;
    String airdropReddit;
    String airdropDiscord;
    String airdropKycRequired;
    String airdropMailRequired;
    String airdropFacebookRequired;
    String airdropTwitterRequired;
    String airdropTelegramRequired;
    public int position =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBarDetail);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.parseColor("#FFE45A"));
        setContentView( R.layout.activity_airdrop_detail);
        ButterKnife.bind( this );
        mInstance = this;
        context = this;
        globalVariable = (ProfitzApp) getApplicationContext();

        factory = LayoutInflater.from(this);
        DialogView = factory.inflate(R.layout.custom_load_layout, null);
        main_dialog = new Dialog(this);
        user = MyPreferenceManager.getInstance(context).getUser();
        constraint_detail = findViewById(R.id.constraint_detail);
        username = user.getUsername();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        attributesMap = new HashMap<String, Object>();
        attributesMap.put("punkty", user.getPoints());
        attributesMap.put("wykonane", user.getDone_count());
        attributesMap.put("zarobione", user.getEarned());
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
                    args.putString("source","AirdropDetailActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3720384080622115/7409282237");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            airdropId = extras.getString("airdropId");
            airdropTitle = extras.getString("airdropTitle");
            airdropEarn = extras.getString("airdropEarn");
            airdropInstruction = extras.getString("airdropInstruction");
            airdropImgUrl = extras.getString("airdropImgUrl");
            airdropShortDesc = extras.getString("airdropShortDesc");
            airdropWhitepaper = extras.getString("airdropWhitepaper");
            airdropWebsite = extras.getString("airdropWebsite");
            airdropExchanges= extras.getString("airdropExchanges");
            airdropToken= extras.getString("airdropToken");
            airdropPlatform= extras.getString("airdropPlatform");
            airdropTotalSupply = extras.getString("airdropTotalSupply");
            airdropIsInactive = extras.getString("airdropIsInactive");
            airdropIsUpline= extras.getString("airdropIsUpline");
            airdropIsHot= extras.getString("airdropIsHot");
            airdropIsNew= extras.getString("airdropIsNew");
            airdropIsExclusive= extras.getString("airdropIsExclusive");
            airdropKycRequired = extras.getString("airdropKycRequired");
            airdropMailRequired = extras.getString("airdropMailRequired");
            airdropFacebookRequired = extras.getString("airdropFacebookRequired");
            airdropTwitterRequired = extras.getString("airdropTwitterRequired");
            airdropTelegramRequired = extras.getString("airdropTelegramRequired");
            airdropTwitter = extras.getString("airdropTwitter");
            airdropTelegram = extras.getString("airdropTelegram");
            airdropMedium = extras.getString("airdropMedium");
            airdropCoinmarketCap = extras.getString("airdropCoinmarketCap");
            airdropReddit= extras.getString("airdropReddit");
            airdropDiscord = extras.getString("airdropDiscord");

            //The key argument here must match that used in the other activity
        }
        TextView add_comment = findViewById(R.id.add_comment);
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();

                BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                args.putString("commentId","0");
                args.putString("userId", String.valueOf(user.getId()));
                args.putString("promoId","0");
                args.putString("airdropId",airdropId);
                args.putString("isAirdrop", "1");
                args.putString("username",username2);
                args.putString("is_reply","no");
                args.putString("global_comm","yes");
                args.putString("comment_action","add");

                bottomSheetComment.setArguments(args);
                bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());

            }
        });

        user2 = MyPreferenceManager.getInstance(context).getUser();
        IDuser = Integer.parseInt(user2.getId());
        username = user2.getName();
        username2 = user2.getUsername();
        linearLayoutManager = new CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        textInfo = findViewById(R.id.textInfo);
        textInfoComment = findViewById(R.id.textInfoComment);
        add_review = findViewById(R.id.add_review);
        no_comments_yet = findViewById(R.id.no_comments_yet);
        showAirdrop();

        report_issue_airdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle argsReport = new Bundle();
                BottomSheetDialogReportIssue bottomSheetReportIssue= new BottomSheetDialogReportIssue();
                argsReport.putString("userId", String.valueOf(user.getId()));
                argsReport.putString("promoId","0");
                argsReport.putString("airdropId",airdropId);
                argsReport.putString("isAirdrop","1");
                argsReport.putString("username",username2);
                bottomSheetReportIssue.setArguments(argsReport);
                bottomSheetReportIssue.show(getSupportFragmentManager(), bottomSheetReportIssue.getTag());
            }
        });


    }
    public void commentAdded(int airdrop_id){
      //  Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Komentarz został dodany.</font>"), Snackbar.LENGTH_LONG);
      //  snackbar.show();
        Toasty.success(context, "Komentarz został dodany.").show();

        AsyncComments asyncComment = new AsyncComments(airdrop_id);
        asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        getData3(airdrop_id);

        // asyncComment.execute();
    }
    public void commentDeleted(int airdrop_id){
       // Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Komentarz został usunięty.</font>"), Snackbar.LENGTH_LONG);
       // snackbar.show();
        Toasty.success(context, "Komentarz został usunięty.").show();

        AsyncComments asyncComment = new AsyncComments(airdrop_id);
        asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        getData3(airdrop_id);


        // asyncComment.execute();
    }

    public void commentEdited(int airdrop_id){
      //  Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Komentarz został zaktualizowany.</font>"), Snackbar.LENGTH_LONG);
        //snackbar.show();
        Toasty.success(context, "Komentarz został zaktualizowany.").show();

        AsyncComments asyncComment = new AsyncComments(airdrop_id);
        asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // asyncComment.execute();

    }
    public void reportSent(){
       // Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Zgłoszenie zostało wysłane. Dziękujemy!</font>"), Snackbar.LENGTH_LONG);
       // snackbar.show();
        Toasty.success(context, "Zgłoszenie zostało wysłane. Dziękujemy!").show();

        // asyncComment.execute();
    }

    public void showAirdrop() {
        //setTitle(promo.getOriginalTitle());
        String urlAddress="https://yoururl.com/api/update_popularity.php?is_aidrop=1";
        User user = MyPreferenceManager.getInstance(this).getUser();
        FloatingActionButton fab = findViewById(R.id.fab_add_done);
        // CachePot.getInstance().push("name", promoName);
        //  CachePot.getInstance().push("id", promoId);
        // CachePot.getInstance().push("rating", promoRating);

        getData3(Integer.parseInt(airdropId));
        Button want_rules = findViewById(R.id.button_want_rules);
        want_rules.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String url_to_white_paper = airdropWhitepaper;
                Intent ir = new Intent(Intent.ACTION_VIEW);
                ir.setData(Uri.parse(url_to_white_paper));
                startActivity(ir);
            }
        });

        if (airdropKycRequired.equals("1")){
            kyc_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (airdropMailRequired.equals("1")){
            mail_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (airdropTwitterRequired.equals("1")){
            twitter_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (airdropTelegramRequired.equals("1")){
            telegram_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (airdropFacebookRequired.equals("1")){
            fb_airdrop_required.setVisibility(View.VISIBLE);
        }

        airdrop_website.setText(airdropWebsite);
        airdrop_token.setText(airdropToken);
        airdrop_exchanges.setText(airdropExchanges);

        if (!airdropTwitter.equals("0")){
            lr_twitter.setVisibility(View.VISIBLE);
        }
        if (!airdropCoinmarketCap.equals("0")){
            lr_coinmarketcap.setVisibility(View.VISIBLE);
        }
        if (!airdropTelegram.equals("0")){
            lr_telegram.setVisibility(View.VISIBLE);
        }
        if (!airdropMedium.equals("0")){
            lr_medium.setVisibility(View.VISIBLE);
        }
        if (!airdropReddit.equals("0")){
            lr_reddit.setVisibility(View.VISIBLE);
        }
        if (!airdropDiscord.equals("0")){
            lr_discord.setVisibility(View.VISIBLE);
        }

        lr_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(Intent.ACTION_VIEW);
               i.setData(Uri.parse(airdropTwitter));
               startActivity(i);
            }
        });

        lr_coinmarketcap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(airdropCoinmarketCap));
                startActivity(i);
            }
        });
        lr_telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(airdropTelegram));
                startActivity(i);
            }
        });
        lr_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(airdropMedium));
                startActivity(i);
            }
        });
        lr_reddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(airdropReddit));
                startActivity(i);
            }
        });
        lr_discord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(airdropDiscord));
                startActivity(i);
            }
        });
        TextView want_refer = findViewById(R.id.button_want_reffer);
        want_refer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String url_to_check_promo = "https://yoururl.com/api/get_airdrop_url.php?airdrop_id="+ airdropId+"&user_id="+user.getId();

                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_to_check_promo, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String get_airdrop_url = response.getString("url");
                                    // TODO Auto-generated method stub
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(get_airdrop_url));
                                    startActivity(i);
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
                Volley.newRequestQueue(context).add(jsonObjectRequest2);

            }
        });
        DecimalFormat df = new DecimalFormat("##.00");
        final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String data_getPoints = response.getString("points");
                            user_balance = Double.parseDouble(data_getPoints);
                            String user_id_string = response.getString("user_id");
                            user_id = Double.parseDouble(user_id_string);
                            data_getLevel = response.getString("user_level");

                            final String url2 = "https://yoururl.com/api/check_favorite.php?airdrop_id="+airdropId+"&is_airdrop=1&user_id="+user.getId()+"&airdrop_name="+ airdropTitle;

                            JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest
                                    (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                status_check_insert_favorite = response.getString("status_favorite");
                                                if (status_check_insert_favorite.equals("2")){
                                                    updateSavedAirdrop();
                                                }
                                                else if (status_check_insert_favorite.equals("1")){
                                                    updateDeletedAirdrop();
                                                }


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
                            Volley.newRequestQueue(context).add(jsonObjectRequest4);



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

        nestedScrollView = findViewById(R.id.nested_detail_movie);

        FloatingActionButton add_to_favorite = findViewById(R.id.fab_add_favorite);
        add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                final String url = "https://yoururl.com/api/insert_favorite.php?airdrop_id="+airdropId+"&is_airdrop=1&user_id="+user.getId()+"&promo_name="+ airdropTitle+"&airdrop_earn="+ airdropEarn;

                JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    status_check_insert_favorite = response.getString("status_favorite");
                                    if (status_check_insert_favorite.equals("1")){
                                        updateSavedAirdrop();
                                     //   Snackbar snackbar = Snackbar.make(nestedScrollView,  Html.fromHtml("<font color=\"#ffffff\">Dodano do ulubionych!</font>"), Snackbar.LENGTH_LONG);
                                  //      snackbar.show();
                                        Toasty.success(context, "Dodano z ulubionych!").show();

                                    }
                                    else if (status_check_insert_favorite.equals("2")){

                                        updateDeletedAirdrop();
                                    //    Snackbar snackbar = Snackbar.make(nestedScrollView, Html.fromHtml("<font color=\"#ffffff\">Usunięto z ulubionych.</font>"), Snackbar.LENGTH_LONG);
                                     //   snackbar.show();
                                        Toasty.success(context, "Usunięto z ulubionych.").show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                              //  Toast.makeText(context, "error"+url, Toast.LENGTH_SHORT).show();
                                Toasty.error(context, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

                            }
                        });
                Volley.newRequestQueue(context).add(jsonObjectRequest3);

            }
        });


        SenderAirdrop s=new SenderAirdrop(context,urlAddress,String.valueOf(airdropId));
        s.execute();

        Picasso.with(this)
                .load(getString(R.string.tmdb_image_airdrop_url, airdropImgUrl))
                .error( context.getDrawable( R.drawable.placeholder_logo ) )
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(mImgPoster);

        mTextTitle.setText(airdropTitle);
        if(airdropEarn.contains("."))
        {
            text_Price.setText(airdropEarn);
            text_PriceZero.setVisibility(View.GONE);
        }
        else{
            text_Price.setText(airdropEarn);
            text_PriceZero.setVisibility(View.VISIBLE);
        }
//        mTextTotalViews.setText(String.valueOf(promo.getPopularity()));
      //  mTextLikes.setText(String.valueOf(promo.getLikes()));
      //  mTextTotalDone.setText(String.valueOf(promo.getTotalDone()));

        instruction.setText( Html.fromHtml(airdropInstruction));
        mTextSynopsis.setText(airdropShortDesc);
        ///ładowanie reklamy
      /*  mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
*/
            text_tutorial_title.setVisibility(View.VISIBLE);
            global_const_bg.setBackgroundResource(R.drawable.shape_round);

    }

    @Override
    public void onButtonClicked(String text) {

    }

    public void getData3(int airdrop_id) {
        final String url25 = "https://yoururl.com/api/check_comments_count.php?airdrop_id="+airdrop_id+"&is_airdrop=1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url25, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            count_comments = response.getString("count_comments");
                            if (count_comments.equals("0")) {
                                // hide_if_zero.setVisibility(View.VISIBLE);
                                RvComment.setVisibility(View.GONE);
                                no_comments_yet.setVisibility(View.VISIBLE);

                                textInfoComment.setText("Brak komentarzy.");
                                // Toast.makeText(mContext, "zero", Toast.LENGTH_SHORT).show();

                            } else {
                                no_comments_yet.setVisibility(View.GONE);
                                RvComment.setVisibility(View.VISIBLE);

                                String comments = "Komentarze ("+count_comments+")";
                                textInfoComment.setText(comments);
                                // new AsyncReviews().execute();

                                AsyncComments asyncComment = new AsyncComments(airdrop_id);
                                asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

    @SuppressLint("StaticFieldLeak")
    private class AsyncComments extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn3;
        URL url3 = null;
        int airdropIdd3;

        LayoutInflater factory = LayoutInflater.from(context);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(context);
        public AsyncComments (int airdropIdd2) {
            airdropIdd3 = airdropIdd2;
        }

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
                url3 = new URL("https://yoururl.com/api/get_comments.php?airdrop_id="+airdropId+"&is_airdrop=1");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn3 = (HttpURLConnection) url3.openConnection();
                conn3.setReadTimeout(READ_TIMEOUT);
                conn3.setConnectTimeout(CONNECTION_TIMEOUT);
                conn3.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn3.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code3 = conn3.getResponseCode();

                // Check if successful connection made
                if (response_code3 == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input3 = conn3.getInputStream();
                    BufferedReader reader3 = new BufferedReader(new InputStreamReader(input3));
                    StringBuilder result3 = new StringBuilder();
                    String line3;

                    while ((line3 = reader3.readLine()) != null) {
                        result3.append(line3);
                    }

                    // Pass data to onPostExecute method
                    return (result3.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn3.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result3) {
            no_comments_yet.setVisibility(View.GONE);

            //this method will be running on UI thread
            // gif_loader.setVisibility(View.GONE);
            ArrayList<Comment> listComment = new ArrayList<>();

            FragmentManager fragmentManager = getSupportFragmentManager();
            RvComment = findViewById(R.id.rv_comment);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    main_dialog.dismiss();
                    handler.postDelayed(this, 1500);
                }
            }, 1000);


            try {

                JSONArray jArray = new JSONArray(result3);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Comment commentData = new Comment(null, null,null,null,null,null,null,null,"0",null,null,null);

                    commentData.commentId = json_data.getString("comment_id");
                    commentData.commentImage = json_data.getString("comment_img");
                    commentData.commentUserName = json_data.getString("comment_username");
                    commentData.commentContent = json_data.getString("comment_content");
                    commentData.commentDate = json_data.getString("comment_date");
                    commentData.hasreply = json_data.getString("comment_has_reply");
                    commentData.commentUserNameFirstReply = json_data.getString("comment_first_reply_username");
                    commentData.commentContentFirstReply = json_data.getString("comment_first_content_reply");
                    commentData.likes = json_data.getString("likes");
                    commentData.reply_to_id = json_data.getString("reply_to_id");
                    commentData.reply_to_reply_id = json_data.getString("reply_to_reply_id");
                    commentData.reply_to_username = json_data.getString("reply_to_username");

                    listComment.add(commentData);


                }
                if (listComment.isEmpty()){
                    RvComment.setVisibility(View.GONE);
                    no_comments_yet.setVisibility(View.VISIBLE);

                }
                else{
                    no_comments_yet.setVisibility(View.GONE);
                    RvComment.setVisibility(View.VISIBLE);
                    adapterComment = new AdapterComments(getApplicationContext(), listComment,fragmentManager);

                    RvComment.setAdapter(adapterComment);
                    RvComment.setLayoutManager(new LinearLayoutManager(context));
                }




            } catch (JSONException e) {
                e.printStackTrace();
                //mSwipeRefreshLayout.setRefreshing(false);
                // Toast.makeText(DoneActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }


        }
    }


    public void openChat(View view) {
        startActivity(new Intent(context, ChatSupportActivity.class));
    }




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_promo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public static void showDialogSuccess()
    {
        final Dialog dialog2 = new Dialog(context); // Context, this, etc.
        dialog2.setContentView(R.layout.dialog_done_success);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView click2 = (TextView) dialog2.findViewById(R.id.button_done_confirm_success);
        click2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }


    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }

    public void updateSavedAirdrop() {
        mFabAddFavorite.setImageDrawable(null);

        LottieAnimationView animationView
                = findViewById(R.id.lav_main);
        animationView.setVisibility(View.VISIBLE);

        animationView
                .addAnimatorUpdateListener(
                        (animation) -> {
                            // Do something.

                        });
        animationView
                .playAnimation();
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //  Log.e("Animation:","start");
                mFabAddFavorite.setImageDrawable(null);
                animationView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Log.e("Animation:","end");
                animationView.setVisibility(View.GONE);
                mFabAddFavorite.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(),R.drawable.ic_favorite_red_24dp));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //  Log.e("Animation:","cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //
                //      Log.e("Animation:","repeat");
            }
        });
    }

    public void updateDeletedAirdrop() {
        mFabAddFavorite.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(),R.drawable.ic_favorite_border_white_24dp));

    }
    public void showMessageNotification(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this,
                message,
                Toast.LENGTH_LONG);
        mToast.show();
    }
    public void openChat(){
        startActivity(new Intent(context, ChatSupportActivity.class));
    }

    public void openAirdropUrl(String promoUrl){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(promoUrl));
        context.startActivity(browserIntent);
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;


    }

    private void onBackgroundTaskDataObtained(ArrayList<Reply> results) {
        //do stuff with the results here..
        listReply = results;


    }
    /*
     public ArrayList<Reply> runAsyncReply(String position)
     {
         ProfitzApp mApp = ((ProfitzApp)getApplicationContext());
         ArrayList<Reply> listReply2 = new ArrayList<>();

         class AsyncReply extends AsyncTask<String, String, String> {
             // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
             HttpURLConnection conn2;
             URL url2 = null;
             String promoIdd;

             public AsyncReply (String promoIdd2) {
                 promoIdd = promoIdd2;
             }


             @Override
             protected void onPreExecute() {
                 super.onPreExecute();
                 //gif_loader.setVisibility(View.VISIBLE);

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
                     url2 = new URL("https://yoururl.com/api/get_replies.php?promo_id=" + promoIdd);

                 } catch (MalformedURLException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                     return e.toString();
                 }
                 try {

                     // Setup HttpURLConnection class to send and receive data from php and mysql
                     conn2 = (HttpURLConnection) url2.openConnection();
                     conn2.setReadTimeout(READ_TIMEOUT);
                     conn2.setConnectTimeout(CONNECTION_TIMEOUT);
                     conn2.setRequestMethod("GET");

                     // setDoOutput to true as we recieve data from json file
                     conn2.setDoOutput(true);

                 } catch (IOException e1) {
                     // TODO Auto-generated catch block
                     e1.printStackTrace();
                     return e1.toString();
                 }

                 try {

                     int response_code2 = conn2.getResponseCode();

                     // Check if successful connection made
                     if (response_code2 == HttpURLConnection.HTTP_OK) {

                         // Read data sent from server
                         InputStream input2 = conn2.getInputStream();
                         BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));
                         StringBuilder result2 = new StringBuilder();
                         String line2;

                         while ((line2 = reader2.readLine()) != null) {
                             result2.append(line2);
                         }

                         // Pass data to onPostExecute method
                         return (result2.toString());

                     } else {

                         return ("unsuccessful");
                     }

                 } catch (IOException e) {
                     e.printStackTrace();
                     return e.toString();
                 } finally {
                     conn2.disconnect();
                 }


             }

             @Override
             protected void onPostExecute(String result2) {
                 //this method will be running on UI thread
                 // gif_loader.setVisibility(View.GONE);


                 try {

                     JSONArray jArray = new JSONArray(result2);

                     // Extract data from json and store into ArrayList as class objects
                     for (int i = 0; i < jArray.length(); i++) {
                         JSONObject json_data = jArray.getJSONObject(i);
                         Reply replyData = new Reply(null,null,null,null,null,null,null);

                         replyData.replyId = json_data.getString("reply_id");
                         replyData.replyImage = json_data.getString("reply_img");
                         replyData.replyUserName = json_data.getString("reply_username");
                         replyData.replyContent = json_data.getString("reply_content");
                         replyData.replyDate = json_data.getString("reply_date");
                         replyData.hasreply = json_data.getString("reply_has_reply");
                         replyData.replyUserNameFirstReply = json_data.getString("reply_first_reply_username");
                         replyData.replyContentFirstReply = json_data.getString("reply_first_content_reply");
                         listReply.add(replyData);

                     }

                    // globalVariable.setGlobalVarValue(listReply);

                     PromoDetailActivity.this.onBackgroundTaskDataObtained(listReply);


                 } catch (JSONException e) {
                     e.printStackTrace();
                     //mSwipeRefreshLayout.setRefreshing(false);
                     // Toast.makeText(DoneActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                 }


             }
         }


        // AsyncReply asyncReply = new AsyncReply(position);
         // asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
       //  asyncReply.execute();
        // listReply2 = ProfitzApp.getInstance().getGlobalVarValue();

         return listReply;
     }
     */
    public class AdapterComments extends RecyclerView.Adapter<AdapterComments.CommentViewHolder> {

        private final Context mContext;
        private final LayoutInflater inflater2;
        private final FragmentManager fragmentManager;
        List<Comment> mData= Collections.emptyList();
        private final List<Comment> dataListFull;
        private AdapterReply replyAdapter;
        ProfitzApp mApp = ((ProfitzApp)getApplicationContext());



        public AdapterComments(Context mContext, List<Comment> mData, FragmentManager fragmentManager) {
            this.mContext = mContext;
            inflater2= LayoutInflater.from(mContext);
            this.mData = mData;
            this.fragmentManager = fragmentManager;
            dataListFull = new ArrayList<>(mData);
        }




        public String parseDateToddMMyyyy(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "dd MMM yyyy hh:mm";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }

        public String covertTimeToText(String dataDate) {

            String convTime = null;

            String prefix = "";
            String suffix = "temu";

            try {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date pasTime = dateFormat.parse(dataDate);


                Date nowTime = new Date();


                long dateDiff = nowTime.getTime() - pasTime.getTime();



                long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
                long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
                long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
                long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

                if (second < 60) {
                    if (second <=10){
                        convTime = "kilka sekund  "+suffix;

                    }
                    else{
                        convTime = "chwilę "+suffix;

                    }
                } else if (minute < 60) {
                    if (minute == 1){
                        convTime = " minutę "+suffix;

                    }
                    else if ((minute >= 2 ) && (minute <=4)){
                        convTime = minute + " minuty "+suffix;

                    }
                    else if ((minute >= 22 ) && (minute <=24)){
                        convTime = minute + " minuty "+suffix;

                    }
                    else if ((minute >= 32 ) && (minute <=34)){
                        convTime = minute + " minuty "+suffix;

                    }
                    else if ((minute >= 42 ) && (minute <=44)){
                        convTime = minute + " minuty "+suffix;

                    }
                    else if ((minute >= 52 ) && (minute <=54)){
                        convTime = minute + " minuty "+suffix;

                    }
                    else{
                        convTime = minute + " minut "+suffix;

                    }
                } else if (hour < 24) {
                    if (hour == 1){
                        convTime = " godzinę "+suffix;

                    }
                    else if ((hour >= 2 ) && (hour <=4)){
                        convTime = hour + " godziny "+suffix;

                    }
                    else if ((hour >= 22 ) && (hour <= 23)){
                        convTime = hour + " godziny "+suffix;

                    }
                    else{
                        convTime = hour + " godzin "+suffix;

                    }

                } else if (day >= 30) {

                    convTime = parseDateToddMMyyyy(dataDate);


                } else if (day < 30) {
                    if (day == 0)
                    {
                        convTime = "wczoraj";

                    }
                    else if (day == 1)
                    {
                        convTime = "dzień "+suffix;

                    }
                    else{
                        convTime = day+" dni "+suffix;

                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
                //    Log.e("ConvTimeE", e.getMessage());
            }

            return convTime;
        }


        @NonNull
        @Override
        public AdapterComments.CommentViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            View view=inflater2.inflate(R.layout.comment_layout, parent,false);

            return new AdapterComments.CommentViewHolder(view);

        }
        @Override
        public void onBindViewHolder(AdapterComments.CommentViewHolder holder, int fposition) {
            String imgUrl = mData.get(fposition).getUimg();
            Picasso.with( mContext )
                    .load( imgUrl )
                    .placeholder( mContext.getDrawable(  R.drawable.default_avatar ) )
                    .error( mContext.getDrawable( R.drawable.default_avatar ) )
                    .into( holder.comment_user_img );
            if (mData.get(fposition).hasReply().equals("1")){
                holder.replies.setVisibility(View.VISIBLE);
                holder.response.setVisibility(View.VISIBLE);
                holder.line2.setVisibility(View.VISIBLE);
                holder.list_item_genre_arrow.setVisibility(View.VISIBLE);
                holder.first_reply_img.setVisibility(View.VISIBLE);
                holder.first_reply_content.setVisibility(View.VISIBLE);
                holder.first_reply_username.setVisibility(View.VISIBLE);
                holder.first_reply_username.setText(mData.get(fposition).getUnameFirstReply());
                holder.first_reply_content.setText(mData.get(fposition).getFirstContentReply());

                String first_reply_user_img_link = "https://yoururl.com/api/images/users/"+mData.get(fposition).getUnameFirstReply()+".png";

                Picasso.with( mContext )
                        .load( first_reply_user_img_link )
                        .placeholder( mContext.getDrawable(  R.drawable.default_avatar ) )
                        .error( mContext.getDrawable( R.drawable.default_avatar ) )
                        .into( holder.first_reply_user_img );

                String url_count = "https://yoururl.com/api/get_replies_count.php?comment_id="+mData.get(fposition).getUid();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_count, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String count_replies = response.getString("count_replies");

                                    if (count_replies.equals("0")) {
                                        holder.response.setVisibility(View.GONE);

                                    } else if (count_replies.equals("1"))
                                    {
                                        holder.response.setVisibility(View.VISIBLE);
                                        holder.response.setText("Zobacz całą odpowiedź");

                                    }
                                    else{
                                        holder.response.setVisibility(View.VISIBLE);
                                        holder.response.setText("Zobacz wszystkie odpowiedzi ("+ count_replies+")");

                                    }

                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

                         */
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
                Volley.newRequestQueue(mContext).add(jsonObjectRequest);




                String url = "https://yoururl.com/api/get_replies.php?comment_id="+mData.get(fposition).getUid();
                ArrayList<Reply> listReply = new ArrayList<>();

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject jsonObject = response.getJSONObject(i);

                                Reply replyData = new Reply(null,null,null,null, null,null,null,null,"0",null,null,null);

                                replyData.replyId = jsonObject.getString("reply_id");
                                replyData.replyImage = jsonObject.getString("reply_img");
                                replyData.replyUserName = jsonObject.getString("reply_username");
                                replyData.replyContent = jsonObject.getString("reply_content");
                                replyData.replyDate = jsonObject.getString("reply_date");
                                replyData.hasreply = jsonObject.getString("reply_has_reply");
                                replyData.replyUserNameFirstReply = jsonObject.getString("reply_first_reply_username");
                                replyData.replyContentFirstReply = jsonObject.getString("reply_first_content_reply");
                                replyData.likes = jsonObject.getString("likes");
                                replyData.reply_to_id = jsonObject.getString("reply_to_id");
                                replyData.reply_to_reply_id = jsonObject.getString("reply_to_reply_id");
                                replyData.reply_to_username = jsonObject.getString("reply_to_username");
                                listReply.add(replyData);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        replyAdapter = new AdapterReply(mContext,listReply,fragmentManager,0, Integer.parseInt(airdropId), "1", Integer.parseInt(user2.getId()), username2,mData.get(fposition).getUid());
                        holder.recycler_replys.setAdapter(replyAdapter);
                        holder.recycler_replys.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

                        // replyAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                requestQueue.add(jsonArrayRequest);



                holder.replies.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.replies.setVisibility(View.GONE);
                        holder.recycler_replys.setVisibility(View.VISIBLE);


                    }
                });

            }
            else if (mData.get(fposition).hasReply().equals("0")){

                holder.replies.setVisibility(View.GONE);
                holder.response.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                holder.list_item_genre_arrow.setVisibility(View.GONE);
                holder.first_reply_img.setVisibility(View.GONE);
                holder.first_reply_username.setVisibility(View.GONE);
                holder.first_reply_username.setVisibility(View.GONE);
            }
            //  Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.img_user);
            holder.tv_name.setText(mData.get(fposition).getUname());

            String datetime = covertTimeToText(mData.get(fposition).getCommentDate());
            holder.tv_date.setText(datetime);


            if (mData.get(fposition).getUname().equals(username2)){
                holder.delete_comment.setVisibility(View.VISIBLE);
                holder.edit_comment.setVisibility(View.VISIBLE);
                holder.report_comment.setVisibility(View.GONE);

                holder.delete_comment.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();

                        BottomSheetDialogDeleteCommentConfirm bottomSheetComment = new BottomSheetDialogDeleteCommentConfirm();
                        args.putString("commentId",mData.get(fposition).getUid());
                        args.putString("promoId","0");
                        args.putString("airdropId",airdropId);
                        args.putString("isAirdrop","1");
                        args.putString("username",username2);
                        args.putString("userid", String.valueOf(user.getId()));
                        args.putString("content",mData.get(fposition).getContent());
                        args.putString("isreply","no");

                        bottomSheetComment.setArguments(args);
                        bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());





                    }
                });
                holder.edit_comment.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();

                        BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                        args.putString("commentId",mData.get(fposition).getUid());
                        args.putString("userId", String.valueOf(user.getId()));
                        args.putString("promoId","0");
                        args.putString("airdropId",airdropId);
                        args.putString("isAirdrop","1");
                        args.putString("username",username2);
                        args.putString("is_reply","yes");
                        args.putString("global_comm","no");
                        args.putString("reply_username",mData.get(fposition).getUname());
                        args.putString("reply_username_true","true");
                        args.putString("replyId",mData.get(fposition).getUid());
                        args.putString("comment_action","edit");
                        args.putString("comment_content",mData.get(fposition).getContent());

                        bottomSheetComment.setArguments(args);
                        bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());





                    }
                });

            }
            else{
                holder.delete_comment.setVisibility(View.GONE);
                holder.edit_comment.setVisibility(View.GONE);
                holder.report_comment.setVisibility(View.VISIBLE);
                holder.report_comment.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();

                        BottomSheetDialogReportComment bottomSheetReportComment = new BottomSheetDialogReportComment();
                        args.putString("commentId",mData.get(fposition).getUid());
                        args.putString("userId", String.valueOf(user.getId()));
                        args.putString("promoId","0");
                        args.putString("airdropId",airdropId);
                        args.putString("isAirdrop","1");
                        args.putString("username",username2);

                        bottomSheetReportComment.setArguments(args);
                        bottomSheetReportComment.show(getSupportFragmentManager(), bottomSheetReportComment.getTag());




                    }
                });




            }


            if (mData.get(fposition).getReplyToReplyId().equals("0")){
                holder.tv_content.setText(mData.get(fposition).getContent());
            }
            else{
                Spanned content_add_username = Html.fromHtml("<font color=\"#2E51A7\">@"+mData.get(fposition).getReplyToUsername()+"</font><font color=\"#000000\"> "+mData.get(fposition).getContent()+"</font>");
                holder.tv_content.setText(content_add_username);

            }



            String url_check_like_dislike = "https://yoururl.com/api/check_comment_like_dislike.php?comment_id="+mData.get(fposition).getUid()+"&user_id="+user.getId();

            JsonObjectRequest jsonObjectRequest21 = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url_check_like_dislike, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String response2 = response.getString("response");

                                if (response2.equals("0")) {
                                    holder.like_full.setVisibility(View.GONE);
                                    holder.like_outline.setVisibility(View.VISIBLE);

                                } else if (response2.equals("1"))
                                {
                                    holder.like_outline.setVisibility(View.GONE);
                                    holder.like_full.setVisibility(View.VISIBLE);

                                }

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
            Volley.newRequestQueue(mContext).add(jsonObjectRequest21);



            holder.reply.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();

                    BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                    args.putString("commentId",mData.get(fposition).getUid());
                    args.putString("userId", String.valueOf(user.getId()));
                    args.putString("promoId","0");
                    args.putString("isAirdrop","1");
                    args.putString("airdropId",airdropId);
                    args.putString("username",username2);
                    args.putString("is_reply","yes");
                    args.putString("global_comm","no");
                    args.putString("reply_username",mData.get(fposition).getUname());
                    args.putString("reply_username_true","true");
                    args.putString("replyId",mData.get(fposition).getUid());
                    args.putString("comment_action","add");

                    bottomSheetComment.setArguments(args);
                    bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());

                }
            });








            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url_like_dislike = "https://yoururl.com/api/update_comment_like_dislike.php?is_aidrop=1&airdrop_id="+airdropId+"&comment_id="+mData.get(fposition).getUid()+"&user_id="+user.getId();

                    JsonObjectRequest jsonObjectRequest22 = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url_like_dislike, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        String response2 = response.getString("response");
                                        String likes = response.getString("likes");

                                        if (response2.equals("0")) {
                                            // String likes = mData.get(fposition).getLikes();

                                            int new_likes = Integer.parseInt(likes.trim())-1;
                                            String new_likes_string;

                                            if (new_likes == 1){
                                                new_likes_string = new_likes + " polubienie";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else if (new_likes == 2){
                                                new_likes_string = new_likes + " polubienia";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else if (new_likes == 3){
                                                new_likes_string = new_likes + " polubienia";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else if (new_likes == 4){
                                                new_likes_string = new_likes + " polubienia";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else {
                                                new_likes_string = new_likes + " polubień";
                                                holder.count.setText(new_likes_string);
                                            }


                                            holder.like_full.setVisibility(View.GONE);
                                            holder.like_outline.setVisibility(View.VISIBLE);

                                        } else if (response2.equals("1"))
                                        {
                                            holder.like_outline.setVisibility(View.GONE);
                                            holder.like_full.setVisibility(View.VISIBLE);
                                            // String likes = mData.get(fposition).getLikes();
                                            int new_likes = Integer.parseInt(likes.trim())+1;
                                            String new_likes_string;

                                            if (new_likes == 1){
                                                new_likes_string = new_likes + " polubienie";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else if (new_likes == 2){
                                                new_likes_string = new_likes + " polubienia";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else if (new_likes == 3){
                                                new_likes_string = new_likes + " polubienia";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else if (new_likes == 4){
                                                new_likes_string = new_likes + " polubienia";
                                                holder.count.setText(new_likes_string);
                                            }
                                            else {
                                                new_likes_string = new_likes + " polubień";
                                                holder.count.setText(new_likes_string);
                                            }



                                        }

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
                    Volley.newRequestQueue(mContext).add(jsonObjectRequest22);






                }
            });
            String likes = String.valueOf(mData.get(fposition).getLikes());

            if (likes.equals("1")){
                String new_likes_string = likes + " polubienie";
                holder.count.setText(new_likes_string);
            }
            else if (likes.equals("2")){
                String new_likes_string = likes + " polubienia";
                holder.count.setText(new_likes_string);
            }
            else if (likes.equals("3")){
                String new_likes_string = likes + " polubienia";
                holder.count.setText(new_likes_string);
            }
            else if (likes.equals("4")){
                String new_likes_string = likes + " polubienia";
                holder.count.setText(new_likes_string);
            }
            else {
                String new_likes_string = likes + " polubień";
                holder.count.setText(new_likes_string);
            }





        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public class CommentViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recycler_replys;
            CircleImageView img_user,first_reply_img;
            TextView tv_name,tv_content,tv_date,first_reply_username, first_reply_content, response,reply, count;
            RelativeLayout replies;

            RelativeLayout like;
            ImageView like_outline;
            ImageView like_full;
            TextView delete_comment;
            TextView edit_comment;
            TextView report_comment;
            View line2;
            ImageView list_item_genre_arrow;
            CircleImageView comment_user_img;
            CircleImageView first_reply_user_img;
            public CommentViewHolder(View itemView) {
                super(itemView);
                img_user = itemView.findViewById(R.id.comment_user_img);
                tv_name = itemView.findViewById(R.id.comment_username);
                tv_content = itemView.findViewById(R.id.comment_content);
                tv_date = itemView.findViewById(R.id.comment_date);
                first_reply_img = itemView.findViewById(R.id.first_reply_user_img);
                first_reply_username = itemView.findViewById(R.id.first_reply_username);
                first_reply_content = itemView.findViewById(R.id.first_reply_content);
                replies = itemView.findViewById(R.id.replies);
                line2 = itemView.findViewById(R.id.line2);
                response = itemView.findViewById(R.id.response);
                list_item_genre_arrow = itemView.findViewById(R.id.list_item_genre_arrow);
                recycler_replys = itemView.findViewById(R.id.recycler_replys);
                like = itemView.findViewById(R.id.like);
                like_full = itemView.findViewById(R.id.like_full);
                like_outline = itemView.findViewById(R.id.like_outline);
                count = itemView.findViewById(R.id.count);
                reply = itemView.findViewById(R.id.reply);
                delete_comment = itemView.findViewById(R.id.delete);
                edit_comment = itemView.findViewById(R.id.edit);
                report_comment = itemView.findViewById(R.id.report);
                comment_user_img = itemView.findViewById(R.id.comment_user_img);
                first_reply_user_img = itemView.findViewById(R.id.first_reply_user_img);

            }
        }



        private String timestampToString(long time) {

            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(time);
            String date = DateFormat.format("hh:mm",calendar).toString();
            return date;


        }


    }

    public void shareAirdrop(String airdrop) {
        String mimeType = "text/plain";
        String title = getString(R.string.share_dialog_title);
        String space = " ";

        String message = getString(R.string.sharing_text, airdrop) +
                space;

        //if (!video.isEmpty()) {
        //   message.append(getString(R.string.youtube_url));
        //  message.append(video);
        //   }

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(message)
                .startChooser();
    }

/*
    @OnClick(R.id.fab_add_favorite)
    public void addFavorite(View view) {
        mPromoDetailPresenter.saveOrDeletePromoAsFavorite();
    }
*/
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




}






















