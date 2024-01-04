package com.profitz.app.promodetail;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.GetReflink;
import com.profitz.app.ProfitzApp;
import com.profitz.app.R;
import com.profitz.app.RefClicked;
import com.profitz.app.data.model.Comment;
import com.profitz.app.data.model.Promo;
import com.profitz.app.data.model.Reply;
import com.profitz.app.data.source.Repository;
import com.profitz.app.data.source.local.LocalDataSource;
import com.profitz.app.data.source.remote.RemoteDataSource;
import com.profitz.app.promos.CustomLinearLayoutManager;
import com.profitz.app.promos.activities.BuyLicenceActivity;
import com.profitz.app.promos.activities.ChatSupportActivity;
import com.profitz.app.promos.adapters.AdapterReply;
import com.profitz.app.promos.adapters.AdapterReview;
import com.profitz.app.promos.data.DataReview;
import com.profitz.app.promos.data.DoneSender;
import com.profitz.app.promos.data.ReviewSender;
import com.profitz.app.promos.fragments.BottomSheetDialogAboutReviews;
import com.profitz.app.promos.fragments.BottomSheetDialogAddComment;
import com.profitz.app.promos.fragments.BottomSheetDialogDeleteCommentConfirm;
import com.profitz.app.promos.fragments.BottomSheetDialogInsertDone;
import com.profitz.app.promos.fragments.BottomSheetDialogInstructions;
import com.profitz.app.promos.fragments.BottomSheetDialogReportComment;
import com.profitz.app.promos.fragments.BottomSheetDialogReportIssue;
import com.profitz.app.promos.fragments.BottomSheetDialogReviews;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.data.Sender;
import com.profitz.app.promos.User;
import com.profitz.app.util.schedulers.SchedulerProvider;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class PromoDetailActivity < tmdb_image_url > extends AppCompatActivity implements PromoDetailContract.View, BottomSheetDialogReviews.BottomSheetListener, BottomSheetDialogInstructions.BottomSheetListener, BottomSheetDialogInsertDone.BottomSheetListener, BottomSheetDialogAddComment.BottomSheetListener, BottomSheetDialogReportComment.BottomSheetListener, BottomSheetDialogDeleteCommentConfirm.BottomSheetListener, BottomSheetDialogAboutReviews.BottomSheetListener {

    //public static final String INTENT_EXTRA_MOVIE = "api";
    public static final String INTENT_EXTRA_PROMO = "promo";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    private RecyclerView mRVReviewList;
    private AdapterReview mAdapter;
    RecyclerView ReviewList;
    TextView add_review;
    TextView see_all_reviews;
    String count_reviews;
    String count_comments;
    List < DataReview > data;
    String promoStatus;
    String promoBonus;
    String promoAdditionalInfo;
    String promoReceivedPoints;
    ImageView no_comments_yet;
    String status_check_stars;
    TextView textInfo;
    TextView textInfoComment;
    Timer timer;
    int IDuser;
    public int promoId;
    public String promoName;
    public float promoRating;
    public String promoDesc;
    public static int promoId2;
    public static String promoName2;
    public static float promoRating2;

    private InterstitialAd mInterstitialAd;
    String promoOrginalTitle;
    String promoTyp;
    String promoPrice;
    private PromoDetailContract.Presenter mPromoDetailPresenter;
    static Context context;
    float ratingg;
    RatingBar ratingBar;
    RatingBar ratingBar2;
    LayoutInflater factory;
    View DialogView;
    Dialog main_dialog;
    String data_getLevel;
    TextView textInfo2;

    ImageView imgPost, imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostTitle;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;
    AdapterReply replyAdapter;
    Bundle args;

    RecyclerView RvComment;
    AdapterComment adapterComment;
    static String COMMENT_KEY = "Comment";

    public ArrayList < Reply > listReply = new ArrayList < > ();
    Handler handler2;
    Runnable runnable;
    boolean isRunning = false;

    LinearLayoutManager linearLayoutManager;

    ProfitzApp globalVariable;
    @BindView(R.id.report_issue_promo)
    LinearLayout report_issue_promo;

    @BindView(R.id.text_tutorial_title)
    TextView text_tutorial_title;

    @BindView(R.id.global_const_bg)
    ConstraintLayout global_const_bg;

    @BindView(R.id.text_PriceZero)
    TextView text_PriceZero;

    @BindView(R.id.arrow_back)
    ImageView arrow_back;

    @BindView(R.id.text_rating_score_five)
    TextView text_rating_score_five;

    @BindView(R.id.img_cover)
    ImageView mImgCover;

    @BindView(R.id.img_poster)
    CircleImageView mImgPoster;

    @BindView(R.id.fab_add_favorite)
    FloatingActionButton mFabAddFavorite;

    @BindView(R.id.fab_add_licence)
    FloatingActionButton mFabAddLicence;

    @BindView(R.id.text_title)
    TextView mTextTitle;

    @BindView(R.id.text_Price_zl)
    TextView text_Price_zl;

    @BindView(R.id.text_release_date)
    TextView mTextReleaseDate;

    @BindView(R.id.text_rating_score)
    TextView mTextRatingScore;

    @BindView(R.id.text_total_views)
    TextView mTextTotalViews;

    @BindView(R.id.text_total_favourite)
    TextView mTextLikes;

    @BindView(R.id.text_total_done)
    TextView mTextTotalDone;

    @BindView(R.id.rating_score)
    RatingBar mRatingScore;

    @BindView(R.id.rv_videos)
    RecyclerView mRvVideos;

    @BindView(R.id.text_synopsis)
    TextView mTextSynopsis;

    @BindView(R.id.text_short_desc)
    TextView mTextShortDesc;

    @BindView(R.id.text_Price)
    TextView mTextPrice;

    @BindView(R.id.text_how_long)
    TextView mTextHowLong;

    @BindView(R.id.text_step1)
    TextView mTextStep1;

    @BindView(R.id.text_step1_1)
    TextView mTextStep1_1;
    @BindView(R.id.text_step1_2)
    TextView mTextStep1_2;
    @BindView(R.id.text_step1_3)
    TextView mTextStep1_3;
    @BindView(R.id.text_step1_4)
    TextView mTextStep1_4;
    @BindView(R.id.text_step1_5)
    TextView mTextStep1_5;
    @BindView(R.id.text_step1_6)
    TextView mTextStep1_6;
    @BindView(R.id.text_step1_7)
    TextView mTextStep1_7;

    @BindView(R.id.text_step2)
    TextView mTextStep2;

    @BindView(R.id.text_step2_1)
    TextView mTextStep2_1;
    @BindView(R.id.text_step2_2)
    TextView mTextStep2_2;
    @BindView(R.id.text_step2_3)
    TextView mTextStep2_3;
    @BindView(R.id.text_step2_4)
    TextView mTextStep2_4;
    @BindView(R.id.text_step2_5)
    TextView mTextStep2_5;
    @BindView(R.id.text_step2_6)
    TextView mTextStep2_6;
    @BindView(R.id.text_step2_7)
    TextView mTextStep2_7;

    @BindView(R.id.text_step3)
    TextView mTextStep3;

    @BindView(R.id.text_step3_1)
    TextView mTextStep3_1;
    @BindView(R.id.text_step3_2)
    TextView mTextStep3_2;
    @BindView(R.id.text_step3_3)
    TextView mTextStep3_3;
    @BindView(R.id.text_step3_4)
    TextView mTextStep3_4;
    @BindView(R.id.text_step3_5)
    TextView mTextStep3_5;
    @BindView(R.id.text_step3_6)
    TextView mTextStep3_6;
    @BindView(R.id.text_step3_7)
    TextView mTextStep3_7;

    @BindView(R.id.text_step4)
    TextView mTextStep4;

    @BindView(R.id.text_step4_1)
    TextView mTextStep4_1;
    @BindView(R.id.text_step4_2)
    TextView mTextStep4_2;
    @BindView(R.id.text_step4_3)
    TextView mTextStep4_3;
    @BindView(R.id.text_step4_4)
    TextView mTextStep4_4;
    @BindView(R.id.text_step4_5)
    TextView mTextStep4_5;
    @BindView(R.id.text_step4_6)
    TextView mTextStep4_6;
    @BindView(R.id.text_step4_7)
    TextView mTextStep4_7;

    @BindView(R.id.text_step5)
    TextView mTextStep5;

    @BindView(R.id.button_step5)
    TextView button_step5;

    @BindView(R.id.text_step5_1)
    TextView mTextStep5_1;
    @BindView(R.id.text_step5_2)
    TextView mTextStep5_2;
    @BindView(R.id.text_step5_3)
    TextView mTextStep5_3;
    @BindView(R.id.text_step5_4)
    TextView mTextStep5_4;
    @BindView(R.id.text_step5_5)
    TextView mTextStep5_5;
    @BindView(R.id.text_step5_6)
    TextView mTextStep5_6;
    @BindView(R.id.text_step5_7)
    TextView mTextStep5_7;

    @BindView(R.id.text_step6)
    TextView mTextStep6;

    @BindView(R.id.button_step6)
    TextView button_step6;

    @BindView(R.id.text_step6_1)
    TextView mTextStep6_1;
    @BindView(R.id.text_step6_2)
    TextView mTextStep6_2;
    @BindView(R.id.text_step6_3)
    TextView mTextStep6_3;
    @BindView(R.id.text_step6_4)
    TextView mTextStep6_4;
    @BindView(R.id.text_step6_5)
    TextView mTextStep6_5;
    @BindView(R.id.text_step6_6)
    TextView mTextStep6_6;
    @BindView(R.id.text_step6_7)
    TextView mTextStep6_7;

    @BindView(R.id.text_step7)
    TextView mTextStep7;

    @BindView(R.id.button_step7)
    TextView button_step7;

    @BindView(R.id.text_step7_1)
    TextView mTextStep7_1;
    @BindView(R.id.text_step7_2)
    TextView mTextStep7_2;
    @BindView(R.id.text_step7_3)
    TextView mTextStep7_3;
    @BindView(R.id.text_step7_4)
    TextView mTextStep7_4;
    @BindView(R.id.text_step7_5)
    TextView mTextStep7_5;
    @BindView(R.id.text_step7_6)
    TextView mTextStep7_6;
    @BindView(R.id.text_step7_7)
    TextView mTextStep7_7;

    @BindView(R.id.text_stepX)
    TextView mTextStepX;

    @BindView(R.id.layout_text_instructions)
    LinearLayout layout_text_instructions;

    @BindView(R.id.layout_aion)
    LinearLayout layout_aion;

    @BindView(R.id.button_want_reffer_aion)
    TextView button_want_reffer_aion;
    HashMap < String, Object > attributesMap;

    private Toast mToast;
    static FloatingActionButton mFabAddDone;

    private String status_check_done;
    private String status_check_insert_favorite;
    private String status_check_done_one;
    double user_balance;
    double user_id;
    private static PromoDetailActivity mInstance;
    private NestedScrollView nestedScrollView;
    RelativeLayout rl_reviews;
    ConstraintLayout layout_reviews;
    User user;
    static ConstraintLayout constraint_detail;
    String username;
    String username2;
    TextView add_review_top;

    public int position = 0;
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
        setContentView(R.layout.activity_promo_detail);
        ButterKnife.bind(this);
        mInstance = this;
        context = this;
        globalVariable = (ProfitzApp) getApplicationContext();

        factory = LayoutInflater.from(PromoDetailActivity.this);
        DialogView = factory.inflate(R.layout.custom_load_layout, null);
        main_dialog = new Dialog(PromoDetailActivity.this);
        user = MyPreferenceManager.getInstance(context).getUser();
        constraint_detail = findViewById(R.id.constraint_detail);
        username = user.getUsername();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        attributesMap = new HashMap < String, Object > ();
        attributesMap.put("punkty", user.getPoints());
        attributesMap.put("wykonane", user.getDone_count());
        attributesMap.put("zarobione", user.getEarned());

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        //mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3720384080622115/7409282237");
        mFabAddDone = findViewById(R.id.fab_add_done);

        //   setUpReviews();

        TextView add_comment = findViewById(R.id.add_comment);

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();

                BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                args.putString("commentId", "0");
                args.putString("userId", String.valueOf(user_id));
                args.putString("promoId", String.valueOf(promoId));
                args.putString("username", username2);
                args.putString("is_reply", "no");
                args.putString("airdropId", "0");
                args.putString("isAirdrop", "0");
                args.putString("global_comm", "yes");
                args.putString("comment_action", "add");

                bottomSheetComment.setArguments(args);
                bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());

            }
        });

        ReviewList = findViewById(R.id.ReviewsList);
        mRVReviewList = findViewById(R.id.ReviewsList);
        user2 = MyPreferenceManager.getInstance(context).getUser();
        IDuser = Integer.parseInt(user2.getId());
        username = user2.getName();
        username2 = user2.getUsername();
        linearLayoutManager = new CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        textInfo2 = findViewById(R.id.textInfo2);

        textInfo = findViewById(R.id.textInfo);
        textInfoComment = findViewById(R.id.textInfoComment);
        add_review = findViewById(R.id.add_review);
        no_comments_yet = findViewById(R.id.no_comments_yet);
        see_all_reviews = findViewById(R.id.see_all_reviews);

        rl_reviews = findViewById(R.id.rl_reviews);
        layout_reviews = findViewById(R.id.layout_reviews);
        add_review_top = findViewById(R.id.add_review_top);
        textInfo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogAboutReviews bottomSheetAboutReviews = new BottomSheetDialogAboutReviews();

                bottomSheetAboutReviews.show(getSupportFragmentManager(), bottomSheetAboutReviews.getTag());
            }
        });

        see_all_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogReviews bottomSheet = new BottomSheetDialogReviews();

                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });
        hideLoading();

    }
    public void commentAdded(int promo_id) {
        // Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Komentarz został dodany.</font>"), Snackbar.LENGTH_LONG);
        //  snackbar.show();
        Toasty.success(context, "Komentarz został dodany.").show();

        AsyncComment asyncComment = new AsyncComment(promo_id);
        asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        getData3(promoId);
        runSlider();

        // asyncComment.execute();
    }
    public void commentDeleted(int promo_id) {
        //Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Komentarz został usunięty.</font>"), Snackbar.LENGTH_LONG);
        // snackbar.show();
        Toasty.success(context, "Komentarz został usunięty.").show();

        AsyncComment asyncComment = new AsyncComment(promo_id);
        asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        getData3(promoId);
        runSlider();

        // asyncComment.execute();
    }

    public void commentEdited(int promo_id) {
        //  Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Komentarz został zaktualizowany.</font>"), Snackbar.LENGTH_LONG);
        //  snackbar.show();
        Toasty.success(context, "Komentarz został zaktualizowany.").show();

        AsyncComment asyncComment = new AsyncComment(promo_id);
        asyncComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        // asyncComment.execute();
        runSlider();

    }
    public void reportSent() {
        //Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Zgłoszenie zostało wysłane. Dziękujemy!</font>"), Snackbar.LENGTH_LONG);
        //snackbar.show();
        Toasty.success(context, "Zgłoszenie zostało wysłane. Dziękujemy!").show();

        runSlider();

        // asyncComment.execute();
    }
    @Override
    public void showPromo(Promo promo) {
        //setTitle(promo.getOriginalTitle());
        String urlAddress = "https://yoururl.com/api/update_popularity.php";
        User user = MyPreferenceManager.getInstance(this).getUser();
        promoName = promo.getOriginalTitle();
        promoId = promo.getId();
        promoRating = promo.getVoteAverage();
        promoOrginalTitle = promo.getOriginalTitle();
        promoTyp = promo.getPromoType();
        promoPrice = promo.getPrice();
        promoDesc = promo.getShortDesc();
        FloatingActionButton fab = findViewById(R.id.fab_add_done);
        promoId2 = promoId;
        promoRating2 = promoRating;
        promoName2 = promoName;
        // CachePot.getInstance().push("name", promoName);
        //  CachePot.getInstance().push("id", promoId);
        // CachePot.getInstance().push("rating", promoRating);
        add_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://yoururl.com/api/get_done_promo_info.php?user_id=" + IDuser + "&promo_id=" + promoId;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            promoStatus = response.getString("promoStatus");
                            promoBonus = response.getString("promoBonus");
                            promoAdditionalInfo = response.getString("promoAdditionalInfo");
                            promoReceivedPoints = response.getString("promoReceivedPoints");
                            // Log.e("dad",promoStatus);
                            if (promoStatus.equals("zatwierdzono")) {
                                if (promoReceivedPoints.equals("no")) {
                                    openDialogReview(promoName, promoId, promoBonus);
                                }
                                if (promoReceivedPoints.equals("yes")) {
                                    if (username.substring(username.length() - 1).equals("a")) {
                                        // Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Dodałaś już opinię do tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                        // snackbar.show();
                                        Toasty.normal(context, "Dodałaś już opinię do tej promocji.").show();

                                    } else {
                                        //     Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Dodałeś już opinię do tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                        //    snackbar.show();
                                        Toasty.normal(context, "Dodałeś już opinię do tej promocji.").show();

                                    }

                                }
                            } else if (promoStatus.equals("w trakcie weryfikacji")) {
                                //  Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Możesz dodać opinię dopiero po ukończeniu tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                // snackbar.show();
                                Toasty.normal(context, "Możesz dodać opinię dopiero po ukończeniu tej promocji.").show();

                            } else {
                                //   Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Możesz dodać opinię dopiero po ukończeniu tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                //     snackbar.show();
                                Toasty.normal(context, "Możesz dodać opinię dopiero po ukończeniu tej promocji.").show();

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
                Volley.newRequestQueue(context).add(jsonObjectRequest);

            }
        });

        add_review_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://yoururl.com/api/get_done_promo_info.php?user_id=" + IDuser + "&promo_id=" + promoId;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            promoStatus = response.getString("promoStatus");
                            promoBonus = response.getString("promoBonus");
                            promoAdditionalInfo = response.getString("promoAdditionalInfo");
                            promoReceivedPoints = response.getString("promoReceivedPoints");
                            //  Log.e("dad",promoStatus);
                            if (promoStatus.equals("zatwierdzono")) {
                                if (promoReceivedPoints.equals("no")) {
                                    openDialogReview(promoName, promoId, promoBonus);
                                }
                                if (promoReceivedPoints.equals("yes")) {
                                    if (username.substring(username.length() - 1).equals("a")) {
                                        //   Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Dodałaś już opinię do tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                        //  snackbar.show();
                                        Toasty.normal(context, "Dodałaś już opinię do tej promocji.").show();

                                    } else {
                                        // Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Dodałeś już opinię do tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                        //snackbar.show();
                                        Toasty.normal(context, "Dodałeś już opinię do tej promocji.").show();

                                    }

                                }
                            } else if (promoStatus.equals("w trakcie weryfikacji")) {
                                //  Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Możesz dodać opinię dopiero po ukończeniu tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                //  snackbar.show();
                                Toasty.normal(context, "Możesz dodać opinię dopiero po ukończeniu tej promocji.").show();

                            } else {
                                //Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Możesz dodać opinię dopiero po ukończeniu tej promocji.</font>"), Snackbar.LENGTH_LONG);
                                //snackbar.show();
                                Toasty.normal(context, "Możesz dodać opinię dopiero po ukończeniu tej promocji.").show();

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
                Volley.newRequestQueue(context).add(jsonObjectRequest);

            }
        });

        getData2(promoId);

        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                final String url_check_promo_star_status = "https://yoururl.com/api/check_promo_star_status.php?promo_id=" + promo.getId();
                JsonObjectRequest jsonObjectRequestx = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_check_promo_star_status, null, new Response.Listener < JSONObject > () {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status_check_stars = response.getString("status");
                            if (status_check_stars.equals("0")) {
                                addDoneNonStar();

                            } else {
                                openPromoDoneDialog(view);
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
                Volley.newRequestQueue(context).add(jsonObjectRequestx);

            }

        });

        Button want_rules = findViewById(R.id.button_want_rules);
        want_rules.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String url_to_rules = promo.getPromoRulesUrl();
                Intent ir = new Intent(Intent.ACTION_VIEW);
                ir.setData(Uri.parse(url_to_rules));
                startActivity(ir);
            }
        });

        TextView want_refer = findViewById(R.id.button_want_reffer);
        want_refer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String url_to_check_promo = "https://yoururl.com/api/get_promo_url.php?promo_id=" + promo.getId() + "&user_id=" + user.getId();

                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_to_check_promo, null, new Response.Listener < JSONObject > () {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String get_promo_url = response.getString("url");
                            // TODO Auto-generated method stub
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(get_promo_url));
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
        String url = "https://yoururl.com/api/get_user_data.php?user_id=" + user.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject response) {


                String url_update_average = "https://yoururl.com/api/update_average.php?promo_id=" + promo.getId();

                JsonObjectRequest jsonObjectRequest8 = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_update_average, null, new Response.Listener < JSONObject > () {

                    @Override
                    public void onResponse(JSONObject response) {

                         String url_check_done_status = "https://yoururl.com/api/check_done_clicked.php?promo_id=" + promo.getId() + "&user_id=" + user.getId();
                        JsonObjectRequest jsonObjectRequest6 = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_check_done_status, null, new Response.Listener < JSONObject > () {

                            @Override
                            public void onResponse(JSONObject response) {
                                 String url_check_licence_status = "https://yoururl.com/api/check_licence_exists.php?promo_id=" + promo.getId() + "&user_id=" + user.getId();
                                JsonObjectRequest jsonObjectRequest_check_licence = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_check_licence_status, null, new Response.Listener < JSONObject > () {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                         String url2 = "https://yoururl.com/api/check_favorite.php?promo_id=" + promo.getId() + "&user_id=" + user.getId() + "&promo_name=" + promo.getOriginalTitle()+"&is_airdrop=0&airdrop_id=null&airdrop_name=null";

                                        JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(com.android.volley.Request.Method.GET, url2, null, new Response.Listener < JSONObject > () {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    status_check_insert_favorite = response.getString("status_favorite");
                                                    if (status_check_insert_favorite.equals("2")) {
                                                        updateSavedPromo();

                                                    } else if (status_check_insert_favorite.equals("1")) {
                                                        updateDeletedPromo();
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

                                        try {
                                            String status_check_licence_before = response.getString("status");
                                            if (status_check_licence_before.equals("1")) {
                                                updateLicence();
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
                                Volley.newRequestQueue(context).add(jsonObjectRequest_check_licence);

                                try {
                                    status_check_done_one = response.getString("status");
                                    if (!status_check_done_one.equals("0")) {
                                        updateDonePromo();
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
                        Volley.newRequestQueue(context).add(jsonObjectRequest6);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });
                Volley.newRequestQueue(context).add(jsonObjectRequest8);

                try {

                    String data_getPoints = response.getString("points");
                    user_balance = Double.parseDouble(data_getPoints);
                    String user_id_string = response.getString("user_id");
                    user_id = Double.parseDouble(user_id_string);
                    data_getLevel = response.getString("user_level");

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

        double licence_cost_before_calc = Double.parseDouble(promo.getPrice()); //pobiera zarobek z danej promocji
        double licence_cost_calc1 = (licence_cost_before_calc / 2.5) * 0.15; // kalkuluje procent
        double licence_cost = (licence_cost_before_calc / 2.5) + licence_cost_calc1; // dzieli zarobek z promocji na 2.5 i dodaje procent

        String decimal_licence_cost_string = df.format(licence_cost);
        //   String licence_cost_string = Double.valueOf(decimal_licence_cost_string).toString(); // zamienia double na string by podliczyć i zaokragla do 0.0

        FloatingActionButton add_licence = findViewById(R.id.fab_add_licence);
        add_licence.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PromoDetailActivity.this, BuyLicenceActivity.class));
            }
      /* public void onClick(View view) {
           final String url_check_licence = "https://yoururl.com/api/check_licence_exists.php?promo_id="+ promo.getId()+"&user_id="+user_id+"";

           JsonObjectRequest jsonObjectRequest_check_licence = new JsonObjectRequest
                   (com.android.volley.Request.Method.GET, url_check_licence, null, new Response.Listener<JSONObject>() {

                       @Override
                       public void onResponse(JSONObject response) {
                           try {
                               String status_check_licence = response.getString("status");

                               if(status_check_licence.equals("1")){
                                   showDialogSuccessLicence();
                                   updateLicence();
                               }
                               else if(status_check_licence.equals("0")) {
                                   final Dialog dialog_licence = new Dialog(context); // Context, this, etc.
                                   dialog_licence.setContentView(R.layout.dialog_add_licence);
                                   dialog_licence.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                   TextView dialog_licence_name = (TextView) dialog_licence.findViewById(R.id.dialog_licence_name);
                                   dialog_licence_name.setText(promo.getOriginalTitle());

                                   Context context_dialog = dialog_licence.getContext();

                                   EditText dialog_licence_link = (EditText) dialog_licence.findViewById(R.id.dialog_licence_link);

                                   ImageView licence_image = (ImageView) dialog_licence.findViewById(R.id.iv_dialog_licence);
                                   Picasso.with(context_dialog)
                                           .load(getString(R.string.tmdb_image_url, promo.getPosterPath()))
                                           .into(licence_image);


                                   TextView dialog_licence_cost = (TextView) dialog_licence.findViewById(R.id.cost);
                                   dialog_licence_cost.setText(decimal_licence_cost_string);
                                   RelativeLayout rl_needpoints = (RelativeLayout) dialog_licence.findViewById(R.id.rl_needpoints);
                                   TextView need_points = (TextView) dialog_licence.findViewById(R.id.need_points);

                                   Button button_licence_add_points = (Button) dialog_licence.findViewById(R.id.button_licence_add_points);
                                   button_licence_add_points.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           dialog_licence.dismiss();
                                           Intent intent = new Intent(PromoDetailActivity.this, PointsActivity.class);
                                           intent.putExtra("openTopUp", 1); //for example
                                           startActivity(intent);
                                       }
                                   });


                                   Button click_accept = (Button) dialog_licence.findViewById(R.id.button_licence_confirm);
                                   click_accept.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {

                                           final String url_insert_licence = "https://yoururl.com/api/insert_licence.php?promo_id=" + promo.getId() + "&user_name=" + user.getName() + "&licence_cost=" + licence_cost + "&user_id=" + user.getId() + "&promo_name=" + promo.getOriginalTitle() + "&url=" + dialog_licence_link.getText().toString();

                                           JsonObjectRequest jsonObjectRequest_licence = new JsonObjectRequest
                                                   (com.android.volley.Request.Method.GET, url_insert_licence, null, new Response.Listener<JSONObject>() {

                                                       @Override
                                                       public void onResponse(JSONObject response) {
                                                           try {
                                                               String status_licence = response.getString("status_licence");
                                                               if (status_licence.equals("0")) {
                                                                   updateLicence();
                                                                   showDialogSuccessLicence();
                                                               }


                                                           } catch (JSONException e) {
                                                               e.printStackTrace();
                                                           }

                                                       }
                                                   }, new Response.ErrorListener() {

                                                       @Override
                                                       public void onErrorResponse(VolleyError error) {
                                                           // TODO: Handle error
                                                           Toast.makeText(context, "error" + url, Toast.LENGTH_SHORT).show();

                                                       }
                                                   });
                                           Volley.newRequestQueue(context).add(jsonObjectRequest_licence);


                                           dialog_licence.dismiss();
                                       }
                                   });
                                   if (user_balance >= licence_cost) {
                                       button_licence_add_points.setVisibility(View.GONE);
                                       rl_needpoints.setVisibility(View.GONE);
                                       click_accept.setVisibility(View.VISIBLE);

                                   } else {
                                       double cost_calc = licence_cost - user_balance;
                                       String string_cost_calc = df.format(cost_calc);

                                       need_points.setText(string_cost_calc); // zamiana double na string
                                       click_accept.setVisibility(View.GONE);
                                       button_licence_add_points.setVisibility(View.VISIBLE);
                                       rl_needpoints.setVisibility(View.VISIBLE);
                                   }
                                   dialog_licence.show();
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
           Volley.newRequestQueue(context).add(jsonObjectRequest_check_licence);
       }  */
        });
        nestedScrollView = findViewById(R.id.nested_detail_movie);

        report_issue_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle argsReport = new Bundle();
                BottomSheetDialogReportIssue bottomSheetReportIssue = new BottomSheetDialogReportIssue();
                argsReport.putString("userId", String.valueOf(user.getId()));
                argsReport.putString("promoId", String.valueOf(promo.getId()));
                argsReport.putString("airdropId", "0");
                argsReport.putString("isAirdrop", "0");
                argsReport.putString("username", username2);
                bottomSheetReportIssue.setArguments(argsReport);
                bottomSheetReportIssue.show(getSupportFragmentManager(), bottomSheetReportIssue.getTag());
            }
        });

        FloatingActionButton add_to_favorite = findViewById(R.id.fab_add_favorite);
        add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                final String url = "https://yoururl.com/api/insert_favorite.php?is_airdrop=0&promo_id=" + promo.getId() + "&user_id=" + user.getId() + "&promo_name=" + promo.getOriginalTitle() + "&promo_earn=" + promo.getPrice();

                JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status_check_insert_favorite = response.getString("status_favorite");
                            if (status_check_insert_favorite.equals("1")) {
                                updateSavedPromo();
                                //     Snackbar snackbar = Snackbar.make(nestedScrollView,  Html.fromHtml("<font color=\"#ffffff\">Dodano do ulubionych!</font>"), Snackbar.LENGTH_LONG);
                                //      snackbar.show();
                                Toasty.success(context, "Dodano do ulubionych!").show();

                            } else if (status_check_insert_favorite.equals("2")) {

                                updateDeletedPromo();
                                //  Snackbar snackbar = Snackbar.make(nestedScrollView, Html.fromHtml("<font color=\"#ffffff\">Usunięto z ulubionych</font>"), Snackbar.LENGTH_LONG);
                                //  snackbar.show();
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
                        // Toast.makeText(context, "error"+url, Toast.LENGTH_SHORT).show();
                        Toasty.error(context, "Wystąpił błąd, spróbuj ponownie.", Toast.LENGTH_SHORT, true).show();
                        //dodaj log z wystapienia bledu z uzytkownika do bazy danych
                    }
                });
                Volley.newRequestQueue(context).add(jsonObjectRequest3);

            }
        });

        int id = promo.getId();
        String promo_type = promo.getPromoType();

        Sender s = new Sender(PromoDetailActivity.this, urlAddress, String.valueOf(id), promo_type);
        s.execute();

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    /* Picasso.with(this)
             .load(getString(R.string.tmdb_backdrop_url, promo.getBackdropPath()))
             .into(mImgCover);*/
    /*
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        // holder.rl_bg.setBackgroundColor(color);
        final String str = "img_" + rnd.nextInt(12);
        mImgCover.setImageDrawable
                (
                        context.getResources().getDrawable(getResourceID(str, "drawable", context))
                );
 */
        Picasso.with(this)
                .load(getString(R.string.tmdb_image_url, promo.getPosterPath()))
                .error(context.getDrawable(R.mipmap.empty_view))
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(mImgPoster);

        mTextTitle.setText(promo.getTitle());
        if (promo.getPriceType().contains(".")) {
            text_Price_zl.setText(promo.getPriceType());
            text_PriceZero.setVisibility(View.GONE);
        } else {
            text_Price_zl.setText(promo.getPriceType());
            text_PriceZero.setVisibility(View.VISIBLE);
        }
        mTextReleaseDate.setText(promo.getReleaseDate());
        mTextTotalViews.setText(String.valueOf(promo.getPopularity()));
        mTextLikes.setText(String.valueOf(promo.getLikes()));
        mTextTotalDone.setText(String.valueOf(promo.getTotalDone()));
        if (promo.getVoteAverage() == 0.0) {
            mRatingScore.setVisibility(View.GONE);
            mTextRatingScore.setVisibility(View.GONE);
            text_rating_score_five.setVisibility(View.GONE);
        } else {
            mRatingScore.setVisibility(View.VISIBLE);
            text_rating_score_five.setVisibility(View.VISIBLE);
            mRatingScore.setRating(promo.getVoteAverage());
            mTextRatingScore.setText(String.valueOf(promo.getVoteAverage()));
        }
        mTextSynopsis.setText(Html.fromHtml(promo.getOverview()));
        mTextShortDesc.setText(promo.getShortDesc());
        mTextPrice.setText(promo.getPrice());
        mTextHowLong.setText(promo.getHow_long());
        mTextStep1.setText(Html.fromHtml(promo.getStep1()));
        ///ładowanie reklamy
    /*  mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
*/
        //////////////////////////// sprawdzanie czy promocja ma nową instrukcje:

        if (promo.getInstructionType().equals("1")) {
            text_tutorial_title.setVisibility(View.GONE);
            layout_text_instructions.setVisibility(View.GONE);
            layout_aion.setVisibility(View.VISIBLE);
            button_want_reffer_aion.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    /////////////////////////////////////////////// lepsze instrukcje \/
                    args = new Bundle();

                    BottomSheetDialogInstructions bottomSheet = new BottomSheetDialogInstructions();
                    args.putString("promoName", promo.getOriginalTitle());
                    args.putString("clickedRef", "0");
                    bottomSheet.setCancelable(false);
                    bottomSheet.setArguments(args);
                    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                }
            });
        } else {
            text_tutorial_title.setVisibility(View.VISIBLE);
            global_const_bg.setBackgroundResource(R.drawable.shape_round);
            if (promo.getStep1_1().equals("0")) {
                mTextStep1_1.setVisibility(View.GONE);

            } else {
                mTextStep1_1.setText(Html.fromHtml(promo.getStep1_1()));
                if ((promo.getStep1_1().contains("UWAGA")) || (promo.getStep1_1().contains("WAŻNE"))) {
                    mTextStep1_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep1_1.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep1_1.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep1_2().equals("0")) {
                mTextStep1_2.setVisibility(View.GONE);
            } else {
                mTextStep1_2.setText(Html.fromHtml(promo.getStep1_2()));
                if ((promo.getStep1_2().contains("UWAGA")) || (promo.getStep1_2().contains("WAŻNE"))) {
                    mTextStep1_2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep1_2.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep1_2.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep1_3().equals("0")) {
                mTextStep1_3.setVisibility(View.GONE);

            } else {
                mTextStep1_3.setText(Html.fromHtml(promo.getStep1_3()));
                if ((promo.getStep1_3().contains("UWAGA")) || (promo.getStep1_3().contains("WAŻNE"))) {
                    mTextStep1_3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep1_3.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep1_3.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep1_4().equals("0")) {
                mTextStep1_4.setVisibility(View.GONE);
            } else {
                mTextStep1_4.setText(Html.fromHtml(promo.getStep1_4()));
                if ((promo.getStep1_4().contains("UWAGA")) || (promo.getStep1_4().contains("WAŻNE"))) {
                    mTextStep1_4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep1_4.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep1_4.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep1_5().equals("0")) {
                mTextStep1_6.setVisibility(View.GONE);
            } else {
                mTextStep1_5.setText(Html.fromHtml(promo.getStep1_5()));
                if ((promo.getStep1_5().contains("UWAGA")) || (promo.getStep1_5().contains("WAŻNE"))) {
                    mTextStep1_5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep1_5.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep1_5.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep1_6().equals("0")) {
                mTextStep1_6.setVisibility(View.GONE);
            } else {
                mTextStep1_6.setText(Html.fromHtml(promo.getStep1_6()));
                if ((promo.getStep1_6().contains("UWAGA")) || (promo.getStep1_6().contains("WAŻNE"))) {
                    mTextStep1_6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep1_6.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep1_6.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep1_7().equals("0")) {
                mTextStep1_7.setVisibility(View.GONE);
            } else {
                mTextStep1_7.setText(Html.fromHtml(promo.getStep1_7()));
                if ((promo.getStep1_7().contains("UWAGA")) || (promo.getStep1_7().contains("WAŻNE"))) {
                    mTextStep1_7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep1_7.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep1_7.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }

            mTextStep2.setText(Html.fromHtml(promo.getStep2()));

            if (promo.getStep2_1().equals("0")) {
                mTextStep2_1.setVisibility(View.GONE);
            } else {
                mTextStep2_1.setText(Html.fromHtml(promo.getStep2_1()));
                if ((promo.getStep2_1().contains("UWAGA")) || (promo.getStep2_1().contains("WAŻNE"))) {
                    mTextStep2_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep2_1.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep2_1.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep2_2().equals("0")) {
                mTextStep2_2.setVisibility(View.GONE);
            } else {
                mTextStep2_2.setText(Html.fromHtml(promo.getStep2_2()));
                if ((promo.getStep2_2().contains("UWAGA")) || (promo.getStep2_2().contains("WAŻNE"))) {
                    mTextStep2_2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep2_2.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep2_2.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep2_3().equals("0")) {
                mTextStep2_3.setVisibility(View.GONE);
            } else {
                mTextStep2_3.setText(Html.fromHtml(promo.getStep2_3()));
                if ((promo.getStep2_3().contains("UWAGA")) || (promo.getStep2_3().contains("WAŻNE"))) {
                    mTextStep2_3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep2_3.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep2_3.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep2_4().equals("0")) {
                mTextStep2_4.setVisibility(View.GONE);
            } else {
                mTextStep2_4.setText(Html.fromHtml(promo.getStep2_4()));
                if ((promo.getStep2_4().contains("UWAGA")) || (promo.getStep2_4().contains("WAŻNE"))) {
                    mTextStep2_4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep2_4.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep2_4.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep2_5().equals("0")) {
                mTextStep2_5.setVisibility(View.GONE);
            } else {
                mTextStep2_5.setText(Html.fromHtml(promo.getStep2_5()));
                if ((promo.getStep2_5().contains("UWAGA")) || (promo.getStep2_5().contains("WAŻNE"))) {
                    mTextStep2_5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep2_5.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep2_5.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep2_6().equals("0")) {
                mTextStep2_6.setVisibility(View.GONE);
            } else {
                mTextStep2_6.setText(Html.fromHtml(promo.getStep2_6()));
                if ((promo.getStep2_6().contains("UWAGA")) || (promo.getStep2_6().contains("WAŻNE"))) {
                    mTextStep2_6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep2_6.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep2_6.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep2_7().equals("0")) {
                mTextStep2_7.setVisibility(View.GONE);
            } else {
                mTextStep2_7.setText(Html.fromHtml(promo.getStep2_7()));
                if ((promo.getStep2_7().contains("UWAGA")) || (promo.getStep2_7().contains("WAŻNE"))) {
                    mTextStep2_7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep2_7.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep2_7.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            mTextStep3.setText(Html.fromHtml(promo.getStep3()));

            if (promo.getStep3_1().equals("0")) {
                mTextStep3_1.setVisibility(View.GONE);
            } else {
                mTextStep3_1.setText(Html.fromHtml(promo.getStep3_1()));
                if ((promo.getStep3_1().contains("UWAGA")) || (promo.getStep3_1().contains("WAŻNE"))) {
                    mTextStep3_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep3_1.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep3_1.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep3_2().equals("0")) {
                mTextStep3_2.setVisibility(View.GONE);
            } else {
                mTextStep3_2.setText(Html.fromHtml(promo.getStep3_2()));
                if ((promo.getStep3_2().contains("UWAGA")) || (promo.getStep3_2().contains("WAŻNE"))) {
                    mTextStep3_2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep3_2.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep3_2.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep3_3().equals("0")) {
                mTextStep3_2.setVisibility(View.GONE);
            } else {
                mTextStep3_3.setText(Html.fromHtml(promo.getStep3_3()));
                if ((promo.getStep3_3().contains("UWAGA")) || (promo.getStep3_3().contains("WAŻNE"))) {
                    mTextStep3_3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep3_3.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep3_3.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep3_4().equals("0")) {
                mTextStep3_3.setVisibility(View.GONE);
            } else {
                mTextStep3_4.setText(Html.fromHtml(promo.getStep3_4()));
                if ((promo.getStep3_4().contains("UWAGA")) || (promo.getStep3_4().contains("WAŻNE"))) {
                    mTextStep3_4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep3_4.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep3_4.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep3_5().equals("0")) {
                mTextStep3_4.setVisibility(View.GONE);
            } else {
                mTextStep3_5.setText(Html.fromHtml(promo.getStep3_5()));
                if ((promo.getStep3_5().contains("UWAGA")) || (promo.getStep3_5().contains("WAŻNE"))) {
                    mTextStep3_5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep3_5.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep3_5.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep3_6().equals("0")) {
                mTextStep3_6.setVisibility(View.GONE);
            } else {
                mTextStep3_6.setText(Html.fromHtml(promo.getStep3_6()));
                if ((promo.getStep3_6().contains("UWAGA")) || (promo.getStep3_6().contains("WAŻNE"))) {
                    mTextStep3_6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep3_6.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep3_6.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep3_7().equals("0")) {
                mTextStep3_7.setVisibility(View.GONE);
            } else {
                mTextStep3_7.setText(Html.fromHtml(promo.getStep3_7()));
                if ((promo.getStep3_7().contains("UWAGA")) || (promo.getStep3_7().contains("WAŻNE"))) {
                    mTextStep3_7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep3_7.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep3_7.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }

            mTextStep4.setText(Html.fromHtml(promo.getStep4()));

            if (promo.getStep4_1().equals("0")) {
                mTextStep4_1.setVisibility(View.GONE);
            } else {
                mTextStep4_1.setText(Html.fromHtml(promo.getStep4_1()));
                if ((promo.getStep4_1().contains("UWAGA")) || (promo.getStep4_1().contains("WAŻNE"))) {
                    mTextStep4_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep4_1.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep4_1.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep4_2().equals("0")) {
                mTextStep4_2.setVisibility(View.GONE);
            } else {
                mTextStep4_2.setText(Html.fromHtml(promo.getStep4_2()));
                if ((promo.getStep4_2().contains("UWAGA")) || (promo.getStep4_2().contains("WAŻNE"))) {
                    mTextStep4_2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep4_2.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep4_2.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep4_3().equals("0")) {
                mTextStep4_3.setVisibility(View.GONE);
            } else {
                mTextStep4_3.setText(Html.fromHtml(promo.getStep4_3()));
                if ((promo.getStep4_3().contains("UWAGA")) || (promo.getStep4_3().contains("WAŻNE"))) {
                    mTextStep4_3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep4_3.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep4_3.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep4_4().equals("0")) {
                mTextStep4_4.setVisibility(View.GONE);
            } else {
                mTextStep4_4.setText(Html.fromHtml(promo.getStep4_4()));
                if ((promo.getStep4_4().contains("UWAGA")) || (promo.getStep4_4().contains("WAŻNE"))) {
                    mTextStep4_4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep4_4.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep4_4.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep4_5().equals("0")) {
                mTextStep4_5.setVisibility(View.GONE);
            } else {
                mTextStep4_5.setText(Html.fromHtml(promo.getStep4_5()));
                if ((promo.getStep4_5().contains("UWAGA")) || (promo.getStep4_5().contains("WAŻNE"))) {
                    mTextStep4_5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep4_5.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep4_5.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep4_6().equals("0")) {
                mTextStep4_6.setVisibility(View.GONE);
            } else {
                mTextStep4_6.setText(Html.fromHtml(promo.getStep4_6()));
                if ((promo.getStep4_6().contains("UWAGA")) || (promo.getStep4_6().contains("WAŻNE"))) {
                    mTextStep4_6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep4_6.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep4_6.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep4_7().equals("0")) {
                mTextStep4_7.setVisibility(View.GONE);
            } else {
                mTextStep4_7.setText(Html.fromHtml(promo.getStep4_7()));
                if ((promo.getStep4_7().contains("UWAGA")) || (promo.getStep4_7().contains("WAŻNE"))) {
                    mTextStep4_7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep4_7.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep4_7.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep5().equals("0")) {
                mTextStep5.setVisibility(View.GONE);
                button_step5.setVisibility(View.GONE);

            } else {
                mTextStep5.setText(Html.fromHtml(promo.getStep5()));
            }

            if (promo.getStep5_1().equals("0")) {
                mTextStep5_1.setVisibility(View.GONE);
            } else {
                mTextStep5_1.setText(Html.fromHtml(promo.getStep5_1()));
                if ((promo.getStep5_1().contains("UWAGA")) || (promo.getStep5_1().contains("WAŻNE"))) {
                    mTextStep5_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep5_1.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep5_1.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }

            if (promo.getStep5_2().equals("0")) {
                mTextStep5_2.setVisibility(View.GONE);
            } else {
                mTextStep5_2.setText(Html.fromHtml(promo.getStep5_2()));
                if ((promo.getStep5_2().contains("UWAGA")) || (promo.getStep5_2().contains("WAŻNE"))) {
                    mTextStep5_2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep5_2.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep5_2.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep5_3().equals("0")) {
                mTextStep5_3.setVisibility(View.GONE);
            } else {
                mTextStep5_3.setText(Html.fromHtml(promo.getStep5_3()));
                if ((promo.getStep5_3().contains("UWAGA")) || (promo.getStep5_3().contains("WAŻNE"))) {
                    mTextStep5_3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep5_3.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep5_3.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep5_4().equals("0")) {
                mTextStep5_4.setVisibility(View.GONE);
            } else {
                mTextStep5_4.setText(Html.fromHtml(promo.getStep5_4()));
                if ((promo.getStep5_4().contains("UWAGA")) || (promo.getStep5_4().contains("WAŻNE"))) {
                    mTextStep5_4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep5_4.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep5_4.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep5_5().equals("0")) {
                mTextStep5_5.setVisibility(View.GONE);

            } else {
                mTextStep5_5.setText(Html.fromHtml(promo.getStep5_5()));
                if ((promo.getStep5_5().contains("UWAGA")) || (promo.getStep5_5().contains("WAŻNE"))) {
                    mTextStep5_5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep5_5.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep5_5.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep5_6().equals("0")) {
                mTextStep5_6.setVisibility(View.GONE);
            } else {
                mTextStep5_6.setText(Html.fromHtml(promo.getStep5_6()));
                if ((promo.getStep5_6().contains("UWAGA")) || (promo.getStep5_6().contains("WAŻNE"))) {
                    mTextStep5_6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep5_6.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep5_6.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep5_7().equals("0")) {
                mTextStep5_7.setVisibility(View.GONE);
            } else {
                mTextStep5_7.setText(Html.fromHtml(promo.getStep5_7()));
                if ((promo.getStep5_7().contains("UWAGA")) || (promo.getStep5_7().contains("WAŻNE"))) {
                    mTextStep5_7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep5_7.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep5_7.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }

            if (promo.getStep6().equals("0")) {
                mTextStep6.setVisibility(View.GONE);
                button_step6.setVisibility(View.GONE);

            } else {
                mTextStep6.setText(Html.fromHtml(promo.getStep6()));
            }
            if (promo.getStep6_1().equals("0")) {
                mTextStep6_1.setVisibility(View.GONE);
            } else {
                mTextStep6_1.setText(Html.fromHtml(promo.getStep6_1()));
                if ((promo.getStep6_1().contains("UWAGA")) || (promo.getStep6_1().contains("WAŻNE"))) {
                    mTextStep6_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep6_1.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep6_1.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep6_2().equals("0")) {
                mTextStep6_2.setVisibility(View.GONE);
            } else {
                mTextStep6_2.setText(Html.fromHtml(promo.getStep6_2()));
                if ((promo.getStep6_1().contains("UWAGA")) || (promo.getStep6_2().contains("WAŻNE"))) {
                    mTextStep6_2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep6_2.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep6_2.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep6_3().equals("0")) {
                mTextStep6_3.setVisibility(View.GONE);
            } else {
                mTextStep6_3.setText(Html.fromHtml(promo.getStep6_3()));
                if ((promo.getStep6_3().contains("UWAGA")) || (promo.getStep6_3().contains("WAŻNE"))) {
                    mTextStep6_3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep6_3.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep6_3.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep6_4().equals("0")) {
                mTextStep6_4.setVisibility(View.GONE);
            } else {
                mTextStep6_4.setText(Html.fromHtml(promo.getStep6_4()));
                if ((promo.getStep6_4().contains("UWAGA")) || (promo.getStep6_4().contains("WAŻNE"))) {
                    mTextStep6_4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep6_4.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep6_4.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep6_5().equals("0")) {
                mTextStep6_5.setVisibility(View.GONE);
            } else {
                mTextStep6_5.setText(Html.fromHtml(promo.getStep6_5()));
                if ((promo.getStep6_5().contains("UWAGA")) || (promo.getStep6_5().contains("WAŻNE"))) {
                    mTextStep6_5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep6_5.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep6_5.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep6_6().equals("0")) {
                mTextStep6_6.setVisibility(View.GONE);

            } else {
                mTextStep6_6.setText(Html.fromHtml(promo.getStep6_6()));
                if ((promo.getStep6_6().contains("UWAGA")) || (promo.getStep6_6().contains("WAŻNE"))) {
                    mTextStep6_6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep6_6.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep6_6.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep6_7().equals("0")) {
                mTextStep6_7.setVisibility(View.GONE);
            } else {
                mTextStep6_7.setText(Html.fromHtml(promo.getStep6_7()));
                if ((promo.getStep6_7().contains("UWAGA")) || (promo.getStep6_7().contains("WAŻNE"))) {
                    mTextStep6_7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep6_7.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep6_7.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }

            if (promo.getStep7().equals("0")) {
                button_step7.setVisibility(View.GONE);
                mTextStep7.setVisibility(View.GONE);
            } else {
                mTextStep7.setText(Html.fromHtml(promo.getStep7()));
            }
            if (promo.getStep7_1().equals("0")) {
                mTextStep7_1.setVisibility(View.GONE);
            } else {
                mTextStep7_1.setText(Html.fromHtml(promo.getStep7_1()));
                if ((promo.getStep7_1().contains("UWAGA")) || (promo.getStep7_1().contains("WAŻNE"))) {
                    mTextStep7_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep7_1.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep7_1.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep7_2().equals("0")) {
                mTextStep7_2.setVisibility(View.GONE);
            } else {
                mTextStep7_2.setText(Html.fromHtml(promo.getStep7_2()));
                if ((promo.getStep7_2().contains("UWAGA")) || (promo.getStep7_2().contains("WAŻNE"))) {
                    mTextStep7_2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep7_2.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep7_2.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep7_3().equals("0")) {
                mTextStep7_3.setVisibility(View.GONE);
            } else {
                mTextStep7_3.setText(Html.fromHtml(promo.getStep7_3()));
                if ((promo.getStep7_3().contains("UWAGA")) || (promo.getStep7_3().contains("WAŻNE"))) {
                    mTextStep7_3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep7_3.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep7_3.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep7_4().equals("0")) {
                mTextStep7_4.setVisibility(View.GONE);
            } else {
                mTextStep7_4.setText(Html.fromHtml(promo.getStep7_4()));
                if ((promo.getStep7_4().contains("UWAGA")) || (promo.getStep7_4().contains("WAŻNE"))) {
                    mTextStep7_4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep7_4.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep7_4.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep7_5().equals("0")) {
                mTextStep7_5.setVisibility(View.GONE);
            } else {
                mTextStep7_5.setText(Html.fromHtml(promo.getStep7_5()));
                if ((promo.getStep7_5().contains("UWAGA")) || (promo.getStep7_5().contains("WAŻNE"))) {
                    mTextStep7_5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep7_5.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep7_5.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            if (promo.getStep7_6().equals("0")) {
                mTextStep7_6.setVisibility(View.GONE);
            } else {
                mTextStep7_6.setText(Html.fromHtml(promo.getStep7_6()));
                if ((promo.getStep7_6().contains("UWAGA")) || (promo.getStep7_6().contains("WAŻNE"))) {
                    mTextStep7_6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep7_6.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep7_6.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);
                }
            }
            if (promo.getStep7_7().equals("0")) {
                mTextStep7_7.setVisibility(View.GONE);
            } else {
                mTextStep7_7.setText(Html.fromHtml(promo.getStep7_7()));
                if ((promo.getStep7_7().contains("UWAGA")) || (promo.getStep7_7().contains("WAŻNE"))) {
                    mTextStep7_7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    mTextStep7_7.setBackground(getResources().getDrawable(R.drawable.dotted_border_important));
                } else {
                    Drawable adjust = ProfitzApp.getContext().getResources().getDrawable(R.drawable.adjust);
                    mTextStep7_7.setCompoundDrawablesWithIntrinsicBounds(adjust, null, null, null);

                }
            }
            mTextStepX.setText(Html.fromHtml(promo.getStepX()));
        }
        getData3(promoId);
        // asyncComment.execute();
    }

    public static String getName2() {
        return promoName2;
    }

    public static int getId2() {
        return promoId2;
    }

    public static float getRating2() {
        return promoRating2;
    }

    @Override
    public void onButtonClicked(String text) {

    }
    public void getData2(int promo_id) {
        final String url25 = "https://yoururl.com/api/check_review_count.php?promo_id=" + promo_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url25, null, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    count_reviews = response.getString("count_reviews");
                    if (count_reviews.equals("0")) {
                        // hide_if_zero.setVisibility(View.VISIBLE);
                        add_review_top.setVisibility(View.VISIBLE);
                        layout_reviews.setVisibility(View.GONE);

                        see_all_reviews.setVisibility(View.GONE);
                        rl_reviews.setVisibility(View.GONE);
                        // Toast.makeText(mContext, "zero", Toast.LENGTH_SHORT).show();

                    } else{
                        rl_reviews.setVisibility(View.VISIBLE);
                        layout_reviews.setVisibility(View.VISIBLE);

                        add_review_top.setVisibility(View.GONE);
                        see_all_reviews.setVisibility(View.VISIBLE);
                        String text_latest_reviews = "Zobacz wszystkie (" + count_reviews +")";
                        textInfo.setText("Najnowsze opinie");
                        see_all_reviews.setText(text_latest_reviews);
                        // new AsyncReviews().execute();

                        new AsyncReviews().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

    public void getData3(int promo_id) {
        final String url25 = "https://yoururl.com/api/check_comments_count.php?promo_id=" + promo_id + "&is_airdrop=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url25, null, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    count_comments = response.getString("count_comments");
                    if (count_comments.equals("0")) {
                        // hide_if_zero.setVisibility(View.VISIBLE);
                        no_comments_yet.setVisibility(View.VISIBLE);

                        textInfoComment.setText("Brak komentarzy dla tej promocji.");
                        // Toast.makeText(mContext, "zero", Toast.LENGTH_SHORT).show();

                    } else {
                        String comments = "Komentarze (" + count_comments + ")";
                        textInfoComment.setText(comments);
                        // new AsyncReviews().execute();

                        AsyncComment asyncComment = new AsyncComment(promo_id);
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
    private class AsyncComment extends AsyncTask < String, String, String > {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn3;
        URL url3 = null;
        int promoIdd3;

        LayoutInflater factory = LayoutInflater.from(context);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(context);
        public AsyncComment(int promoIdd2) {
            promoIdd3 = promoIdd2;
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
        protected String doInBackground(String...params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url3 = new URL("https://yoururl.com/api/get_comments.php?promo_id=" + promoIdd3 + "&is_airdrop=0");

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
            ArrayList < Comment > listComment = new ArrayList < > ();

            FragmentManager fragmentManager = getSupportFragmentManager();
            RvComment = findViewById(R.id.rv_comment);
            RvComment.setVisibility(View.VISIBLE);

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
                    Comment commentData = new Comment(null, null, null, null, null, null, null, null, "0", null, null, null);

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
                adapterComment = new AdapterComment(getApplicationContext(), listComment, fragmentManager);

                RvComment.setAdapter(adapterComment);
                RvComment.setLayoutManager(new LinearLayoutManager(context));

            } catch (JSONException e) {
                e.printStackTrace();
                //mSwipeRefreshLayout.setRefreshing(false);
                // Toast.makeText(DoneActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }

    class AsyncReviews extends AsyncTask < String, String, String > {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

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
        protected String doInBackground(String...params) {

            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                User user = MyPreferenceManager.getInstance(context).getUser();
                url = new URL("https://yoururl.com/api/get_reviews.php?promo_id=" + promoId + "&limit=yes");
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

            // pdLoading.dismiss();
            data = new ArrayList < > ();
            // pdLoading.dismiss();
            try {
                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataReview reviewData = new DataReview();
                    reviewData.reviewId = json_data.getInt("review_id");
                    reviewData.reviewName = json_data.getString("review_name");
                    reviewData.reviewDescription = json_data.getString("review_description");
                    reviewData.reviewImage = json_data.getString("review_img");
                    reviewData.reviewStars = json_data.getDouble("review_stars");
                    reviewData.reviewDateAdd = json_data.getString("review_date_add");
                    data.add(reviewData);
                }

                // Setup and Handover data to recyclerview
                //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterReview(context, data);
                mRVReviewList.setAdapter(mAdapter);
                mRVReviewList.setLayoutManager(linearLayoutManager);

                if (data.size() > 1) {

                    runSlider();

                }
            } catch (JSONException e) {
                // Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(context, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }

    public void runSlider() {
        final String url25 = "https://yoururl.com/api/check_review_count.php?promo_id=" + promoId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url25, null, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    count_reviews = response.getString("count_reviews");
                    if (!count_reviews.equals("0")) {
                        final int time = 4000; // it's the delay time for sliding between items in recyclerview
                        final LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
                        if (mRVReviewList.getOnFlingListener() == null) {
                            linearSnapHelper.attachToRecyclerView(mRVReviewList);
                        }

                        timer = new Timer();
                        timer.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                try {
                                    isRunning = true;

                                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (mAdapter.getItemCount() - 1)) {

                                        linearLayoutManager.smoothScrollToPosition(mRVReviewList, new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                                    } else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (mAdapter.getItemCount() - 1)) {

                                        linearLayoutManager.smoothScrollToPosition(mRVReviewList, new RecyclerView.State(), 0);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    isRunning = false;
                                }

                            }
                        }, 0, time);

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
    public void stopSlider() {
        if (isRunning) {
            // handler2.removeCallbacks(runnable);
            timer.cancel();
            timer.purge();
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
        initPresenter();
        mPromoDetailPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPromoDetailPresenter.unsubscribe();
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
            mPromoDetailPresenter.sharePromo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(@NonNull PromoDetailContract.Presenter presenter) {
        mPromoDetailPresenter = presenter;
    }

    public static void showDialogSuccess() {
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
    public void showDialogSuccessLicence() {
        final Dialog dialog3 = new Dialog(context); // Context, this, etc.
        dialog3.setContentView(R.layout.dialog_licence_exists);
        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_licence_info2 = (TextView) dialog3.findViewById(R.id.dialog_licence_info2);
        dialog_licence_info2.setText("Gratulacje! Od teraz posiadasz licencję na polecanie tej promocji za pomocą aplikacji Zarabiaj Online. Twój link polecający z " + promoName + " został dodany. Od teraz każda osoba, która poda Twój kod polecający podczas rejestracji zobaczy Twój link/kod polecający z tej promocji. Używaj instrukcji i promuj ją jak swoją! Powodzenia! :) ");

        Button click3 = (Button) dialog3.findViewById(R.id.button_licence_confirm_success);
        click3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog3.dismiss();
            }
        });
        dialog3.show();

    }

    @Override
    public void showLoading() {
        //mProgressLoading.setVisibility(View.VISIBLE);
        //   gif_loader.setVisibility(View.VISIBLE);
        main_dialog.setContentView(DialogView);
        main_dialog.setCancelable(false);
        main_dialog.setCanceledOnTouchOutside(false);

        main_dialog.show();
        //main_dialog.getWindow().setLayout(300, 300);
        main_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //this method will be running on UI thread
    }
    public void openDialogReview(String promoName, int promo_id, String promo_earn_points) {
        stopSlider();
        final Dialog dialog_review = new Dialog(context); // Context, this, etc.
        dialog_review.setContentView(R.layout.dialog_add_review);
        dialog_review.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_review_name = (TextView) dialog_review.findViewById(R.id.dialog_review_name);
        dialog_review_name.setText(promoName);
        User user = MyPreferenceManager.getInstance(this).getUser();
        Context context_dialog = dialog_review.getContext();

        CircleImageView iv_dialog_promo = (CircleImageView) dialog_review.findViewById(R.id.iv_dialog_promo);
        Picasso.with(context_dialog).load("https://yoururl.com/api/images/promos/" + promoName + ".png").into(iv_dialog_promo);

        TextView ratingbar_text = dialog_review.findViewById(R.id.rating_score_text);
        TextView dialog_promo_info_points = dialog_review.findViewById(R.id.dialog_promo_info_points);
        dialog_promo_info_points.setVisibility(View.VISIBLE);
        final String url_check_promo_star_status1 = "https://yoururl.com/api/check_promo_star_status.php?promo_id=" + promo_id;
        JsonObjectRequest jsonObjectRequestcheck = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_check_promo_star_status1, null, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    status_check_stars = response.getString("status");
                    if (status_check_stars.equals("0")) {
                        String dialog_text = "Dodaj kilka zdań od siebie o tej promocji.";
                        dialog_promo_info_points.setText(dialog_text);

                    } else {
                        String dialog_text = "Dodaj opinię by otrzymać " + promo_earn_points + " bonusowych punktów.";
                        dialog_promo_info_points.setText(dialog_text);
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
        Volley.newRequestQueue(context).add(jsonObjectRequestcheck);

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

        ratingBar2 = (RatingBar) dialog_review.findViewById(R.id.rating_score);
        ratingBar2.setRating(Float.parseFloat("5.0"));
        ratingbar_text.setText("5.0");
        ratingg = (float) 5.0;
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingg = ratingBar.getRating();
                String rating_string = String.valueOf(ratingg);
                ratingbar_text.setText(rating_string);
            }
        });
        TextView add_review1 = dialog_review.findViewById(R.id.button_review_confirm);
        add_review1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialog_review_text.length() < 10 ||
                        dialog_review_text.length() > 175) {

                    dialog_review_text.setError("Ilość znaków powinna wynosić od 10 do 175.");

                    dialog_review_text.requestFocus();
                } else {
                    runSlider();
                    if (status_check_stars.equals("0")) {

                        String urlAddress = "https://yoururl.com/api/insert_review.php?add_points=false";
                        ReviewSender s = new ReviewSender(PromoDetailActivity.this, urlAddress, String.valueOf(user.getId()), String.valueOf(promo_id), promoName, promo_earn_points, String.valueOf(ratingg), dialog_review_text.getText().toString());
                        s.execute();

                        dialog_review.dismiss();
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog_received_points_from_review = new Dialog(context); // Context, this, etc.
                                dialog_received_points_from_review.setContentView(R.layout.dialog_received_points_from_review);
                                TextView text_receive_points = dialog_received_points_from_review.findViewById(R.id.dialog_received_points_info2);
                                Button button_received_points = dialog_received_points_from_review.findViewById(R.id.button_received_points);
                                TextView text_receive_points2 = dialog_received_points_from_review.findViewById(R.id.dialog_received_points_info3);

                                ImageView iv_congrats = dialog_received_points_from_review.findViewById(R.id.iv_congrats);
                                ImageView iv_congrats2 = dialog_received_points_from_review.findViewById(R.id.iv_congrats2);

                                iv_congrats.setVisibility(View.GONE);
                                iv_congrats2.setVisibility(View.VISIBLE);

                                TextView dialog_received_points_info = dialog_received_points_from_review.findViewById(R.id.dialog_received_points_info);
                                dialog_received_points_info.setText("Dziękujemy!");
                                String text_string = "Dziękujemy za dodanie opinii do promocji " + promoName + ".";
                                text_receive_points.setText(text_string);

                                String text_string2 = "Zostanie dodana, po uprzednim sprawdzeniu przez administrację.";
                                text_receive_points2.setText(text_string2);

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
                    } else {

                        String urlAddress = "https://yoururl.com/api/insert_review.php?add_points=true";
                        ReviewSender s = new ReviewSender(PromoDetailActivity.this, urlAddress, String.valueOf(user.getId()), String.valueOf(promo_id), promoName, promo_earn_points, String.valueOf(ratingg), dialog_review_text.getText().toString());
                        s.execute();

                        dialog_review.dismiss();
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog_received_points_from_review = new Dialog(context); // Context, this, etc.
                                dialog_received_points_from_review.setContentView(R.layout.dialog_received_points_from_review);
                                TextView text_receive_points = dialog_received_points_from_review.findViewById(R.id.dialog_received_points_info2);
                                Button button_received_points = dialog_received_points_from_review.findViewById(R.id.button_received_points);
                                String text_string = "Otrzymujesz " + promo_earn_points + " bonusowych punktów za wykonanie promocji " + promoName;
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

            }
        });

        dialog_review.show();
    }

    @Override
    public void hideLoading() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                main_dialog.dismiss();
                handler.postDelayed(this, 3000);
            }
        }, 2500);

    }
    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException(
                    "No resource string found with name " + resName
            );
        } else {
            return ResourceID;
        }
    }
    public void updateLicence() {
        mFabAddLicence.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(), R.drawable.ic_baseline_add_circle_outline_blue_24));
    }

    public static void updateDonePromo() {

        mFabAddDone.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(), R.drawable.ic_done_green_24dp));
    }

    @Override
    public void updateSavedPromo() {
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
                mFabAddFavorite.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(), R.drawable.ic_favorite_red_24dp));
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

    @Override
    public void updateDeletedPromo() {
        mFabAddFavorite.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(), R.drawable.ic_favorite_border_white_24dp));

    }
    @Override
    public void showMessage(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this,
                message,
                Toast.LENGTH_LONG);
        mToast.show();
    }
    public void openChat() {
        startActivity(new Intent(context, ChatSupportActivity.class));
    }

    public void openPromoUrl(String promoUrl) {
        args.putString("clickedRef", "1");
        args.putString("reflinkUrl", promoUrl);

        RefClicked refClicked = com.profitz.app.RefClicked.getInstance();
        GetReflink getReflink = com.profitz.app.GetReflink.getInstance();
        getReflink.setReflink(promoUrl);
        refClicked.setData("1");
        // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(promoUrl));
        // context.startActivity(browserIntent);

    }
    public void addDoneNonStar() {
        final String url = "https://yoururl.com/api/check_done_clicked.php?promo_id=" + promoId + "&user_id=" + user_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    status_check_done = response.getString("status");

                    if (status_check_done.equals("0")) // brak w wykonanych
                    {
                        String urlAddress2 = "https://yoururl.com/api/insert_done.php";
                        // String done_date = promo.getReleaseDate();

                        DoneSender s = new DoneSender(context, urlAddress2, String.valueOf(user_id), user.getName(), user.getEmail(), String.valueOf(promoId), promoName, "null", "promocja", promoPrice, "null", "null", "null");
                        s.execute();

                        mFabAddDone.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(), R.drawable.ic_done_green_24dp));
                        if (username.substring(username.length() - 1).equals("a")) {
                            // Snackbar snackbar = Snackbar.make(nestedScrollView,  Html.fromHtml("<font color=\"#ffffff\"></font>"), Snackbar.LENGTH_LONG);
                            // snackbar.show();
                            Toasty.success(context, "Oznaczyłaś promocję jako wykonaną.").show();

                        } else {
                            //  Snackbar snackbar = Snackbar.make(nestedScrollView,  Html.fromHtml("<font color=\"#ffffff\"></font>"), Snackbar.LENGTH_LONG);
                            //  snackbar.show();
                            Toasty.success(context, "Oznaczyłeś promocję jako wykonaną.").show();

                        }
                    } else {
                        final String url = "https://yoururl.com/api/delete_done.php?user_id=" + user.getId() + "&promo_name=" + promoName + "&promo_id=" + promoId;

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String deleted_status = response.getString("deleted_status");

                                    if (deleted_status.equals("1")) {
                                        mFabAddDone.setImageDrawable(ContextCompat.getDrawable(ProfitzApp.getContext(), R.drawable.ic_done_border_white_24dp));
                                        if (username.substring(username.length() - 1).equals("a")) {
                                            // Snackbar snackbar = Snackbar.make(nestedScrollView,  Html.fromHtml("<font color=\"#ffffff\">Usunęłaś promocję z listy wykonanych.</font>"), Snackbar.LENGTH_LONG);
                                            //  snackbar.show();
                                            Toasty.success(context, "Usunęłaś promocję z listy wykonanych.").show();

                                        } else {
                                            //   Snackbar snackbar = Snackbar.make(nestedScrollView,  Html.fromHtml("<font color=\"#ffffff\">Usunąłeś promocję z listy wykonanych.</font>"), Snackbar.LENGTH_LONG);
                                            //   snackbar.show();
                                            Toasty.success(context, "Usunąłeś promocję z listy wykonanych.").show();

                                        }
                                    } else {
                                        // Snackbar snackbar = Snackbar.make(nestedScrollView,  Html.fromHtml("<font color=\"#ffffff\">Coś poszło nie tak. Spróbuj ponownie.</font>"), Snackbar.LENGTH_LONG);
                                        //    snackbar.show();
                                        Toasty.error(context, "Coś poszło nie tak. Spróbuj ponownie.").show();

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
                        Volley.newRequestQueue(context).add(jsonObjectRequest);

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
        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }

    public void openPromoDoneDialog(View view) {

        final String url = "https://yoururl.com/api/check_done_clicked.php?promo_id=" + promoId + "&user_id=" + user_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    status_check_done = response.getString("status");

                    if (status_check_done.equals("0")) // brak w wykonanych
                    {

                        BottomSheetDialogInsertDone bottomSheet = new BottomSheetDialogInsertDone();
                        Bundle args = new Bundle();

                        args.putInt("user_id", Integer.parseInt(user.getId()));
                        args.putString("user_nickname", user.getUsername());
                        args.putString("user_email", user.getEmail());
                        args.putInt("promo_id", promoId);
                        args.putString("promo_name", promoOrginalTitle);
                        args.putString("promo_type2", promoTyp);
                        args.putString("promo_earn", promoPrice);
                        args.putInt("user_lvl", Integer.parseInt(data_getLevel));

                        bottomSheet.setArguments(args);
                        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                    } else if (status_check_done.equals("1")) // jeszcze nie zatwierdzono
                    {
                        showDialogSuccess();
                    } else if (status_check_done.equals("2")) // do odebrania punkty
                    {
                        final String url = "https://yoururl.com/api/get_done_promo_info.php?user_id=" + user.getId() + "&promo_id=" + promoId;

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener < JSONObject > () {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String promoReceivedPoints = response.getString("promoBonus");
                                    openDialogReview(promoName, promoId, promoReceivedPoints);

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
                    } else if (status_check_done.equals("3")) // odebrano punkty
                    {
                        updateDonePromo();
                        if (username.substring(username.length() - 1).equals("a")) {
                            //   Snackbar snackbar = Snackbar.make(constraint_detail, Html.fromHtml("<font color=\"#ffffff\">Ukończyłaś już tą promocję.</font>"), Snackbar.LENGTH_LONG);
                            //    snackbar.show();
                            Toasty.normal(context, "Ukończyłaś już tą promocję.").show();

                        } else {
                            //     Snackbar snackbar = Snackbar.make(constraint_detail,  Html.fromHtml("<font color=\"#ffffff\">Ukończyłeś już tą promocję.</font>"), Snackbar.LENGTH_LONG);
                            //   snackbar.show();
                            Toasty.normal(context, "Ukończyłeś już tą promocję.").show();

                        }
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

        //  DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);

        // Comment comment = snap.getValue(Comment.class);

    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;

    }

    private void onBackgroundTaskDataObtained(ArrayList < Reply > results) {
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
    public class AdapterComment extends RecyclerView.Adapter < AdapterComment.CommentViewHolder > {

        private final Context mContext;
        private final LayoutInflater inflater;
        private final FragmentManager fragmentManager;
        List < Comment > mData = Collections.emptyList();
        private final List < Comment > dataListFull;
        private AdapterReply replyAdapter;
        ProfitzApp mApp = ((ProfitzApp) getApplicationContext());

        public AdapterComment(Context mContext, List < Comment > mData, FragmentManager fragmentManager) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(mContext);
            this.mData = mData;
            this.fragmentManager = fragmentManager;
            dataListFull = new ArrayList < > (mData);
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
                long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
                long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

                if (second < 60) {
                    if (second <= 10) {
                        convTime = "kilka sekund  " + suffix;

                    } else {
                        convTime = "chwilę " + suffix;

                    }
                } else if (minute < 60) {
                    if (minute == 1) {
                        convTime = " minutę " + suffix;

                    } else if ((minute >= 2) && (minute <= 4)) {
                        convTime = minute + " minuty " + suffix;

                    } else if ((minute >= 22) && (minute <= 24)) {
                        convTime = minute + " minuty " + suffix;

                    } else if ((minute >= 32) && (minute <= 34)) {
                        convTime = minute + " minuty " + suffix;

                    } else if ((minute >= 42) && (minute <= 44)) {
                        convTime = minute + " minuty " + suffix;

                    } else if ((minute >= 52) && (minute <= 54)) {
                        convTime = minute + " minuty " + suffix;

                    } else {
                        convTime = minute + " minut " + suffix;

                    }
                } else if (hour < 24) {
                    if (hour == 1) {
                        convTime = " godzinę " + suffix;

                    } else if ((hour >= 2) && (hour <= 4)) {
                        convTime = hour + " godziny " + suffix;

                    } else if ((hour >= 22) && (hour <= 23)) {
                        convTime = hour + " godziny " + suffix;

                    } else {
                        convTime = hour + " godzin " + suffix;

                    }

                } else if (day >= 30) {

                    convTime = parseDateToddMMyyyy(dataDate);

                } else if (day < 30) {
                    if (day == 0) {
                        convTime = "wczoraj";

                    } else if (day == 1) {
                        convTime = "dzień " + suffix;

                    } else {
                        convTime = day + " dni " + suffix;

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
        public CommentViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.comment_layout, parent, false);

            return new CommentViewHolder(view);

        }
        @Override
        public void onBindViewHolder(CommentViewHolder holder, @SuppressLint("RecyclerView") int fposition) {
            String imgUrl = mData.get(fposition).getUimg();
            Picasso.with(mContext)
                    .load(imgUrl)
                    .placeholder(mContext.getDrawable(R.drawable.default_avatar))
                    .error(mContext.getDrawable(R.drawable.default_avatar))
                    .into(holder.comment_user_img);
            if (mData.get(fposition).hasReply().equals("1")) {
                holder.replies.setVisibility(View.VISIBLE);
                holder.response.setVisibility(View.VISIBLE);
                holder.line2.setVisibility(View.VISIBLE);
                holder.list_item_genre_arrow.setVisibility(View.VISIBLE);
                holder.first_reply_img.setVisibility(View.VISIBLE);
                holder.first_reply_content.setVisibility(View.VISIBLE);
                holder.first_reply_username.setVisibility(View.VISIBLE);
                holder.first_reply_username.setText(mData.get(fposition).getUnameFirstReply());
                holder.first_reply_content.setText(mData.get(fposition).getFirstContentReply());

                String first_reply_user_img_link = "https://yoururl.com/api/images/users/" + mData.get(fposition).getUnameFirstReply() + ".png";

                Picasso.with(mContext)
                        .load(first_reply_user_img_link)
                        .placeholder(mContext.getDrawable(R.drawable.default_avatar))
                        .error(mContext.getDrawable(R.drawable.default_avatar))
                        .into(holder.first_reply_user_img);

                String url_count = "https://yoururl.com/api/get_replies_count.php?comment_id=" + mData.get(fposition).getUid();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_count, null, new Response.Listener < JSONObject > () {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String count_replies = response.getString("count_replies");

                            if (count_replies.equals("0")) {
                                holder.response.setVisibility(View.GONE);

                            } else if (count_replies.equals("1")) {
                                holder.response.setVisibility(View.VISIBLE);
                                holder.response.setText("Zobacz całą odpowiedź");

                            } else {
                                holder.response.setVisibility(View.VISIBLE);
                                holder.response.setText("Zobacz wszystkie odpowiedzi (" + count_replies + ")");

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

                String url = "https://yoururl.com/api/get_replies.php?comment_id=" + mData.get(fposition).getUid();
                ArrayList < Reply > listReply = new ArrayList < > ();

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener < JSONArray > () {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject jsonObject = response.getJSONObject(i);

                                Reply replyData = new Reply(null, null, null, null, null, null, null, null, "0", null, null, null);

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

                        replyAdapter = new AdapterReply(mContext, listReply, fragmentManager, promoId, 0, "0", user_id, username2, mData.get(fposition).getUid());
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

            } else if (mData.get(fposition).hasReply().equals("0")) {

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

            if (mData.get(fposition).getUname().equals(username2)) {
                holder.delete_comment.setVisibility(View.VISIBLE);
                holder.edit_comment.setVisibility(View.VISIBLE);
                holder.report_comment.setVisibility(View.GONE);

                holder.delete_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();

                        BottomSheetDialogDeleteCommentConfirm bottomSheetComment = new BottomSheetDialogDeleteCommentConfirm();
                        args.putString("commentId", mData.get(fposition).getUid());
                        args.putString("promoId", String.valueOf(promoId));
                        args.putString("username", username2);
                        args.putString("airdropId", "0");
                        args.putString("isAirdrop", "0");
                        args.putString("userid", String.valueOf(user_id));
                        args.putString("content", mData.get(fposition).getContent());
                        args.putString("isreply", "no");

                        bottomSheetComment.setArguments(args);
                        bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());

                    }
                });
                holder.edit_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();

                        BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                        args.putString("commentId", mData.get(fposition).getUid());
                        args.putString("userId", String.valueOf(user_id));
                        args.putString("promoId", String.valueOf(promoId));
                        args.putString("username", username2);
                        args.putString("is_reply", "yes");
                        args.putString("global_comm", "no");
                        args.putString("reply_username", mData.get(fposition).getUname());
                        args.putString("reply_username_true", "true");
                        args.putString("replyId", mData.get(fposition).getUid());
                        args.putString("comment_action", "edit");
                        args.putString("comment_content", mData.get(fposition).getContent());

                        bottomSheetComment.setArguments(args);
                        bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());

                    }
                });

            } else {
                holder.delete_comment.setVisibility(View.GONE);
                holder.edit_comment.setVisibility(View.GONE);
                holder.report_comment.setVisibility(View.VISIBLE);
                holder.report_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();

                        BottomSheetDialogReportComment bottomSheetReportComment = new BottomSheetDialogReportComment();
                        args.putString("commentId", mData.get(fposition).getUid());
                        args.putString("userId", String.valueOf(user_id));
                        args.putString("promoId", String.valueOf(promoId));
                        args.putString("promoId", "0");
                        args.putString("airdropId", "0");
                        args.putString("isAirdrop", "0");
                        args.putString("username", username2);

                        bottomSheetReportComment.setArguments(args);
                        bottomSheetReportComment.show(getSupportFragmentManager(), bottomSheetReportComment.getTag());

                    }
                });

            }

            if (mData.get(fposition).getReplyToReplyId().equals("0")) {
                holder.tv_content.setText(mData.get(fposition).getContent());
            } else {
                Spanned content_add_username = Html.fromHtml("<font color=\"#2E51A7\">@" + mData.get(fposition).getReplyToUsername() + "</font><font color=\"#000000\"> " + mData.get(fposition).getContent() + "</font>");
                holder.tv_content.setText(content_add_username);

            }

            String url_check_like_dislike = "https://yoururl.com/api/check_comment_like_dislike.php?comment_id=" + mData.get(fposition).getUid() + "&user_id=" + user_id;

            JsonObjectRequest jsonObjectRequest21 = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_check_like_dislike, null, new Response.Listener < JSONObject > () {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        String response2 = response.getString("response");

                        if (response2.equals("0")) {
                            holder.like_full.setVisibility(View.GONE);
                            holder.like_outline.setVisibility(View.VISIBLE);

                        } else if (response2.equals("1")) {
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

            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();

                    BottomSheetDialogAddComment bottomSheetComment = new BottomSheetDialogAddComment();
                    args.putString("commentId", mData.get(fposition).getUid());
                    args.putString("userId", String.valueOf(user_id));
                    args.putString("promoId", String.valueOf(promoId));
                    args.putString("username", username2);
                    args.putString("isAirdrop", "0");
                    args.putString("airdropId", "0");
                    args.putString("is_reply", "yes");
                    args.putString("global_comm", "no");
                    args.putString("reply_username", mData.get(fposition).getUname());
                    args.putString("reply_username_true", "true");
                    args.putString("replyId", mData.get(fposition).getUid());
                    args.putString("comment_action", "add");

                    bottomSheetComment.setArguments(args);
                    bottomSheetComment.show(getSupportFragmentManager(), bottomSheetComment.getTag());

                }
            });

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url_like_dislike = "https://yoururl.com/api/update_comment_like_dislike.php?is_aidrop=1&promo_id=" + promoId + "&comment_id=" + mData.get(fposition).getUid() + "&user_id=" + user_id;

                    JsonObjectRequest jsonObjectRequest22 = new JsonObjectRequest(com.android.volley.Request.Method.GET, url_like_dislike, null, new Response.Listener < JSONObject > () {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String response2 = response.getString("response");
                                String likes = response.getString("likes");

                                if (response2.equals("0")) {
                                    // String likes = mData.get(fposition).getLikes();

                                    int new_likes = Integer.parseInt(likes.trim()) - 1;
                                    String new_likes_string;

                                    if (new_likes == 1) {
                                        new_likes_string = new_likes + " polubienie";
                                        holder.count.setText(new_likes_string);
                                    } else if (new_likes == 2) {
                                        new_likes_string = new_likes + " polubienia";
                                        holder.count.setText(new_likes_string);
                                    } else if (new_likes == 3) {
                                        new_likes_string = new_likes + " polubienia";
                                        holder.count.setText(new_likes_string);
                                    } else if (new_likes == 4) {
                                        new_likes_string = new_likes + " polubienia";
                                        holder.count.setText(new_likes_string);
                                    } else {
                                        new_likes_string = new_likes + " polubień";
                                        holder.count.setText(new_likes_string);
                                    }

                                    holder.like_full.setVisibility(View.GONE);
                                    holder.like_outline.setVisibility(View.VISIBLE);

                                } else if (response2.equals("1")) {
                                    holder.like_outline.setVisibility(View.GONE);
                                    holder.like_full.setVisibility(View.VISIBLE);
                                    // String likes = mData.get(fposition).getLikes();
                                    int new_likes = Integer.parseInt(likes.trim()) + 1;
                                    String new_likes_string;

                                    if (new_likes == 1) {
                                        new_likes_string = new_likes + " polubienie";
                                        holder.count.setText(new_likes_string);
                                    } else if (new_likes == 2) {
                                        new_likes_string = new_likes + " polubienia";
                                        holder.count.setText(new_likes_string);
                                    } else if (new_likes == 3) {
                                        new_likes_string = new_likes + " polubienia";
                                        holder.count.setText(new_likes_string);
                                    } else if (new_likes == 4) {
                                        new_likes_string = new_likes + " polubienia";
                                        holder.count.setText(new_likes_string);
                                    } else {
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

            if (likes.equals("1")) {
                String new_likes_string = likes + " polubienie";
                holder.count.setText(new_likes_string);
            } else if (likes.equals("2")) {
                String new_likes_string = likes + " polubienia";
                holder.count.setText(new_likes_string);
            } else if (likes.equals("3")) {
                String new_likes_string = likes + " polubienia";
                holder.count.setText(new_likes_string);
            } else if (likes.equals("4")) {
                String new_likes_string = likes + " polubienia";
                holder.count.setText(new_likes_string);
            } else {
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
            CircleImageView img_user, first_reply_img;
            TextView tv_name, tv_content, tv_date, first_reply_username, first_reply_content, response, reply, count;
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
            String date = DateFormat.format("hh:mm", calendar).toString();
            return date;

        }

    }

    @Override
    public void sharePromo(String promo, String video) {
        String mimeType = "text/plain";
        String title = getString(R.string.share_dialog_title);
        String space = " ";

        String message = getString(R.string.sharing_text, promo) +
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

    private void initPresenter() {
        showLoading();
        Intent intent = getIntent();
        if (intent.hasExtra("intent_fav")) {} else {
            //Log.i("extra2", String.valueOf(  getIntent().getExtras().getParcelable(INTENT_EXTRA_PROMO)));
            mPromoDetailPresenter = new PromoDetailPresenter(this,
                    Repository.getInstance(RemoteDataSource.getInstance(),
                            LocalDataSource.getInstance(getApplicationContext())),
                    SchedulerProvider.getInstance(),
                    getIntent().getExtras().getParcelable(INTENT_EXTRA_PROMO));
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}