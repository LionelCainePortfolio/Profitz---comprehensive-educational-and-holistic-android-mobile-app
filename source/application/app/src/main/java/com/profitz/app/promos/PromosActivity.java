package com.profitz.app.promos;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.profitz.app.Config;
import com.profitz.app.R;
import com.profitz.app.data.model.Promo;
import com.profitz.app.data.source.Repository;
import com.profitz.app.data.source.local.LocalDataSource;
import com.profitz.app.data.source.preferences.AppPreferences;
import com.profitz.app.data.source.remote.RemoteDataSource;
import com.profitz.app.promodetail.PromoDetailActivity;
import com.profitz.app.promos.activities.DoneActivity;
import com.profitz.app.promos.activities.FavoriteActivity;
import com.profitz.app.promos.activities.PointsActivity;
import com.profitz.app.promos.activities.ReferFriendHowItWorksActivity;
import com.profitz.app.promos.activities.WelcomeActivity;
import com.profitz.app.promos.adapters.PromosAdapter;
import com.profitz.app.promos.fragments.AdsFragment;
import com.profitz.app.promos.fragments.AirdropsFragment;
import com.profitz.app.promos.fragments.AwardsFragment;
import com.profitz.app.promos.fragments.BottomSheetDialogDisabledFuture;
import com.profitz.app.promos.fragments.BottomSheetDialogFirstIntroduction;
import com.profitz.app.promos.fragments.BottomSheetDialogNeedHelp;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.GamesFragment;
import com.profitz.app.promos.fragments.HelpFragment;
import com.profitz.app.promos.fragments.MarketFragment;
import com.profitz.app.promos.fragments.MyProfileFragment;
import com.profitz.app.promos.fragments.ReferFriendFragment;
import com.profitz.app.promos.interfaces.PhoneNumberListener;
import com.profitz.app.util.CustomSwipeToRefresh;
import com.profitz.app.util.MovableFloatingActionButton;
import com.profitz.app.util.SliderTransformer;
import com.profitz.app.util.UnScrollableViewPager;
import com.profitz.app.util.isQuizAnswerChoosen;
import com.profitz.app.util.schedulers.SchedulerProvider;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.sentry.Sentry;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class PromosActivity extends AppCompatActivity implements PromosContract.View, PromosAdapter.PromoClickListener, BottomSheetDialogNeedHelp.BottomSheetListener, BottomSheetDialogFirstIntroduction.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener, BottomSheetDialogDisabledFuture.BottomSheetListener {


    private static final String MOVIES_LIST = "movies_list";
    private static final String NAVIGATION = "navigation";
    private static final int NAV_FAVORITE_MOVIES = 2;
    private static final int ID_ONE = 1;
    private static final int ID_TWO = 2;
    private static final int ID_EARN = 3;
    private static final int ID_FOUR = 4;
    private static final int ID_FIVE = 5;
    private RelativeLayout mView1;
    private RelativeLayout mView2;
    NavigationView navigationView;
    Boolean timer = false;
    private ViewPager viewPagerAirdropIntroduciton;
    private UnScrollableViewPager viewPagerAirdropQuiz;
    private PromosActivity.AirdropIntroducitonViewPagerAdapter myViewPagerAirdropIntroductionAdapter;
    private PromosActivity.AirdropQuizViewPagerAdapter myViewPagerAirdropQuizAdapter;
    private Dialog airdropPreIntroductionDialog;
    private Dialog airdropIntroductionDialog;
    private Dialog airdropQuizDialog;
    private Dialog discountDialog;
    private Dialog airdropPassedQuizDialog;
    private Dialog airdropNotPassedQuizDialog;
    private Dialog locked_contentDialog;
    private Dialog locked_contentPremiumDialog;
    int correct_answers = 0;
    isQuizAnswerChoosen isQuizAnswerChoosenn = new isQuizAnswerChoosen();
    LinearLayout lr_premium_plan_my_profile;
    ConstraintSet constraintSet;
    private int[] layouts;
    int page;
    Bundle extras = null;

    TimerTask timer2;
    private static final String SHOWCASE_ID = "Sequence Showcase";
    boolean active_init;
    boolean active_ads;
    boolean active_games;
    boolean active_market;
    boolean active_airdrops;
    boolean active_profile;
    String fromPointsActivity;
    FragmentManager fragmentManager;
    LottieAnimationView animationView;
    boolean animsnow = false;
    RatingBar ratingBar;
    TextView show_love_text;
    private ActionBarDrawerToggle t;
    ConstraintLayout scrollLayout;
    static CustomSwipeToRefresh mSwipeRefreshLayout;
    MenuItem changeItem;
    AppCompatActivity activity;
    MenuItem helpItem;
    private PromosContract.Presenter mMoviesPresenter;
    private PromosAdapter mPromosAdapter;
    private List<Promo> mMoviesList;
    private int mNavigation;
    private boolean mForceLoad;
    private LinearLayoutManager mLinearLayoutManager;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    public BottomSheetDialogReportBug report_dialog;
    @BindView(R.id.rv_promos)
    ViewPager2 mRvPromos;
    @BindView(R.id.progress_loading)
    ProgressBar mProgressLoading;
    @BindView(R.id.empty_view)
    ConstraintLayout mEmptyView;
    @BindView(R.id.bottom_navigation)
    MeowBottomNavigation mBottomNavigation;
    Button button;
    static TextView nickname_drawer;
    View view_review_app;
    Dialog dialog;
    TextView textViewId;
    TextView textViewUsername;
    TextView textViewEmail;
    TextView textViewGender;
    TextView title_page;
    static TextView textViewFavouriteCount;
    static TextView textViewDoneCount;
    static TextView textViewPoints;
    static TextView textViewEarned;
    String currentVersion, latestVersion;
    private AppBarConfiguration mAppBarConfiguration;
    RatingBar mRatingScore;
    RelativeLayout animView;
    static Context context;
    int IDuser;
    private static String data_getFavoriteCount;
    private static String data_getFirstIntroduction;
    private static String data_getDoneCount;
    private static String data_getPoints;
    private static String data_getEarned;
    private static String data_getImage;
    private static String data_getNickname;
    private static String data_active_premium_yearly_discount;
    private static String data_discount_to;
    private static String data_how_much_procent;
    String data_airdrop_introduction;
    static CircleImageView iv;
    FrameLayout frame_layout_for_fragments;
    LayoutInflater factory;
    View DialogView;
    Dialog main_dialog;
    LinearLayout reklamy;
    LinearLayout promocje;
    LinearLayout gry;
    LinearLayout market;
    LinearLayout airdropy;
    RelativeLayout buttons;
    ImageView iv_reklamy;
    ImageView iv_gry;
    ImageView iv_promocje;
    ImageView iv_market;
    ImageView iv_airdropy;
    TextView text_reklamy;
    TextView text_promocje;
    TextView text_gry;
    TextView text_market;
    TextView text_airdropy;
    TextView points_count_top;
    String quiz_answer1;
    String quiz_answer2;
    String quiz_answer3;
    String quiz_answer4;
    String quiz_answer5;
    String quiz_answer6;
    String quiz_answer7;
    String quiz_answer8;
    String quiz_answer9;
    String quiz_answer10;
    String quiz_answer11;
    String quiz_answer12;
    String quiz_answer13;
    String quiz_answer14;
    String quiz_answer15;
    String quiz_answer16;
    String quiz_answer17;
    String quiz_answer18;
    String quiz_answer19;
    String quiz_answer20;
    String quiz_answer21;
    String quiz_answer22;
    String quiz_answer23;

    LottieAnimationView animationViewSnow1;
    LottieAnimationView animationViewSnow2;
    LottieAnimationView animationViewSnow3;
    int notificationId = 0;
    private static final String TAG = PromosActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView txtRegId, txtMessage;
    final Fragment fragmentMyProfile = new MyProfileFragment();
    final Fragment fragmentAwards = new AwardsFragment();
    final Fragment fragmentReferFriend = new ReferFriendFragment();
    final Fragment fragmentHelp = new HelpFragment();
    final Fragment fragmentAds = new AdsFragment();
    final Fragment fragmentGames = new GamesFragment();
    final Fragment fragmentMarket = new MarketFragment();
    final Fragment fragmentAirdrops = new AirdropsFragment();
    private int[] layouts_airdrop_introduction;
    private int[] layouts_airdrop_quiz;
    private TextView[] dots;
    static User user;
    final FragmentManager fm = getSupportFragmentManager();
    final FragmentTransaction transaction = fm.beginTransaction();
    View view;
    Fragment active = null;
    ImageView icon_favorites;
    ImageView icon_dones;
    ImageView icon_help;
    RelativeLayout rl_points;
    RelativeLayout rl_energy;
    ViewPager viewpagerDiscount;
    LinearLayout dotsLayoutDiscount;
    DiscountViewPagerAdapter discountViewPagerAdapter;
    RelativeLayout rl1;
    RelativeLayout rl2;

    @SuppressLint("InflateParams")
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));
        getWindow().setBackgroundDrawableResource(R.drawable.white);


        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_Profitz));

        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (isOnline(this)) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();

                            // Log and toast
                            String msg_token = getString(R.string.msg_token_fmt, token);


                            final String url = "https://yoururl.com/api/update_notification_token.php?user_id=" + user.getId() + "&token=" + token;

                            JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest
                                    (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                String status_update_notification = response.getString("status_update_notification");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // TODO: Handle error
                                            Log.e("error_update_notification", url);

                                        }
                                    });
                            Volley.newRequestQueue(context).add(jsonObjectRequest3);


                            Log.e("Token: ", msg_token);

                        }
                    });


        }
        MyPreferenceManager myPreferenceManager = MyPreferenceManager.getInstance(PromosActivity.this);
        if (myPreferenceManager.isLoggedIn()) {
            setContentView(R.layout.activity_promos);

            //  profile.setOnClickListener(new View.OnClickListener(){
            // @Override
            //  public void onClick(View v) {
            // startActivity(new Intent(MainActivity.this,PromosActivity.class));
            //   }
            // });
        } else {
            setContentView(R.layout.activity_login);
            // startActivity(new Intent(PromosActivity.this,LoginActivity.class));
            startActivity(new Intent(PromosActivity.this, WelcomeActivity.class));
            finish();
        }
        ButterKnife.bind(this);


        activity = this;
        active_init = true;
        active_ads = false;
        active_games = false;
        active_market = false;
        active_airdrops = false;
        active_profile = false;
        fragmentManager = getSupportFragmentManager();
        active = null;
        context = this;
        animView = findViewById(R.id.animView);
        animationViewSnow1 = findViewById(R.id.anim_snow1);
        animationViewSnow2 = findViewById(R.id.anim_snow2);
        animationViewSnow3 = findViewById(R.id.anim_snow3);
        lr_premium_plan_my_profile = findViewById(R.id.lr_premium_plan_my_profile);
        mBottomNavigation = findViewById(R.id.bottom_navigation);
        mRvPromos = findViewById(R.id.rv_promos);
        mEmptyView= findViewById(R.id.empty_view);
        /*
        animationView.setVisibility(View.VISIBLE);

        animationView
                .addAnimatorUpdateListener(
                        (animation) -> {
                            // Do something.

                        });
        animationView
                .playAnimation();
        animationView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runSnowAnim();
            }
        }, 5500);

        RenderScriptProvider renderScriptProvider = new RenderScriptProvider((Context)this);
        ImageView var10000 = (ImageView)this._$_findCachedViewById(R.id.cosmonaut);
        Intrinsics.checkExpressionValueIsNotNull(var10000, "cosmonaut");
        var10000.setClipToOutline(true);
        ((ShadowView)this._$_findCachedViewById(R.id.cosmonautShadow)).setBlurScript(new RenderScriptBlur(renderScriptProvider));

        ConstraintLayout cl = findViewById(R.id.constl);
        ImageView iv_round = findViewById(R.id.imgRound);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        bm = new RoundCard().setEffect(bm, 0.2f, ((ColorDrawable) cl.getBackground()).getColor());
        iv_round.setImageBitmap(bm);
*/


        scrollLayout = findViewById(R.id.scroll_layout);
        reklamy = findViewById(R.id.reklamy);
        promocje = findViewById(R.id.promocje);
        gry = findViewById(R.id.gry);
        market = findViewById(R.id.market);
        airdropy = findViewById(R.id.airdropy);

        buttons = findViewById(R.id.buttons);
        text_gry = findViewById(R.id.text_gry);
        text_reklamy = findViewById(R.id.text_reklamy);
        text_promocje = findViewById(R.id.text_promocje);
        text_market = findViewById(R.id.text_market);
        text_airdropy = findViewById(R.id.text_airdropy);


        layouts = new int[]{
                R.layout.premium_info_slide0,
                R.layout.premium_info_slide1,
                R.layout.premium_info_slide2,
                R.layout.premium_info_slide3};


        setBlackNavigationBar();

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
                    args.putString("source", "WelcomeActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });

        iv_promocje = findViewById(R.id.iv_promocje);
        iv_reklamy = findViewById(R.id.iv_reklamy);
        iv_gry = findViewById(R.id.iv_gry);
        iv_market = findViewById(R.id.iv_market);
        iv_airdropy = findViewById(R.id.iv_airdropy);

        promocje.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((active_ads) || (active_games) || (active_market) || (active_airdrops)) {
                    if (!(reklamy.getBackground() == null)) {
                        reklamy.setBackgroundResource(0);
                    }
                    if (!(gry.getBackground() == null)) {
                        gry.setBackgroundResource(0);
                    }
                    if (!(market.getBackground() == null)) {
                        market.setBackgroundResource(0);
                    }
                    if (!(airdropy.getBackground() == null)) {
                        airdropy.setBackgroundResource(0);
                    }
                    if (iv_promocje.getVisibility() == View.GONE) {
                        iv_promocje.setVisibility(View.VISIBLE);
                    }
                    if (iv_reklamy.getVisibility() == View.VISIBLE) {
                        iv_reklamy.setVisibility(View.GONE);
                    }
                    if (iv_gry.getVisibility() == View.VISIBLE) {
                        iv_gry.setVisibility(View.GONE);
                    }
                    if (iv_market.getVisibility() == View.VISIBLE) {
                        iv_market.setVisibility(View.GONE);
                    }
                    if (iv_airdropy.getVisibility() == View.VISIBLE) {
                        iv_airdropy.setVisibility(View.GONE);
                    }
                    promocje.setBackgroundResource(R.drawable.shape_black);
                    //text_reklamy.setTextColor(Color.parseColor("#000000"));
                    //  text_gry.setTextColor(Color.parseColor("#000000"));
                    // text_market.setTextColor(Color.parseColor("#000000"));
                    text_airdropy.setTextColor(Color.parseColor("#000000"));
                    text_promocje.setTextColor(Color.parseColor("#ffffff"));

                    if (rl_energy.getVisibility() == View.VISIBLE) {
                        rl_energy.setVisibility(View.GONE);
                    }
                    if (icon_dones.getVisibility() == View.GONE) {
                        icon_dones.setVisibility(View.VISIBLE);
                    }
                    if (icon_favorites.getVisibility() == View.GONE) {
                        icon_favorites.setVisibility(View.VISIBLE);
                    }

                    fm.beginTransaction().hide(active).commit();
                    frame_layout_for_fragments.setVisibility(View.GONE);
                    mMoviesPresenter.getPopularPromos();
                    onResume();
                    active = null;
                    active_init = true;
                    active_ads = false;
                    active_games = false;
                    active_market = false;
                    active_airdrops = false;
                    active_profile = false;
                }


            }
        });
        reklamy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    /*
                if (!active_ads) {
                              mRvPromos.setVisibility(View.GONE);
                              mEmptyView.setVisibility(View.VISIBLE);
                              frame_layout_for_fragments.setVisibility(View.VISIBLE);
                    if (!(promocje.getBackground() ==null)){
                        promocje.setBackgroundResource(0);
                    }
                    if (!(gry.getBackground() ==null)){
                        gry.setBackgroundResource(0);
                    }
                    if (!(market.getBackground() ==null)){
                        market.setBackgroundResource(0);
                    }
                    if (!(airdropy.getBackground() ==null)){
                        airdropy.setBackgroundResource(0);
                    }
                    if (iv_reklamy.getVisibility() == View.GONE){
                        iv_reklamy.setVisibility(View.VISIBLE);
                    }
                    if (iv_promocje.getVisibility() == View.VISIBLE){
                        iv_promocje.setVisibility(View.GONE);
                    }
                    if (iv_gry.getVisibility() == View.VISIBLE){
                        iv_gry.setVisibility(View.GONE);
                    }
                    if (iv_market.getVisibility() == View.VISIBLE){
                        iv_market.setVisibility(View.GONE);
                    }
                    if (iv_airdropy.getVisibility() == View.VISIBLE){
                        iv_airdropy.setVisibility(View.GONE);
                    }
                    reklamy.setBackgroundResource(R.drawable.shape_black);
                  //  text_reklamy.setTextColor(Color.parseColor("#ffffff"));
                    text_promocje.setTextColor(Color.parseColor("#000000"));
                   // text_gry.setTextColor(Color.parseColor("#000000"));
                   // text_market.setTextColor(Color.parseColor("#000000"));
                    text_airdropy.setTextColor(Color.parseColor("#000000"));
                    if (rl_energy.getVisibility() == View.GONE) {
                        rl_energy.setVisibility(View.VISIBLE);
                    }
                    if (icon_dones.getVisibility() == View.VISIBLE) {
                        icon_dones.setVisibility(View.GONE);
                    }
                    if (icon_favorites.getVisibility() == View.VISIBLE) {
                        icon_favorites.setVisibility(View.GONE);
                    }
                              if (active == null) {
                                  active = fragmentAds;
                                  fm.beginTransaction().show(fragmentAds).commit();
                              } else if (active == fragmentMyProfile) {
                                  fm.beginTransaction().hide(fragmentMyProfile).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }
                              else if (active == fragmentAwards) {
                                  fm.beginTransaction().hide(fragmentAwards).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }
                              else if (active == fragmentReferFriend) {
                                  fm.beginTransaction().hide(fragmentReferFriend).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }
                              else if (active == fragmentHelp) {
                                  fm.beginTransaction().hide(fragmentHelp).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }

                              else if (active == fragmentGames) {
                                  fm.beginTransaction().hide(fragmentGames).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }
                              else if (active == fragmentMarket) {
                                  fm.beginTransaction().hide(fragmentMarket).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }
                              else if (active == fragmentAirdrops) {

                                  fm.beginTransaction().hide(fragmentAirdrops).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }else {
                                  fm.beginTransaction().hide(active).show(fragmentAds).commit();
                                  active = fragmentAds;
                              }
                              active_init = false;
                                active_games = false;
                                active_market = false;
                                active_airdrops = false;
                                active_profile = false;
                              active_ads = true;
                          }

                */
            }
        });
        gry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                if (!active_games) {
                    mRvPromos.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                    frame_layout_for_fragments.setVisibility(View.VISIBLE);
                    if (!(promocje.getBackground() ==null)){
                        promocje.setBackgroundResource(0);
                    }
                    if (!(reklamy.getBackground() ==null)){
                        reklamy.setBackgroundResource(0);
                    }
                    if (!(market.getBackground() ==null)){
                        market.setBackgroundResource(0);
                    }
                    if (!(airdropy.getBackground() ==null)){
                        airdropy.setBackgroundResource(0);
                    }


                    if (iv_reklamy.getVisibility() == View.VISIBLE){
                        iv_reklamy.setVisibility(View.GONE);
                    }
                    if (iv_promocje.getVisibility() == View.VISIBLE){
                        iv_promocje.setVisibility(View.GONE);
                    }
                    if (iv_gry.getVisibility() == View.GONE){
                        iv_gry.setVisibility(View.VISIBLE);
                    }
                    if (iv_market.getVisibility() == View.VISIBLE){
                        iv_market.setVisibility(View.GONE);
                    }
                    if (iv_airdropy.getVisibility() == View.VISIBLE){
                        iv_airdropy.setVisibility(View.GONE);
                    }
                    gry.setBackgroundResource(R.drawable.shape_black);
                //    text_gry.setTextColor(Color.parseColor("#ffffff"));
                    text_promocje.setTextColor(Color.parseColor("#000000"));
                 //   text_reklamy.setTextColor(Color.parseColor("#000000"));
                    //  text_market.setTextColor(Color.parseColor("#000000"));
                    text_airdropy.setTextColor(Color.parseColor("#000000"));

                    if (rl_energy.getVisibility() == View.GONE) {
                        rl_energy.setVisibility(View.VISIBLE);
                    }
                    if (icon_dones.getVisibility() == View.VISIBLE) {
                        icon_dones.setVisibility(View.GONE);
                    }
                    if (icon_favorites.getVisibility() == View.VISIBLE) {
                        icon_favorites.setVisibility(View.GONE);
                    }
                    if (active == null) {
                        active = fragmentGames;
                        fm.beginTransaction().show(fragmentGames).commit();
                    } else if (active == fragmentMyProfile) {
                        fm.beginTransaction().hide(fragmentMyProfile).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    else if (active == fragmentAwards) {
                        fm.beginTransaction().hide(fragmentAwards).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    else if (active == fragmentReferFriend) {
                        fm.beginTransaction().hide(fragmentReferFriend).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    else if (active == fragmentHelp) {
                        fm.beginTransaction().hide(fragmentHelp).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    else if (active == fragmentAds) {
                        fm.beginTransaction().hide(fragmentAds).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    else if (active == fragmentMarket) {
                        fm.beginTransaction().hide(fragmentMarket).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    else if (active == fragmentAirdrops) {
                        fm.beginTransaction().hide(fragmentAirdrops).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    else {
                        fm.beginTransaction().hide(active).show(fragmentGames).commit();
                        active = fragmentGames;
                    }
                    active_init = false;
                    active_ads = false;
                    active_games = true;
                    active_market = false;
                    active_airdrops = false;
                    active_profile = false;
                }
                */
            }
        });
        market.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                if (!active_market) {
                    mRvPromos.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                    frame_layout_for_fragments.setVisibility(View.VISIBLE);
                    if (!(promocje.getBackground() ==null)){
                        promocje.setBackgroundResource(0);
                    }
                    if (!(reklamy.getBackground() ==null)){
                        reklamy.setBackgroundResource(0);
                    }
                    if (!(gry.getBackground() ==null)){
                        gry.setBackgroundResource(0);
                    }
                    if (!(airdropy.getBackground() ==null)){
                        airdropy.setBackgroundResource(0);
                    }


                    if (iv_reklamy.getVisibility() == View.VISIBLE){
                        iv_reklamy.setVisibility(View.GONE);
                    }
                    if (iv_promocje.getVisibility() == View.VISIBLE){
                        iv_promocje.setVisibility(View.GONE);
                    }
                    if (iv_gry.getVisibility() == View.VISIBLE){
                        iv_gry.setVisibility(View.GONE);
                    }
                    if (iv_market.getVisibility() == View.GONE){
                        iv_market.setVisibility(View.VISIBLE);
                    }
                    if (iv_airdropy.getVisibility() == View.VISIBLE){
                        iv_airdropy.setVisibility(View.GONE);
                    }
                    market.setBackgroundResource(R.drawable.shape_black);
                   // text_gry.setTextColor(Color.parseColor("#000000"));
                    text_promocje.setTextColor(Color.parseColor("#000000"));
                   // text_reklamy.setTextColor(Color.parseColor("#000000"));
                    //text_market.setTextColor(Color.parseColor("#ffffff"));
                    text_airdropy.setTextColor(Color.parseColor("#000000"));
                    if (rl_energy.getVisibility() == View.VISIBLE) {
                        rl_energy.setVisibility(View.GONE);
                    }
                    if (icon_dones.getVisibility() == View.VISIBLE) {
                        icon_dones.setVisibility(View.GONE);
                    }
                    if (icon_favorites.getVisibility() == View.VISIBLE) {
                        icon_favorites.setVisibility(View.GONE);
                    }
                    if (active == null) {
                        active = fragmentMarket;
                        fm.beginTransaction().show(fragmentMarket).commit();
                    } else if (active == fragmentMyProfile) {
                        fm.beginTransaction().hide(fragmentMyProfile).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    else if (active == fragmentAwards) {
                        fm.beginTransaction().hide(fragmentAwards).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    else if (active == fragmentReferFriend) {
                        fm.beginTransaction().hide(fragmentReferFriend).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    else if (active == fragmentHelp) {
                        fm.beginTransaction().hide(fragmentHelp).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    else if (active == fragmentAds) {
                        fm.beginTransaction().hide(fragmentAds).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    else if (active == fragmentGames) {
                        fm.beginTransaction().hide(fragmentGames).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    else if (active == fragmentAirdrops) {
                        fm.beginTransaction().hide(fragmentAirdrops).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    else {
                        fm.beginTransaction().hide(active).show(fragmentMarket).commit();
                        active = fragmentMarket;
                    }
                    active_init = false;
                    active_ads = false;
                    active_games = false;
                    active_market = true;
                    active_airdrops = false;
                    active_profile = false;

                }
                */
            }
        });
        airdropy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!active_airdrops) {

                    final String url = "https://yoururl.com/api/get_user_data.php?user_id=" + user.getId();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        if (data_airdrop_introduction.equals("0")) {
                                            showAirdropIntroductionDialog(activity);

                                        } else {
                                            mRvPromos.setVisibility(View.GONE);
                                            mEmptyView.setVisibility(View.VISIBLE);
                                            frame_layout_for_fragments.setVisibility(View.VISIBLE);
                                            if (!(promocje.getBackground() == null)) {
                                                promocje.setBackgroundResource(0);
                                            }
                                            if (!(reklamy.getBackground() == null)) {
                                                reklamy.setBackgroundResource(0);
                                            }
                                            if (!(gry.getBackground() == null)) {
                                                gry.setBackgroundResource(0);
                                            }
                                            if (!(market.getBackground() == null)) {
                                                market.setBackgroundResource(0);
                                            }


                                            if (iv_reklamy.getVisibility() == View.VISIBLE) {
                                                iv_reklamy.setVisibility(View.GONE);
                                            }
                                            if (iv_promocje.getVisibility() == View.VISIBLE) {
                                                iv_promocje.setVisibility(View.GONE);
                                            }
                                            if (iv_gry.getVisibility() == View.VISIBLE) {
                                                iv_gry.setVisibility(View.GONE);
                                            }
                                            if (iv_market.getVisibility() == View.VISIBLE) {
                                                iv_market.setVisibility(View.GONE);
                                            }
                                            if (iv_airdropy.getVisibility() == View.GONE) {
                                                iv_airdropy.setVisibility(View.VISIBLE);
                                            }
                                            airdropy.setBackgroundResource(R.drawable.shape_black);
                                            //text_gry.setTextColor(Color.parseColor("#000000"));
                                            text_promocje.setTextColor(Color.parseColor("#000000"));
                                            //  text_reklamy.setTextColor(Color.parseColor("#000000"));
                                            // text_market.setTextColor(Color.parseColor("#000000"));
                                            text_airdropy.setTextColor(Color.parseColor("#ffffff"));
                                            if (rl_energy.getVisibility() == View.VISIBLE) {
                                                rl_energy.setVisibility(View.GONE);
                                            }
                                            if (icon_dones.getVisibility() == View.VISIBLE) {
                                                icon_dones.setVisibility(View.GONE);
                                            }
                                            if (icon_favorites.getVisibility() == View.VISIBLE) {
                                                icon_favorites.setVisibility(View.GONE);
                                            }
                                            if (active == null) {
                                                active = fragmentAirdrops;
                                                fm.beginTransaction().show(fragmentAirdrops).commit();

                                            } else if (active == fragmentMyProfile) {
                                                fm.beginTransaction().hide(fragmentMyProfile).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            } else if (active == fragmentAwards) {
                                                fm.beginTransaction().hide(fragmentAwards).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            } else if (active == fragmentReferFriend) {
                                                fm.beginTransaction().hide(fragmentReferFriend).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            } else if (active == fragmentHelp) {
                                                fm.beginTransaction().hide(fragmentHelp).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            } else if (active == fragmentAds) {
                                                fm.beginTransaction().hide(fragmentAds).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            } else if (active == fragmentGames) {
                                                fm.beginTransaction().hide(fragmentGames).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            } else if (active == fragmentMarket) {
                                                fm.beginTransaction().hide(fragmentMarket).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            } else {
                                                fm.beginTransaction().hide(active).show(fragmentAirdrops).commit();
                                                active = fragmentAirdrops;
                                            }
                                            active_init = false;
                                            active_ads = false;
                                            active_games = false;
                                            active_market = false;
                                            active_airdrops = true;
                                            active_profile = false;

                                        }
                                    } catch (Exception e) {
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
            }
        });
        extras = getIntent().getExtras();
        if (extras != null) {
            fromPointsActivity = extras.getString("fromPoints");
            Log.d("fromPointsActivity", "x: " + fromPointsActivity);

            //The key argument here must match that used in the other activity
        }

        //  showDiscountPromoDialog(this);


        mView1 = findViewById(R.id.relative_info_promo);
        mView2 = findViewById(R.id.rl2);
        frame_layout_for_fragments = findViewById(R.id.frame_layout_for_fragments);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg_token = getString(R.string.msg_token_fmt, token);

                        Log.e("Token: ", msg_token);

                    }
                });


        if (isOnline(this)) {
            Log.d("internet status", "Internet Access");
            user = MyPreferenceManager.getInstance(this).getUser();
            init(Integer.parseInt(user.getId()));
            //    LayoutInflater inflater2 = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            //   view_review_app = inflater2.inflate(R.layout.review_app_custom_dialog, null);
            // show_love_text = view_review_app.findViewById(R.id.show_love_text);
            //ratingBar = (RatingBar) view_review_app.findViewById(R.id.rating_score);
            // ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            //    @Override
            //   public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            //     float ratingg = ratingBar.getRating();
            //  String rating_string = String.valueOf(ratingg);
            //   if (ratingg == 5) {
            //     show_love_text.setVisibility(View.VISIBLE);
            //   } else {
            //          show_love_text.setVisibility(View.GONE);
            //    }
            //  }
            //  });


            if (Integer.parseInt(user.getId()) == 1) {
                FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_PRIVATE_ADMIN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create the NotificationChannel
                    registerNotificationChannel("channel_done", "channel_done", "notification channel 1");
                    registerNotificationChannel("channel_buy", "channel_buy", "notification channel 2");

                }
            } else {
                FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Create the NotificationChannel
                    registerNotificationChannel("default", "default", "all other notifications");

                }

            }
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        if (Integer.parseInt(user.getId()) == 1) {
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_PRIVATE_ADMIN);
                        } else {
                            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                        }                    // now subscribe to `global` topic to receive app wide notifications
                        //Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();

                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received
                        String title = intent.getStringExtra("title");

                        String message = intent.getStringExtra("message");

                        //  Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                        // Create an explicit intent for an Activity in your app
                        //Intent intent = new Intent(this, AlertDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        notificationManager.notify(notificationId, builder.build());
                        // txtMessage.setText(message);
                    }
                }
            };



            // getSupportActionBar().setElevation(0); usuwa cień z ctionbara

            IDuser = Integer.parseInt(user.getId());
           /* textViewFavouriteCount = findViewById(R.id.textulubione);
            textViewDoneCount = findViewById(R.id.textwykonane);
            textViewPoints = findViewById(R.id.textpunkty);
            textViewEarned = findViewById(R.id.txtzarobione);
*/
            //getting the current user
            // ImageView imageView = (ImageView) findViewById(R.id.nav_header_circleimageview_id);
            //Picasso.with(context).load(imageUrl).transform(new CircleTransform()).into(imageView);


            //mBottomNavigation.setCircleColor(ContextCompat.getColor(context, R.color.bg_Profitz));
            mBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.icon_account));
            mBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.icon_giftbox));
            mBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_attach_money_black_24dp));
            mBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.icon_friends));
            mBottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_baseline_help_outline_black_24dp));

            mForceLoad = true;
            mNavigation = 0;
            if (savedInstanceState != null) {
                // Restore value of members from saved state
                mMoviesList = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
                mNavigation = savedInstanceState.getInt(NAVIGATION);
                // If we are in favorites movies we force the load of the new list
                mForceLoad = mNavigation == 2;
            }
          //  DiscreteScrollLayoutManager mLayoutManager = (DiscreteScrollLayoutManager) mRvPromos.getLayoutManager();
            //mLinearLayoutManager = new CarouselLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            //mRvPromos.setLayoutManager(mLinearLayoutManager);
            mSwipeRefreshLayout = (CustomSwipeToRefresh) findViewById(R.id.swipe_container);
       /*     mRvPromos.addScrollListener(new DiscreteScrollView.ScrollListener() {
                @Override
                public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
                    try {

                        int firstPos = currentPosition;
                        if (firstPos > 0) {
                            mSwipeRefreshLayout.setEnabled(false);
                        } else {
                            mSwipeRefreshLayout.setEnabled(true);
                            if (mRvPromos.getScrollState() == 1)
                                if (mSwipeRefreshLayout.isRefreshing())
                                    mRvPromos.stopScroll();
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Scroll Error : " + e.getLocalizedMessage());
                    }
                }
            });
*/
            mSwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            mSwipeRefreshLayout.setRefreshing(true);
                           init(Integer.parseInt(user.getId()));
                           if (active == fragmentAirdrops){
                               active_airdrops = true;
                               Fragment frag = new AirdropsFragment();
                               FragmentManager fragmentManager = getSupportFragmentManager();
                               fragmentManager.beginTransaction().replace(R.id.frame_layout_for_fragments, frag).commit();
                               active = fragmentAirdrops;
                               mSwipeRefreshLayout.setRefreshing(false);
                           }
                           else{
                               if ((active == fragmentAwards) || (active == fragmentMyProfile) || (active == fragmentReferFriend) || (active == fragmentHelp) || (active == fragmentAds) || (active == fragmentGames)|| (active == fragmentMarket)) {
                                   mSwipeRefreshLayout.setRefreshing(false);
                               } else {
                                   onResume();
                               }
                           }

                        }
                    }
            );
            //mBottomNavigation.setOnNavigationItemSelectedListener(this);
            //setTitle("Promocje");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                  mBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
                @Override
                public void onClickItem(MeowBottomNavigation.Model item) {
                }
            });
            mBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
                @Override
                public void onReselectItem(MeowBottomNavigation.Model item) {
                }
            });
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentAirdrops, "8")
                    .hide(fragmentAirdrops).commit();
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentMarket, "7")
                    .hide(fragmentMarket).commit();
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentGames, "6")
                    .hide(fragmentGames).commit();
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentAds, "5")
                    .hide(fragmentAds).commit();
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentHelp, "4")
                    .hide(fragmentHelp).commit();
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentReferFriend, "3")
                    .hide(fragmentReferFriend).commit();
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentAwards, "2")
                    .hide(fragmentAwards).commit();
            fm.beginTransaction().add(R.id.frame_layout_for_fragments, fragmentMyProfile, "1")
                    .hide(fragmentMyProfile).commit();


            constraintSet = new ConstraintSet();
            icon_dones = findViewById(R.id.icon_dones);
            icon_favorites = findViewById(R.id.icon_favorites);
            icon_help = findViewById(R.id.icon_help);
            rl_points = findViewById(R.id.rl_points);
            rl_energy = findViewById(R.id.rl_energy);
            title_page = findViewById(R.id.title_page);
            points_count_top = findViewById(R.id.points_count_top);

            rl1 = (RelativeLayout) findViewById(R.id.nav_bar);
            rl2 = (RelativeLayout) findViewById(R.id.nav_bar_non_visible);
            rl_points.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(PromosActivity.this, PointsActivity.class));
                }
            });
            icon_dones.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(PromosActivity.this, DoneActivity.class));
                }
            });
            icon_favorites.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(PromosActivity.this, FavoriteActivity.class));
                }
            });
            icon_help.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (active == fragmentReferFriend)
                    {
                        startActivity(new Intent(context, ReferFriendHowItWorksActivity.class));
                    }
                    else{
                        //showTutorSequence(500);
                       // dialog3.show();
                        BottomSheetDialogNeedHelp bottomSheetHelp = new BottomSheetDialogNeedHelp();
                        bottomSheetHelp.show(getSupportFragmentManager(), bottomSheetHelp.getTag());
                    }

                }
            });



            mBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {

                @Override
                public void onShowItem(MeowBottomNavigation.Model item) {

                    switch (item.getId()) {
                        case ID_EARN:


                            constraintSet.clone(scrollLayout);
                            constraintSet.connect(R.id.frame_layout_for_fragments,ConstraintSet.TOP,R.id.nav_bar,ConstraintSet.BOTTOM,0);
                            constraintSet.applyTo(scrollLayout);


                            //changeItem.setVisible(true);
                            if ((active_ads) && (!active_init)) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_ads = true;
                                fm.beginTransaction().hide(active).show(fragmentAds).commit();
                                active = fragmentAds;

                                //setTitle("Reklamy");
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                            else if ((active_games) && (!active_init)) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_games = true;
                                fm.beginTransaction().hide(active).show(fragmentGames).commit();
                                active = fragmentGames;

                                //setTitle("Reklamy");
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                            else if ((active_market) && (!active_init)) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_market = true;
                                fm.beginTransaction().hide(active).show(fragmentMarket).commit();
                                active = fragmentMarket;
                            }
                            else if ((active_airdrops) && (!active_init)) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_airdrops = true;
                                fm.beginTransaction().hide(active).show(fragmentAirdrops).commit();
                                active = fragmentAirdrops;
                            }
                            else if ((active_profile) && (!active_init)){
                                    if (buttons.getVisibility() == View.GONE){
                                        buttons.setVisibility(View.VISIBLE);
                                    }
                                    active_profile = false;
                                    active_init = true;
                                    if (frame_layout_for_fragments.getVisibility() == View.VISIBLE) {
                                        frame_layout_for_fragments.setVisibility(View.GONE);
                                    }
                                    mMoviesPresenter.getPopularPromos();
                                    onResume();

                                    //setTitle("Promocje");
                                    //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                            else if ((active_profile) && (!active_init)){
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_profile = false;
                                active_init = true;
                                if (frame_layout_for_fragments.getVisibility() == View.VISIBLE) {
                                    frame_layout_for_fragments.setVisibility(View.GONE);
                                }
                                mMoviesPresenter.getPopularPromos();
                                onResume();

                                //setTitle("Promocje");
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                            else if (!active_ads) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_init = true;
                                if (frame_layout_for_fragments.getVisibility() == View.VISIBLE) {
                                    frame_layout_for_fragments.setVisibility(View.GONE);
                                }
                                mMoviesPresenter.getPopularPromos();
                                onResume();
                                //setTitle("Promocje");
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                            else if (!active_games) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_init = true;
                                if (frame_layout_for_fragments.getVisibility() == View.VISIBLE) {
                                    frame_layout_for_fragments.setVisibility(View.GONE);
                                }
                                mMoviesPresenter.getPopularPromos();
                                onResume();

                                //setTitle("Promocje");
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                            else if (!active_market) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_init = true;
                                if (frame_layout_for_fragments.getVisibility() == View.VISIBLE) {
                                    frame_layout_for_fragments.setVisibility(View.GONE);
                                }
                                mMoviesPresenter.getPopularPromos();
                                onResume();

                                //setTitle("Promocje");
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }
                            else if (!active_airdrops) {
                                if (buttons.getVisibility() == View.GONE){
                                    buttons.setVisibility(View.VISIBLE);
                                }
                                active_init = true;
                                if (frame_layout_for_fragments.getVisibility() == View.VISIBLE) {
                                    frame_layout_for_fragments.setVisibility(View.GONE);
                                }
                                mMoviesPresenter.getPopularPromos();
                                onResume();

                                //setTitle("Promocje");
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }



                            getWindow().setBackgroundDrawableResource(R.drawable.white);

                            rl1.setVisibility(View.VISIBLE);
                            rl2.setVisibility(View.VISIBLE);
                            title_page.setText("Zarabiaj");
                            if (icon_dones.getVisibility() == View.GONE) {
                                icon_dones.setVisibility(View.VISIBLE);
                            }
                            if (icon_favorites.getVisibility() == View.GONE) {
                                icon_favorites.setVisibility(View.VISIBLE);
                            }
                            buttons.setVisibility(View.VISIBLE);
                            frame_layout_for_fragments.setVisibility(View.GONE);
                            break;
                        case ID_TWO:

                            if (icon_dones.getVisibility() == View.VISIBLE) {
                                icon_dones.setVisibility(View.GONE);
                            }
                            if (icon_favorites.getVisibility() == View.VISIBLE) {
                                icon_favorites.setVisibility(View.GONE);
                            }
                            constraintSet.clone(scrollLayout);
                            constraintSet.connect(R.id.frame_layout_for_fragments,ConstraintSet.TOP,R.id.nav_bar_non_visible,ConstraintSet.BOTTOM,0);
                            constraintSet.applyTo(scrollLayout);
                            if (buttons.getVisibility() == View.VISIBLE){
                                buttons.setVisibility(View.GONE);
                            }
                         //   changeItem.setVisible(false);
                            active_init = false;
                            if (mRvPromos.getVisibility() == View.VISIBLE) {
                                mRvPromos.setVisibility(View.GONE);
                                mEmptyView.setVisibility(View.VISIBLE);
                                frame_layout_for_fragments.setVisibility(View.VISIBLE);
                            }
                            if (active == null) {
                                fm.beginTransaction().show(fragmentAwards).commit();
                                active = fragmentAwards;
                            } else {
                                fm.beginTransaction().hide(active).show(fragmentAwards).commit();
                                active = fragmentAwards;
                            }
                            getWindow().setBackgroundDrawableResource(R.drawable.ic_underline_awards_top_nav);

                            title_page.setText("Nagrody");
                            rl1.setVisibility(View.GONE);
                            rl2.setVisibility(View.GONE);
                            //setTitle("Nagrody");
                            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            break;
                        case ID_ONE:
                            active_init = false;
                            active_profile = true;


                            final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

                            JsonObjectRequest jsonObjectRequestCheckPremium = new JsonObjectRequest
                                    (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                if (active_profile){
                                                    String premium = response.getString("premium");

                                                    if(premium.equals("1")){
                                                        lr_premium_plan_my_profile.setVisibility(View.VISIBLE);
                                                    }
                                                    else{
                                                        lr_premium_plan_my_profile.setVisibility(View.GONE);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // TODO: Handle error

                                        }
                                    });
                            Volley.newRequestQueue(context).add(jsonObjectRequestCheckPremium);

                            if (icon_dones.getVisibility() == View.VISIBLE) {
                                icon_dones.setVisibility(View.GONE);
                            }
                            if (icon_favorites.getVisibility() == View.VISIBLE) {
                                icon_favorites.setVisibility(View.GONE);
                            }
                            constraintSet.clone(scrollLayout);
                            constraintSet.connect(R.id.frame_layout_for_fragments,ConstraintSet.TOP,R.id.nav_bar_non_visible,ConstraintSet.BOTTOM,0);
                            constraintSet.applyTo(scrollLayout);
                             if (buttons.getVisibility() == View.VISIBLE){
                                buttons.setVisibility(View.GONE);
                            }
                            //changeItem.setVisible(false);
                            if (mRvPromos.getVisibility() == View.VISIBLE) {
                                mRvPromos.setVisibility(View.GONE);
                                mEmptyView.setVisibility(View.VISIBLE);
                                frame_layout_for_fragments.setVisibility(View.VISIBLE);
                            }

                            if (active == null) {
                                fm.beginTransaction().show(fragmentMyProfile).commit();
                                active = fragmentMyProfile;
                            } else {
                                fm.beginTransaction().hide(active).show(fragmentMyProfile).commit();
                                active = fragmentMyProfile;
                            }
                            getWindow().setBackgroundDrawableResource(R.drawable.white);

                            title_page.setText("Konto");
                            rl1.setVisibility(View.VISIBLE);
                            rl2.setVisibility(View.VISIBLE);
                            //setTitle("Moje konto");
                            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            break;
                        case ID_FOUR:
                            if (icon_dones.getVisibility() == View.VISIBLE) {
                                icon_dones.setVisibility(View.GONE);
                            }
                            if (icon_favorites.getVisibility() == View.VISIBLE) {
                                icon_favorites.setVisibility(View.GONE);
                            }
                            constraintSet.clone(scrollLayout);
                            constraintSet.connect(R.id.frame_layout_for_fragments,ConstraintSet.TOP,R.id.nav_bar_non_visible,ConstraintSet.BOTTOM,0);
                            constraintSet.applyTo(scrollLayout);
                             if (buttons.getVisibility() == View.VISIBLE){
                                buttons.setVisibility(View.GONE);
                            }
                           // changeItem.setVisible(false);
                            active_init = false;
                            if (mRvPromos.getVisibility() == View.VISIBLE) {
                                mRvPromos.setVisibility(View.GONE);
                                mEmptyView.setVisibility(View.VISIBLE);
                                frame_layout_for_fragments.setVisibility(View.VISIBLE);
                            }
                            if (active == null) {
                                fm.beginTransaction().show(fragmentReferFriend).commit();
                                active = fragmentReferFriend;
                            } else {
                                fm.beginTransaction().hide(active).show(fragmentReferFriend).commit();
                                active = fragmentReferFriend;
                            }
                            title_page.setText("Zaproś znajomego");

                            getWindow().setBackgroundDrawableResource(R.drawable.gold_gradient_shape);

                            rl1.setVisibility(View.GONE);
                            rl2.setVisibility(View.GONE);
                            //setTitle("Zaproś znajomego");
                            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            break;
                        case ID_FIVE:
                            if (icon_dones.getVisibility() == View.VISIBLE) {
                                icon_dones.setVisibility(View.GONE);
                            }
                            if (icon_favorites.getVisibility() == View.VISIBLE) {
                                icon_favorites.setVisibility(View.GONE);
                            }
                            constraintSet.clone(scrollLayout);
                            constraintSet.connect(R.id.frame_layout_for_fragments,ConstraintSet.TOP,R.id.nav_bar_non_visible,ConstraintSet.BOTTOM,0);
                            constraintSet.applyTo(scrollLayout);
                             if (buttons.getVisibility() == View.VISIBLE){
                                buttons.setVisibility(View.GONE);
                            }
                           // changeItem.setVisible(false);
                            active_init = false;
                            if (mRvPromos.getVisibility() == View.VISIBLE) {
                                mRvPromos.setVisibility(View.GONE);
                                mEmptyView.setVisibility(View.VISIBLE);
                                frame_layout_for_fragments.setVisibility(View.VISIBLE);
                            }
                            if (active == null) {
                                fm.beginTransaction().show(fragmentHelp).commit();
                                active = fragmentHelp;
                            } else {
                                fm.beginTransaction().hide(active).show(fragmentHelp).commit();
                                active = fragmentHelp;
                            }
                            getWindow().setBackgroundDrawableResource(R.drawable.gold_gradient_shape);

                            title_page.setText("Pomoc");
                            rl1.setVisibility(View.GONE);
                            rl2.setVisibility(View.GONE);
                            //setTitle("Pomoc");
                            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            break;

                    }

                }

            });
        factory = LayoutInflater.from(PromosActivity.this);
        DialogView = factory.inflate(R.layout.custom_load_layout, null);
        main_dialog = new Dialog(PromosActivity.this);
            setUpRecyclerView();


            if (fromPointsActivity == null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (buttons.getVisibility() == View.GONE){
                                buttons.setVisibility(View.VISIBLE);
                            }

                            mBottomNavigation.show(3, false);
                        } catch (Exception e) {
                            //
                        }
                    }
                }, 2300);
            }
           else if (fromPointsActivity != null && fromPointsActivity.equals("openReferFriend")){
               initPresenter();

               mBottomNavigation.show(4, false);
            }
            else if (fromPointsActivity != null && fromPointsActivity.equals("openAwards")){
               initPresenter();
               mBottomNavigation.show(2, false);
                }
            else{
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           if (buttons.getVisibility() == View.GONE){
                               buttons.setVisibility(View.VISIBLE);
                           }
                           mMoviesPresenter.getPopularPromos();
                           onResume();
                           mBottomNavigation.show(3, false);
                       } catch (Exception e) {
                           //
                       }
                   }
               }, 2300);
           }

    }
    else
    {
        openInternetConnectionErrorDialog();
    }
    }



    @Override
    protected void onStart() {
        super.onStart();
            active_init = true;

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setBlackNavigationBar() {
        Window window = getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.rgb(30,30,30));

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setWhiteNavigationBar() {
        Window window = getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.rgb(255,255,255));

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }



    public void openInternetConnectionErrorDialog(){
        final Dialog dialog_internet_connection = new Dialog(context); // Context, this, etc.
        dialog_internet_connection.setContentView(R.layout.dialog_internet_connection);
        dialog_internet_connection.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button click_dialogTaskInfo = (Button) dialog_internet_connection.findViewById(R.id.button_retry_internet_connection);
        click_dialogTaskInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_internet_connection.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        dialog_internet_connection.show();
    }

    private void registerNotificationChannel(String id, String name, String description) {
        Uri alarmSoundDone = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/done_notification");
        Uri alarmSoundBuy = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/buy_notification");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.YELLOW);

        if (name.equals("channel_buy")) {
            mChannel.setSound(alarmSoundBuy, null);
        } else if (name.equals("channel_done")) {
            mChannel.setSound(alarmSoundDone, null);
        }

        mChannel.enableVibration(true);
        mNotificationManager.createNotificationChannel(mChannel);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController( this, R.id.nav_host_fragment );
        return NavigationUI.navigateUp( navController, mAppBarConfiguration )
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // inflate menu from xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer, menu);
       changeItem = menu.findItem(R.id.action_settings);
       helpItem = menu.findItem(R.id.need_help_from_global);
       helpItem.setIcon(ContextCompat.getDrawable(this, R.drawable.help_icon));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.need_help_from_global) {
            //showTutorSequence(500);
            Bundle args = new Bundle();

            BottomSheetDialogDisabledFuture bottomSheetDialogDisabledFuture = new BottomSheetDialogDisabledFuture();
            args.putString("source","promosActivity");
            bottomSheetDialogDisabledFuture.setArguments(args);
            bottomSheetDialogDisabledFuture.show(getSupportFragmentManager(), bottomSheetDialogDisabledFuture.getTag());

            return true;
        }
       else if (id == R.id.action_settings) {

                if (active_init) {
                    if (active == fragmentAds) {
                        if (changeItem.isEnabled()) {
                            fm.beginTransaction().hide(active).commit();
                            changeItem.setEnabled(false);
                            changeItem.getIcon().setAlpha(100);
                            mRvPromos.setVisibility(View.GONE);
                            mEmptyView.setVisibility(View.VISIBLE);
                            frame_layout_for_fragments.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    changeItem.setEnabled(true);
                                    changeItem.getIcon().setAlpha(255);
                                }
                            }, 2000);// set time as per your requirement
                        }
                    } else {
                        if (changeItem.isEnabled()) {
                            changeItem.setEnabled(false);
                            changeItem.getIcon().setAlpha(100);
                            mRvPromos.setVisibility(View.GONE);
                            mEmptyView.setVisibility(View.VISIBLE);
                            frame_layout_for_fragments.setVisibility(View.VISIBLE);

                            if (active == null) {
                                active = fragmentAds;
                                fm.beginTransaction().show(fragmentAds).commit();
                            } else if (active == fragmentMyProfile) {
                                fm.beginTransaction().hide(fragmentMyProfile).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            else if (active == fragmentAwards) {
                                fm.beginTransaction().hide(fragmentAwards).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            else if (active == fragmentHelp) {
                                fm.beginTransaction().hide(fragmentHelp).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            else if (active == fragmentReferFriend) {
                                fm.beginTransaction().hide(fragmentReferFriend).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            else if (active == fragmentAirdrops) {
                                fm.beginTransaction().hide(fragmentAirdrops).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            else if (active == fragmentGames) {
                                fm.beginTransaction().hide(fragmentGames).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            else if (active == fragmentMarket) {
                                fm.beginTransaction().hide(fragmentMarket).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            else {
                                fm.beginTransaction().hide(active).show(fragmentAds).commit();
                                active = fragmentAds;
                            }
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    changeItem.setEnabled(true);
                                    changeItem.getIcon().setAlpha(255);
                                }
                            }, 2000);// set time as per your requirement

                           // setTitle("Reklamy");
                            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            active_init = false;
                            active_ads = true;
                            changeItem.setIcon(ContextCompat.getDrawable(this, R.drawable.promos_earn));
                        }
                    }
                }else {
                        if (changeItem.isEnabled()) {
                            changeItem.setEnabled(false);
                            changeItem.getIcon().setAlpha(100);

                            fm.beginTransaction().hide(active).commit();
                            frame_layout_for_fragments.setVisibility(View.GONE);
                            mMoviesPresenter.getPopularPromos();
                            onResume();
                            active = null;
                            //setTitle("Promocje");
                            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            active_init = true;
                            active_ads = false;
                            changeItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ads_earn));
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // This method will be executed once the timer is over
                                    changeItem.setEnabled(true);
                                    changeItem.getIcon().setAlpha(255);
                                }
                            }, 2000);// set time as per your requirement
                        }
            }

        }
          /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            if (!prefs.getBoolean("viewId", false)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("viewId", true);
                editor.commit();

               // GridLayoutManager layoutManager = new GridLayoutManager(this, 1,GridLayoutManager.VERTICAL, false);
                mPromosAdapter = new PromosAdapter(this, this,0);
                //mRvPromos.setLayoutManager(layoutManager);
                //mRvPromos.setHasFixedSize(true);
                InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(mPromosAdapter);
                mRvPromos.setAdapter(wrapper);
                mRvPromos.setItemTransformer(new ScaleTransformer.Builder()
                        .setMaxScale(1.05f)
                        .setMinScale(0.8f)
                        .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                        .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                        .build());
                showMovies(mMoviesList, mNavigation);
                onResume();
            }
            else if (prefs.getBoolean("viewId", false)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("viewId", false);
                editor.commit();

                GridLayoutManager layoutManager = new GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false);
                mPromosAdapter = new PromosAdapter(this, this,1);

                // mRvPromos.setLayoutManager(layoutManager);
                //mRvPromos.setHasFixedSize(true);
                InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(mPromosAdapter);
                mRvPromos.setAdapter(wrapper);
                mRvPromos.setItemTransformer(new ScaleTransformer.Builder()
                        .setMaxScale(1.05f)
                        .setMinScale(0.8f)
                        .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                        .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                        .build());
                showMovies(mMoviesList, mNavigation);
                onResume();

            }

            //  mPromosAdapter.changeView(viewId);
            return true;
        }*/
        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    public static boolean isOnline(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

        if(active_init) {
                initPresenter();
            }

            if (mForceLoad) {
                mMoviesPresenter.subscribe();
            }

            if (fromPointsActivity != null && fromPointsActivity.equals("openReferFriend")){
            if (icon_dones.getVisibility() == View.VISIBLE) {
                icon_dones.setVisibility(View.GONE);
            }
            if (icon_favorites.getVisibility() == View.VISIBLE) {
                icon_favorites.setVisibility(View.GONE);
            }
            constraintSet.clone(scrollLayout);
            constraintSet.connect(R.id.frame_layout_for_fragments,ConstraintSet.TOP,R.id.nav_bar_non_visible,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(scrollLayout);
            if (buttons.getVisibility() == View.VISIBLE){
                buttons.setVisibility(View.GONE);
            }
            // changeItem.setVisible(false);
            active_init = false;
            if (mRvPromos.getVisibility() == View.VISIBLE) {
                mRvPromos.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                frame_layout_for_fragments.setVisibility(View.VISIBLE);
            }
            if (active == null) {
                fm.beginTransaction().show(fragmentReferFriend).commit();
                active = fragmentReferFriend;
            } else {
                fm.beginTransaction().hide(active).show(fragmentReferFriend).commit();
                active = fragmentReferFriend;
            }
            title_page.setText("Zaproś znajomego");

            getWindow().setBackgroundDrawableResource(R.drawable.gold_gradient_shape);

            rl1.setVisibility(View.GONE);
            rl2.setVisibility(View.GONE);
        }
        else if (fromPointsActivity != null && fromPointsActivity.equals("openAwards")){
            if (icon_dones.getVisibility() == View.VISIBLE) {
                icon_dones.setVisibility(View.GONE);
            }
            if (icon_favorites.getVisibility() == View.VISIBLE) {
                icon_favorites.setVisibility(View.GONE);
            }
            constraintSet.clone(scrollLayout);
            constraintSet.connect(R.id.frame_layout_for_fragments,ConstraintSet.TOP,R.id.nav_bar_non_visible,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(scrollLayout);
            if (buttons.getVisibility() == View.VISIBLE){
                buttons.setVisibility(View.GONE);
            }
            //   changeItem.setVisible(false);
            active_init = false;
            if (mRvPromos.getVisibility() == View.VISIBLE) {
                mRvPromos.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                frame_layout_for_fragments.setVisibility(View.VISIBLE);
            }
            if (active == null) {
                fm.beginTransaction().show(fragmentAwards).commit();
                active = fragmentAwards;
            } else {
                fm.beginTransaction().hide(active).show(fragmentAwards).commit();
                active = fragmentAwards;
            }
            getWindow().setBackgroundDrawableResource(R.drawable.ic_underline_awards_top_nav);

            title_page.setText("Nagrody");
            rl1.setVisibility(View.GONE);
            rl2.setVisibility(View.GONE);
        }
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
    }
    public void showAirdropIntroductionDialog(AppCompatActivity activity){


        airdropPreIntroductionDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        airdropPreIntroductionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        airdropPreIntroductionDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        airdropPreIntroductionDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        airdropPreIntroductionDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        airdropPreIntroductionDialog.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        airdropPreIntroductionDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        airdropPreIntroductionDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        airdropPreIntroductionDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        airdropPreIntroductionDialog.setCancelable(false);
        airdropPreIntroductionDialog.setContentView(R.layout.dialog_needed_quiz);
        airdropPreIntroductionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView dismiss_dialog_introduction= airdropPreIntroductionDialog.findViewById(R.id.arrow_back);
        Button startIntroductionAirdrop = airdropPreIntroductionDialog.findViewById(R.id.startIntroductionAirdrop);

        dismiss_dialog_introduction.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropPreIntroductionDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropPreIntroductionDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropPreIntroductionDialog.dismiss();
            }
        });

        startIntroductionAirdrop.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                airdropPreIntroductionDialog.dismiss();
                startAirdropIntroduction(activity);
            }
        });



        airdropPreIntroductionDialog.show();

    }
    public class AirdropIntroducitonViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public AirdropIntroducitonViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts_airdrop_introduction[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts_airdrop_introduction.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public class AirdropQuizViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public AirdropQuizViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts_airdrop_quiz[position], container, false);
            container.addView(view);

            if (position == 0){
                RelativeLayout fragment1_option1 = container.findViewById(R.id.fragment1_option1);
                RelativeLayout fragment1_option2 = container.findViewById(R.id.fragment1_option2);
                RelativeLayout fragment1_option3 = container.findViewById(R.id.fragment1_option3);
                RelativeLayout fragment1_option4 = container.findViewById(R.id.fragment1_option4);

                fragment1_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment1_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer1="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);
                    }
                });
                fragment1_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment1_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment1_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer1="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment1_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment1_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment1_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer1="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment1_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment1_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment1_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment1_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer1="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
            }
            else if (position== 1){
                RelativeLayout fragment2_option1 = container.findViewById(R.id.fragment2_option1);
                RelativeLayout fragment2_option2 = container.findViewById(R.id.fragment2_option2);
                RelativeLayout fragment2_option3 = container.findViewById(R.id.fragment2_option3);
                RelativeLayout fragment2_option4 = container.findViewById(R.id.fragment2_option4);

                fragment2_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment2_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer2="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment2_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment2_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment2_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer2="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment2_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment2_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment2_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer2="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment2_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment2_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment2_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment2_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer2="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }

            else if (position == 2) {
                RelativeLayout fragment3_option1 = container.findViewById(R.id.fragment3_option1);
                RelativeLayout fragment3_option2 = container.findViewById(R.id.fragment3_option2);
                RelativeLayout fragment3_option3 = container.findViewById(R.id.fragment3_option3);
                RelativeLayout fragment3_option4 = container.findViewById(R.id.fragment3_option4);
                fragment3_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment3_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer3="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment3_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment3_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment3_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer3="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment3_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment3_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment3_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer3="0";
                    }
                });
                fragment3_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment3_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment3_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment3_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer3="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
            }
            else if (position == 3) {
                RelativeLayout fragment4_option1 = container.findViewById(R.id.fragment4_option1);
                RelativeLayout fragment4_option2 = container.findViewById(R.id.fragment4_option2);
                RelativeLayout fragment4_option3 = container.findViewById(R.id.fragment4_option3);
                RelativeLayout fragment4_option4 = container.findViewById(R.id.fragment4_option4);

                fragment4_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment4_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer4="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment4_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment4_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment4_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer4="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment4_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment4_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment4_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer4="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment4_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment4_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment4_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment4_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer4="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
            }
            else if (position == 4) {
                RelativeLayout fragment5_option1 = container.findViewById(R.id.fragment5_option1);
                RelativeLayout fragment5_option2 = container.findViewById(R.id.fragment5_option2);
                RelativeLayout fragment5_option3 = container.findViewById(R.id.fragment5_option3);
                RelativeLayout fragment5_option4 = container.findViewById(R.id.fragment5_option4);
                fragment5_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment5_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer5="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment5_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment5_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment5_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer5="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment5_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment5_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment5_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer5="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment5_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment5_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment5_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment5_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer5="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 5) {
                RelativeLayout fragment6_option1 = container.findViewById(R.id.fragment6_option1);
                RelativeLayout fragment6_option2 = container.findViewById(R.id.fragment6_option2);
                RelativeLayout fragment6_option3 = container.findViewById(R.id.fragment6_option3);
                RelativeLayout fragment6_option4 = container.findViewById(R.id.fragment6_option4);
                fragment6_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment6_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer6="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment6_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment6_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment6_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer6="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment6_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment6_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment6_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer6="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment6_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment6_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment6_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment6_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer6="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 6) {
                RelativeLayout fragment7_option1 = container.findViewById(R.id.fragment7_option1);
                RelativeLayout fragment7_option2 = container.findViewById(R.id.fragment7_option2);
                RelativeLayout fragment7_option3 = container.findViewById(R.id.fragment7_option3);
                RelativeLayout fragment7_option4 = container.findViewById(R.id.fragment7_option4);

                fragment7_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment7_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer7="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment7_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment7_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment7_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer7="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment7_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment7_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment7_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer7="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment7_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment7_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment7_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment7_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer7="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 7) {
                RelativeLayout fragment8_option1 = container.findViewById(R.id.fragment8_option1);
                RelativeLayout fragment8_option2 = container.findViewById(R.id.fragment8_option2);
                RelativeLayout fragment8_option3 = container.findViewById(R.id.fragment8_option3);
                RelativeLayout fragment8_option4 = container.findViewById(R.id.fragment8_option4);

                fragment8_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment8_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer8="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment8_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment8_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment8_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer8="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment8_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment8_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment8_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer8="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment8_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment8_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment8_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment8_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer8="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });


            }
            else if (position == 8) {
                RelativeLayout fragment9_option1 = container.findViewById(R.id.fragment9_option1);
                RelativeLayout fragment9_option2 = container.findViewById(R.id.fragment9_option2);
                RelativeLayout fragment9_option3 = container.findViewById(R.id.fragment9_option3);
                RelativeLayout fragment9_option4 = container.findViewById(R.id.fragment9_option4);

                fragment9_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment9_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer9="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment9_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment9_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment9_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer9="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment9_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment9_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment9_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer9="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment9_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment9_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment9_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment9_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer9="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });


            }
            else if (position == 9) {
                RelativeLayout fragment10_option1 = container.findViewById(R.id.fragment10_option1);
                RelativeLayout fragment10_option2 = container.findViewById(R.id.fragment10_option2);
                RelativeLayout fragment10_option3 = container.findViewById(R.id.fragment10_option3);
                RelativeLayout fragment10_option4 = container.findViewById(R.id.fragment10_option4);
                fragment10_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment10_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer10="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment10_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment10_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment10_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer10="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment10_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment10_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment10_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer10="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment10_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment10_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment10_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment10_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer10="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 10) {
                RelativeLayout fragment11_option1 = container.findViewById(R.id.fragment11_option1);
                RelativeLayout fragment11_option2 = container.findViewById(R.id.fragment11_option2);
                RelativeLayout fragment11_option3 = container.findViewById(R.id.fragment11_option3);
                RelativeLayout fragment11_option4 = container.findViewById(R.id.fragment11_option4);
                fragment11_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment11_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer11="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment11_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment11_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment11_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer11="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment11_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment11_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment11_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer11="0";
                    }
                });
                fragment11_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment11_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment11_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment11_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer11="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 11) {
                RelativeLayout fragment12_option1 = container.findViewById(R.id.fragment12_option1);
                RelativeLayout fragment12_option2 = container.findViewById(R.id.fragment12_option2);
                RelativeLayout fragment12_option3 = container.findViewById(R.id.fragment12_option3);
                RelativeLayout fragment12_option4 = container.findViewById(R.id.fragment12_option4);

                fragment12_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment12_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer12="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment12_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment12_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment12_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer12="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment12_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment12_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment12_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer12="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment12_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment12_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment12_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment12_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer12="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });


            }
            else if (position == 12) {
                RelativeLayout fragment13_option1 = container.findViewById(R.id.fragment13_option1);
                RelativeLayout fragment13_option2 = container.findViewById(R.id.fragment13_option2);
                RelativeLayout fragment13_option3 = container.findViewById(R.id.fragment13_option3);
                RelativeLayout fragment13_option4 = container.findViewById(R.id.fragment13_option4);

                fragment13_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment13_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer13="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment13_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment13_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment13_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer13="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment13_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment13_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment13_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer13="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment13_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment13_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment13_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment13_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer13="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 13) {
                RelativeLayout fragment14_option1 = container.findViewById(R.id.fragment14_option1);
                RelativeLayout fragment14_option2 = container.findViewById(R.id.fragment14_option2);
                RelativeLayout fragment14_option3 = container.findViewById(R.id.fragment14_option3);
                RelativeLayout fragment14_option4 = container.findViewById(R.id.fragment14_option4);


                fragment14_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment14_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer14="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment14_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment14_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment14_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer14="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment14_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment14_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment14_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer14="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment14_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment14_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment14_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment14_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer14="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 14) {
                RelativeLayout fragment15_option1 = container.findViewById(R.id.fragment15_option1);
                RelativeLayout fragment15_option2 = container.findViewById(R.id.fragment15_option2);
                RelativeLayout fragment15_option3 = container.findViewById(R.id.fragment15_option3);
                RelativeLayout fragment15_option4 = container.findViewById(R.id.fragment15_option4);


                fragment15_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment15_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer15="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment15_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment15_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment15_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer15="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment15_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment15_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment15_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer15="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment15_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment15_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment15_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment15_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer15="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 15) {
                RelativeLayout fragment16_option1 = container.findViewById(R.id.fragment16_option1);
                RelativeLayout fragment16_option2 = container.findViewById(R.id.fragment16_option2);
                RelativeLayout fragment16_option3 = container.findViewById(R.id.fragment16_option3);
                RelativeLayout fragment16_option4 = container.findViewById(R.id.fragment16_option4);


                fragment16_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment16_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer16="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment16_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment16_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment16_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer16="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment16_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment16_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment16_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer16="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment16_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment16_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment16_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment16_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer16="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 16) {
                RelativeLayout fragment17_option1 = container.findViewById(R.id.fragment17_option1);
                RelativeLayout fragment17_option2 = container.findViewById(R.id.fragment17_option2);


                fragment17_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment17_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment17_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer17="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment17_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment17_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment17_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer17="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
            }
            else if (position == 17) {
                RelativeLayout fragment18_option1 = container.findViewById(R.id.fragment18_option1);
                RelativeLayout fragment18_option2 = container.findViewById(R.id.fragment18_option2);
                RelativeLayout fragment18_option3 = container.findViewById(R.id.fragment18_option3);
                RelativeLayout fragment18_option4 = container.findViewById(R.id.fragment18_option4);

                fragment18_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment18_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer18="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment18_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment18_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment18_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer18="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment18_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment18_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment18_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer18="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment18_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment18_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment18_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment18_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer18="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
            }
            else if (position == 18) {
                RelativeLayout fragment19_option1 = container.findViewById(R.id.fragment19_option1);
                RelativeLayout fragment19_option2 = container.findViewById(R.id.fragment19_option2);
                RelativeLayout fragment19_option3 = container.findViewById(R.id.fragment19_option3);
                RelativeLayout fragment19_option4 = container.findViewById(R.id.fragment19_option4);
                fragment19_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment19_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer19="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment19_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment19_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment19_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer19="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment19_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment19_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment19_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer19="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment19_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment19_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment19_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment19_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer19="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 19) {
                RelativeLayout fragment20_option1 = container.findViewById(R.id.fragment20_option1);
                RelativeLayout fragment20_option2 = container.findViewById(R.id.fragment20_option2);
                RelativeLayout fragment20_option3 = container.findViewById(R.id.fragment20_option3);
                RelativeLayout fragment20_option4 = container.findViewById(R.id.fragment20_option4);
                fragment20_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment20_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer20="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment20_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment20_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment20_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer20="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment20_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment20_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment20_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer20="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment20_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment20_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment20_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment20_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer20="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });


            }
            else if (position == 20) {
                RelativeLayout fragment21_option1 = container.findViewById(R.id.fragment21_option1);
                RelativeLayout fragment21_option2 = container.findViewById(R.id.fragment21_option2);
                RelativeLayout fragment21_option3 = container.findViewById(R.id.fragment21_option3);
                RelativeLayout fragment21_option4 = container.findViewById(R.id.fragment21_option4);

                fragment21_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment21_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer21="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment21_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment21_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment21_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer21="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment21_option3.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment21_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment21_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer21="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment21_option4.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment21_option4.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment21_option3.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment21_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer21="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 21) {
                RelativeLayout fragment22_option1 = container.findViewById(R.id.fragment22_option1);
                RelativeLayout fragment22_option2 = container.findViewById(R.id.fragment22_option2);
                fragment22_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment22_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment22_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer22="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment22_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment22_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment22_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer22="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });

            }
            else if (position == 22) {
                RelativeLayout fragment23_option1 = container.findViewById(R.id.fragment23_option1);
                RelativeLayout fragment23_option2 = container.findViewById(R.id.fragment23_option2);

                fragment23_option1.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment23_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        fragment23_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        quiz_answer23="0";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
                fragment23_option2.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.CUPCAKE)
                    @Override
                    public void onClick(View view) {
                        fragment23_option2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_gold));
                        fragment23_option1.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.round_textedit_grey));
                        quiz_answer23="1";
                        isQuizAnswerChoosenn.setAnswerChoosen(true);

                    }
                });
            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts_airdrop_quiz.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


    private int getItemIntroduction(int i) {
        return viewPagerAirdropIntroduciton.getCurrentItem() + i;
    }
    private int getItemQuiz(int i) {
        return viewPagerAirdropQuiz.getCurrentItem() + i;
    }

    public void startAirdropIntroduction(AppCompatActivity activity){


        airdropIntroductionDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        airdropIntroductionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        airdropIntroductionDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        airdropIntroductionDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        airdropIntroductionDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        airdropIntroductionDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        airdropIntroductionDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        airdropIntroductionDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        airdropIntroductionDialog.setCancelable(false);
        airdropIntroductionDialog.setContentView(R.layout.dialog_airdrop_introduction);
        airdropIntroductionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setWhiteNavigationBar();
        airdropIntroductionDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        airdropIntroductionDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));

        ImageView dismiss_dialog_introduction= airdropIntroductionDialog.findViewById(R.id.arrow_back_introduction);
        Button startQuizAirdrop = airdropIntroductionDialog.findViewById(R.id.startQuizAirdrop);
        Button nextStepIntroductionAirdrop = airdropIntroductionDialog.findViewById(R.id.nextStepIntroductionQuizAirdrop);
        TextView title_introduction = airdropIntroductionDialog.findViewById(R.id.text_title_quiz_introduction);

        dismiss_dialog_introduction.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropIntroductionDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                airdropIntroductionDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropIntroductionDialog.dismiss();

            }
        });

        viewPagerAirdropIntroduciton = (ViewPager) airdropIntroductionDialog.findViewById(R.id.view_pager_airdrop_introduction);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts_airdrop_introduction = new int[]{
                R.layout.quiz_introduction_fragment_1,
                R.layout.quiz_introduction_fragment_2,
                R.layout.quiz_introduction_fragment_3,
                R.layout.quiz_introduction_fragment_4,
                R.layout.quiz_introduction_fragment_5,
                R.layout.quiz_introduction_fragment_6,
                R.layout.quiz_introduction_fragment_7,
                R.layout.quiz_introduction_fragment_8,
                R.layout.quiz_introduction_fragment_9,
                R.layout.quiz_introduction_fragment_10,
                R.layout.quiz_introduction_fragment_11,
                R.layout.quiz_introduction_fragment_12,
                R.layout.quiz_introduction_fragment_13,
                R.layout.quiz_introduction_fragment_14,
                R.layout.quiz_introduction_fragment_15,
                R.layout.quiz_introduction_fragment_16,
                R.layout.quiz_introduction_fragment_17
        };


        myViewPagerAirdropIntroductionAdapter = new PromosActivity.AirdropIntroducitonViewPagerAdapter();
        viewPagerAirdropIntroduciton.setAdapter(myViewPagerAirdropIntroductionAdapter);
        ViewPager.OnPageChangeListener viewPagerAirdropIntroductionPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts_airdrop_introduction.length - 1) {
                    // last page. make button text to GOT IT
                    nextStepIntroductionAirdrop.setVisibility(View.GONE);
                    startQuizAirdrop.setVisibility(View.VISIBLE);
                } else {
                    // still pages are left
                    startQuizAirdrop.setVisibility(View.GONE);
                    nextStepIntroductionAirdrop.setVisibility(View.VISIBLE);
                }

                if (position==0){
                    title_introduction.setText("Czym są kryptowaluty?");
                }
                else if (position==1){
                    title_introduction.setText("Historia i zastosowanie kryptowalut");
                }
                else if (position==2){
                    title_introduction.setText("Czym są stablecoiny?");
                }
                else if (position==3){
                    title_introduction.setText("Czym są altcoiny?");
                }
                else if (position==4){
                    title_introduction.setText("Czym jest klucz prywatny?");
                }
                else if (position==5){
                    title_introduction.setText("Gdzie najbezpieczniej przechowywać kryptowaluty?");
                }
                else if (position==6){
                    title_introduction.setText("Czym jest portfel papierowy?");
                }
                else if (position==7){
                    title_introduction.setText("Czym jest portfel sprzętowy?");
                }
                else if (position==8){
                    title_introduction.setText("Jakie ryzyko wiąże się z korzystania z kryptowalut");
                }
                else if (position==9){
                    title_introduction.setText("Czym są Airdropy?");
                }
                else if (position==10){
                    title_introduction.setText("Czym są Airdropy w Profitz?");
                }
                else if (position==11){
                    title_introduction.setText("Czym są Eksluzywne Airdropy w Profitz?");
                }
                else if (position==12){
                    title_introduction.setText("Dlaczego firmy rozdają swoje kryptowaluty za darmo?");
                }
                else if (position==13){
                    title_introduction.setText("Jak mogę wziąć udział w airdropach?");
                }
                else if (position==14){
                    title_introduction.setText("Kiedy otrzymam zrzut?");
                }
                else if (position==15){
                    title_introduction.setText("Dlaczego nie otrzymałem(am) zrzutu?");
                }
                else if (position==16){
                    title_introduction.setText("Przejdź quiz i otrzymaj dostęp do airdropów");
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };

        viewPagerAirdropIntroduciton.addOnPageChangeListener(viewPagerAirdropIntroductionPageChangeListener);

        nextStepIntroductionAirdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page if true launch MainActivity
                int current = getItemIntroduction(+1);
                if (current < layouts_airdrop_introduction.length) {
                    // move to next screen
                    viewPagerAirdropIntroduciton.setCurrentItem(current);
                } else {
                    startQuizAirdrop.setVisibility(View.VISIBLE);
                }
            }
        });


        startQuizAirdrop.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropIntroductionDialog.dismiss();
                startAirdropQuiz(activity);
            }
        });


        airdropIntroductionDialog.show();
    }


    public void startAirdropQuiz(AppCompatActivity activity){


        airdropQuizDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        airdropQuizDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        airdropQuizDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        airdropQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        airdropQuizDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        airdropQuizDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        airdropQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        airdropQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        airdropQuizDialog.setCancelable(false);
        airdropQuizDialog.setContentView(R.layout.dialog_quiz);

        airdropQuizDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setWhiteNavigationBar();
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        airdropQuizDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));

        ImageView dismiss_dialog_quiz= airdropQuizDialog.findViewById(R.id.arrow_back_quiz);
        Button nextStepQuiz = airdropQuizDialog.findViewById(R.id.nextStepQuiz);
        Button finishQuizAirdrop = airdropQuizDialog.findViewById(R.id.finishQuiz);

        isQuizAnswerChoosenn.setListener(new isQuizAnswerChoosen.ChangeListener() {
            @Override
            public void onChange() {
                if (isQuizAnswerChoosenn.isAnswerChoosen()){
                    nextStepQuiz.setBackground(null);
                    nextStepQuiz.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_button_shape));
                    finishQuizAirdrop.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_button_shape));

                }
                else{
                    nextStepQuiz.setBackground(null);
                    nextStepQuiz.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_grey_button_shape));
                    finishQuizAirdrop.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_grey_button_shape));

                }

            }
        });

        finishQuizAirdrop.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                calculateAnswersQuiz();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropQuizDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                airdropQuizDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropQuizDialog.dismiss();
            }
        });

        dismiss_dialog_quiz.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropQuizDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                airdropQuizDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropQuizDialog.dismiss();
            }
        });

        viewPagerAirdropQuiz = (UnScrollableViewPager) airdropQuizDialog.findViewById(R.id.view_pager_airdrop_quiz);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts_airdrop_quiz = new int[]{
                R.layout.quiz_answers_fragment_1,
                R.layout.quiz_answers_fragment_2,
                R.layout.quiz_answers_fragment_3,
                R.layout.quiz_answers_fragment_4,
                R.layout.quiz_answers_fragment_5,
                R.layout.quiz_answers_fragment_6,
                R.layout.quiz_answers_fragment_7,
                R.layout.quiz_answers_fragment_8,
                R.layout.quiz_answers_fragment_9,
                R.layout.quiz_answers_fragment_10,
                R.layout.quiz_answers_fragment_11,
                R.layout.quiz_answers_fragment_12,
                R.layout.quiz_answers_fragment_13,
                R.layout.quiz_answers_fragment_14,
                R.layout.quiz_answers_fragment_15,
                R.layout.quiz_answers_fragment_16,
                R.layout.quiz_answers_fragment_17,
                R.layout.quiz_answers_fragment_18,
                R.layout.quiz_answers_fragment_19,
                R.layout.quiz_answers_fragment_20,
                R.layout.quiz_answers_fragment_21,
                R.layout.quiz_answers_fragment_22,
                R.layout.quiz_answers_fragment_23
        };


        myViewPagerAirdropQuizAdapter = new PromosActivity.AirdropQuizViewPagerAdapter();
        viewPagerAirdropQuiz.setAdapter(myViewPagerAirdropQuizAdapter);
        ViewPager.OnPageChangeListener viewPagerAirdropIntroductionPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts_airdrop_quiz.length - 1) {
                    // last page. make button text to GOT IT
                    nextStepQuiz.setVisibility(View.GONE);
                    finishQuizAirdrop.setVisibility(View.VISIBLE);
                } else {
                    // still pages are left
                    finishQuizAirdrop.setVisibility(View.GONE);
                    nextStepQuiz.setVisibility(View.VISIBLE);
                }
                nextStepQuiz.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_button_shape));
                finishQuizAirdrop.setBackground(ContextCompat.getDrawable(context,R.drawable.gradient_button_shape));
                isQuizAnswerChoosenn.setAnswerChoosen(false);

                String stepId = "circle_step"+position;
                HorizontalScrollView steps_hr = airdropQuizDialog.findViewById(R.id.steps_hr);


                if (position >0){
                    int oldpos = position-1;
                    String stepIdOld = "circle_step"+oldpos;
                    int oldquizstepId = getResources().getIdentifier(stepIdOld, "id", getPackageName());
                    airdropQuizDialog.findViewById(oldquizstepId).setBackground(ContextCompat.getDrawable(context,R.drawable.circle_shape_done));
                }

                int actualquizstepId = getResources().getIdentifier(stepId, "id", getPackageName());
                airdropQuizDialog.findViewById(actualquizstepId).setBackground(ContextCompat.getDrawable(context,R.drawable.circle_shape_actual_step));
                RelativeLayout circle_scroll = airdropQuizDialog.findViewById(actualquizstepId);
                steps_hr.scrollTo(circle_scroll.getLeft()-400, 0);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };

        viewPagerAirdropQuiz.addOnPageChangeListener(viewPagerAirdropIntroductionPageChangeListener);

        nextStepQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isQuizAnswerChoosenn.isAnswerChoosen()){
                    // checking for last page if true launch MainActivity
                    int current = getItemQuiz(+1);
                    if (current < layouts_airdrop_quiz.length) {
                        // move to next screen
                        viewPagerAirdropQuiz.setCurrentItem(current);


                    } else {
                        // startQuizAirdrop.setVisibility(View.VISIBLE);
                    }
                }

            }
        });




        airdropQuizDialog.show();
    }


    public void showDiscountPromoDialog(AppCompatActivity activity, String how_much_procent, String discount_to){


        discountDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        discountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        discountDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        discountDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        discountDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        discountDialog.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        discountDialog.getWindow().setBackgroundDrawableResource(R.drawable.discount_bg);
        discountDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        discountDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
       // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        discountDialog.setCancelable(false);
        discountDialog.setContentView(R.layout.dialog_discount_promo);
        discountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dismiss_dialog_discount = discountDialog.findViewById(R.id.dismiss_dialog_discount);
        dismiss_dialog_discount.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                discountDialog.dismiss();
            }
        });

        final String url2 = "https://yoururl.com/api/get_prices.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            TextView text1_rl_discount = discountDialog.findViewById(R.id.text1_rl_discount);
                            text1_rl_discount.setText(how_much_procent+"% zniżki na roczną subskrypcję Premium!");
                            Double discount_to_double = Double.parseDouble(how_much_procent);
                            Double discount_procent = discount_to_double/100;


                            String premium_month_pln_price = response.getString("premium_month_pln_price");
                            String premium_month_monthly_pln_price = response.getString("premium_month_monthly_pln_price");

                            String premium_year_pln_price = response.getString("premium_year_pln_price");
                            String premium_year_monthly_pln_price = response.getString("premium_year_monthly_pln_price");

                            String premium_lifetime_pln_price = response.getString("premium_lifetime_pln_price");

                            TextView text4_first = discountDialog.findViewById(R.id.text4_first);
                            text4_first.setText(premium_month_monthly_pln_price);

                            TextView text3_middle = discountDialog.findViewById(R.id.text3_middle);
                            text3_middle.setText(premium_year_monthly_pln_price);
                            text3_middle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);



                            Double premium_year_monthly_pln_price_to_double = Double.parseDouble(premium_year_monthly_pln_price);
                            Double calculate_premium_year_monthly_pln_price_discount = premium_year_monthly_pln_price_to_double*discount_procent;
                            Double calculated_premium_year_monthly_pln_price_discount = premium_year_monthly_pln_price_to_double - calculate_premium_year_monthly_pln_price_discount;
                            DecimalFormat df = new DecimalFormat("#.##");
                            String calculated_premium_year_monthly_pln_price_discount_formatted  = df.format(calculated_premium_year_monthly_pln_price_discount);
                            String calculated_premium_year_monthly_pln_price_discount_to_string = String.valueOf(calculated_premium_year_monthly_pln_price_discount_formatted);


                            TextView text4_middle = discountDialog.findViewById(R.id.text4_middle);
                            text4_middle.setText(calculated_premium_year_monthly_pln_price_discount_to_string);

                            TextView text4_last = discountDialog.findViewById(R.id.text4_last);
                            text4_last.setText(premium_lifetime_pln_price);


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


        viewpagerDiscount= (ViewPager) discountDialog.findViewById(R.id.viewpagerDiscount);
        dotsLayoutDiscount = (LinearLayout) discountDialog.findViewById(R.id.layoutDotsDiscount);
        discountViewPagerAdapter = new DiscountViewPagerAdapter(this);
        viewpagerDiscount.setAdapter(discountViewPagerAdapter);
        viewpagerDiscount.addOnPageChangeListener(discountviewPagerPageChangeListener);
        TextView text3_rl_discount = discountDialog.findViewById(R.id.text3_rl_discount);


        page = 0;
        Handler handler2 = new android.os.Handler();

        timer2=new TimerTask() {
            @Override
            public void run() {
                handler2.post(new Runnable() {

                    @Override
                    public void run() {
                        viewpagerDiscount.setCurrentItem(page % 4); // 4= no. of page in viewpager
                        page++;
                    }
                });
            }
        };
        Timer time=new Timer();
        time.schedule(timer2, 0, 5000); //3000 millisecond for replace the viewpager page




        Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date eventdate = dateFormat.parse(discount_to);

                    Date currentdate = new Date(); //2nd date taken as current date

                    if(!currentdate.after(eventdate))
                    {
                        long diff = eventdate.getTime() - currentdate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        String days2 = String.format("%02d",days);
                        String hours2 = String.format("%02d",hours);
                        String mins2 = String.format("%02d",minutes);
                        String seconds2 = String.format("%02d",seconds);
                        if (days2.equals("00")){
                            String countdown = hours2+":"+mins2+":"+seconds2;
                            text3_rl_discount.setText(countdown);
                        }
                        else{
                            String countdown = days2+":"+hours2+":"+mins2+":"+seconds2;
                            text3_rl_discount.setText(countdown);

                        }

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable,0);


        // layouts of all welcome sliders
        // add few more layouts if you want

        addBottomDots(0);






        discountDialog.show();

    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
            mMoviesPresenter.unsubscribe();
        mSensorManager.unregisterListener(mShakeDetector);

    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    //@Override
    // protected void onDestroy() {
    //  super.onDestroy();
    // }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mPromosAdapter != null) {
            List<Promo> moviesList = mPromosAdapter.getList();
            if (moviesList != null) {
                outState.putParcelableArrayList(MOVIES_LIST, new ArrayList<>(mPromosAdapter.getList()));
                outState.putInt(NAVIGATION, mNavigation);
            } else {
            }
        } else {
        }

        super.onSaveInstanceState(outState);
    }



    @Override
    public void setPresenter(@NonNull PromosContract.Presenter presenter) {
            mMoviesPresenter = presenter;

    }

    public void init(int userID){
        //textViewId = findViewById(R.id.textViewId);
        //textViewUsername = findViewById(R.id.textViewUsername);
        //textViewEmail = findViewById(R.id.textViewEmail);


/*
        textViewFavouriteCount.setText(String.valueOf(user.getFavourite_count()));
        textViewDoneCount.setText(String.valueOf(user.getDone_count()));
        textViewPoints.setText(String.valueOf(user.getPoints()));
        textViewEarned.setText(String.valueOf(user.getEarned()));

*/
        final String url = "https://yoururl.com/api/get_user_data.php?user_id="+userID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            data_getNickname = response.getString("username");
                            data_getFavoriteCount = response.getString("favorite_count");
                            data_getFirstIntroduction= response.getString("first_introduction");
                            data_getDoneCount = response.getString("done_count");
                            data_getPoints = response.getString("points");
                            data_getEarned = response.getString("earned");
                            data_getImage = response.getString("image_url");
                            data_active_premium_yearly_discount = response.getString("active_yearly_discount");
                            data_discount_to = response.getString("discount_to");
                            data_how_much_procent = response.getString("how_much_procent");
                            data_airdrop_introduction = response.getString("airdrop_introduction");
                            data_airdrop_introduction = response.getString("airdrop_introduction");


                            if (data_active_premium_yearly_discount.equals("1")){

                        //        showDiscountPromoDialog(activity,data_how_much_procent,data_discount_to);
                            }


                            if (data_getFirstIntroduction.equals("0")){
                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        BottomSheetDialogFirstIntroduction bottomSheetFirstIntroduction = new BottomSheetDialogFirstIntroduction();
                                        bottomSheetFirstIntroduction.show(getSupportFragmentManager(), bottomSheetFirstIntroduction.getTag());
                                    }
                                }, 2500);

                            }




                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

                         */
                            points_count_top.setText(data_getPoints);
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
//        mSwipeRefreshLayout.setRefreshing(false);

    }





    @Override
    public void showPromos(List<Promo> promos, int nav) {
        Sentry.captureMessage("from showPromos");

        mEmptyView.setVisibility(View.GONE);
        mRvPromos.setVisibility(View.VISIBLE);
        mPromosAdapter.setPromosData(promos);
        // mBottomNavigation.getMenu().getItem(nav).setChecked(true);
        mNavigation = nav;
       MovableFloatingActionButton fab_c = findViewById(R.id.fab_chat);
        User user = MyPreferenceManager.getInstance(this).getUser();
        HashMap<String, Object> attributesMap = new HashMap<String, Object>();
        attributesMap.put("punkty", user.getPoints());
        attributesMap.put("wykonane", user.getDone_count());
        attributesMap.put("zarobione", user.getEarned());

        Sentry.captureMessage("from showpromos2");

        fab_c.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
             //   Customerly.openSupport(PromosActivity.this);
              /*  Customerly.registerUser(
                        user.getEmail(),
                        String.valueOf(user.getId()),                //OPTIONALLY you can pass the user ID or null
                        user.getUsername(),             //OPTIONALLY you can pass the user name or null
                        attributesMap
                ); */
            }
        });
    }

    public void showPassedQuizDialog(AppCompatActivity activity, String correct_answers_quiz_dialog){


        airdropPassedQuizDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        airdropPassedQuizDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        airdropPassedQuizDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        airdropPassedQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        airdropPassedQuizDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        airdropPassedQuizDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        airdropPassedQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        airdropPassedQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        airdropPassedQuizDialog.setCancelable(false);
        airdropPassedQuizDialog.setContentView(R.layout.dialog_passed_quiz);

        airdropPassedQuizDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setWhiteNavigationBar();
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        airdropPassedQuizDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        Button dismiss_dialog_passed_quiz = airdropPassedQuizDialog.findViewById(R.id.dismiss_dialog_passed_quiz);
        TextView text1 = airdropPassedQuizDialog.findViewById(R.id.text1);
        TextView text2 = airdropPassedQuizDialog.findViewById(R.id.text2);
        TextView text3 = airdropPassedQuizDialog.findViewById(R.id.text3);

        String text1_string = correct_answers_quiz_dialog + " / 23";
        text1.setText(text1_string);


        LottieAnimationView animationViewLoading = airdropPassedQuizDialog.findViewById(R.id.animationViewLoading);
        LottieAnimationView animationViewAccepted = airdropPassedQuizDialog.findViewById(R.id.animationViewAccepted);
        animationViewLoading.playAnimation();
        animationViewLoading.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationViewLoading.setVisibility(View.GONE);
                animationViewAccepted.setVisibility(View.VISIBLE);
                animationViewAccepted.playAnimation();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        text3.setVisibility(View.VISIBLE);
                        dismiss_dialog_passed_quiz.setVisibility(View.VISIBLE);
                    }
                }, 850);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });



        dismiss_dialog_passed_quiz.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropPassedQuizDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                airdropPassedQuizDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropPassedQuizDialog.dismiss();
            }
        });

        airdropPassedQuizDialog.show();
    }

    public void showNotPassedQuizDialog(AppCompatActivity activity, String correct_answers_quiz_dialog){


        airdropNotPassedQuizDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        airdropNotPassedQuizDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        airdropNotPassedQuizDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        airdropNotPassedQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        airdropNotPassedQuizDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        airdropNotPassedQuizDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        airdropNotPassedQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        airdropNotPassedQuizDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        airdropNotPassedQuizDialog.setCancelable(false);
        airdropNotPassedQuizDialog.setContentView(R.layout.dialog_notpassed_quiz);

        airdropNotPassedQuizDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setWhiteNavigationBar();
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        airdropNotPassedQuizDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));

        Button show_introduction = airdropNotPassedQuizDialog.findViewById(R.id.show_introduction);
        TextView start_quiz = airdropNotPassedQuizDialog.findViewById(R.id.start_quiz);
        TextView text1 = airdropNotPassedQuizDialog.findViewById(R.id.text1);
        TextView text2 = airdropNotPassedQuizDialog.findViewById(R.id.text2);
        TextView text3 = airdropNotPassedQuizDialog.findViewById(R.id.text3);
        ImageView arrow_back = airdropNotPassedQuizDialog.findViewById(R.id.arrow_back);
        String text1_string = correct_answers_quiz_dialog + " / 23";
        text1.setText(text1_string);

        LottieAnimationView animationViewLoading = airdropNotPassedQuizDialog.findViewById(R.id.animationViewLoading);
        LottieAnimationView animationViewDeclined = airdropNotPassedQuizDialog.findViewById(R.id.animationViewDeclined);
        animationViewLoading.playAnimation();
        animationViewLoading.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationViewLoading.setVisibility(View.GONE);


                animationViewDeclined.setVisibility(View.VISIBLE);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        text3.setVisibility(View.VISIBLE);
                        show_introduction.setVisibility(View.VISIBLE);
                        arrow_back.setVisibility(View.VISIBLE);
                        start_quiz.setVisibility(View.VISIBLE);
                    }
                }, 850);
                animationViewDeclined.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropNotPassedQuizDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                airdropNotPassedQuizDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropNotPassedQuizDialog.dismiss();
            }
        });
        show_introduction.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropNotPassedQuizDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                airdropNotPassedQuizDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropNotPassedQuizDialog.dismiss();
                startAirdropIntroduction(activity);
            }
        });
        start_quiz.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                airdropNotPassedQuizDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                airdropNotPassedQuizDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropNotPassedQuizDialog.dismiss();
                startAirdropQuiz(activity);
            }
        });
        airdropNotPassedQuizDialog.show();
    }
    public void showLockedContentDialog(AppCompatActivity activity){
        locked_contentDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        locked_contentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        locked_contentDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        locked_contentDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        locked_contentDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        locked_contentDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        locked_contentDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        locked_contentDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        locked_contentDialog.setCancelable(false);
        locked_contentDialog.setContentView(R.layout.dialog_exclusive_airdrop_locked);

        locked_contentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setWhiteNavigationBar();
        //getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
       // getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        locked_contentDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        TextView dismiss_dialog_content_locked = locked_contentDialog.findViewById(R.id.dismiss_dialog_locked_content);
        Button unlock_content = locked_contentDialog.findViewById(R.id.unlock_content);

        LottieAnimationView animationViewLocked = locked_contentDialog.findViewById(R.id.animationViewLocked);
        animationViewLocked.setSpeed(-1);
        animationViewLocked.playAnimation();

        unlock_content.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                locked_contentDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                locked_contentDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                //getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                locked_contentDialog.dismiss();
                //runpremiumbuy
            }
        });

        dismiss_dialog_content_locked.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                locked_contentDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                locked_contentDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
               // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                //getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                locked_contentDialog.dismiss();
            }
        });

        locked_contentDialog.show();
    }
    public void showLockedContentOnlyPremiumDialog(AppCompatActivity activity){
        locked_contentPremiumDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        locked_contentPremiumDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        locked_contentPremiumDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        locked_contentPremiumDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        locked_contentPremiumDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        locked_contentPremiumDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        locked_contentPremiumDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        locked_contentPremiumDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        locked_contentPremiumDialog.setCancelable(false);
        locked_contentPremiumDialog.setContentView(R.layout.dialog_locked_content);

        locked_contentPremiumDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setWhiteNavigationBar();
        //getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        // getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        locked_contentPremiumDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        TextView dismiss_dialog_content_locked = locked_contentPremiumDialog.findViewById(R.id.dismiss_dialog_locked_content);
        Button unlock_content = locked_contentPremiumDialog.findViewById(R.id.unlock_content);

        LottieAnimationView animationViewLocked = locked_contentPremiumDialog.findViewById(R.id.animationViewLocked);
        animationViewLocked.setSpeed(-1);
        animationViewLocked.playAnimation();

        unlock_content.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                locked_contentPremiumDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                locked_contentPremiumDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                //getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                locked_contentPremiumDialog.dismiss();
                //runpremiumbuy
            }
        });

        dismiss_dialog_content_locked.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setBlackNavigationBar();
                locked_contentPremiumDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                locked_contentPremiumDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.bg_Profitz));
                //getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                locked_contentPremiumDialog.dismiss();
            }
        });

        locked_contentPremiumDialog.show();
    }


    public void calculateAnswersQuiz(){
        correct_answers=0;
    if (quiz_answer1=="1"){
        correct_answers++;
    }
    if (quiz_answer2=="1"){
        correct_answers++;
    }
    if (quiz_answer3=="1"){
        correct_answers++;
    }
    if (quiz_answer3=="1"){
        correct_answers++;
    }
    if (quiz_answer4=="1"){
        correct_answers++;
    }
    if (quiz_answer5=="1"){
        correct_answers++;
    }
    if (quiz_answer6=="1"){
        correct_answers++;
    }
    if (quiz_answer7=="1"){
        correct_answers++;
    }
    if (quiz_answer8=="1"){
        correct_answers++;
    }
    if (quiz_answer9=="1"){
        correct_answers++;
    }
    if (quiz_answer10=="1"){
        correct_answers++;
    }
    if (quiz_answer11=="1"){
        correct_answers++;
    }
    if (quiz_answer12=="1"){
        correct_answers++;
    }
    if (quiz_answer13=="1"){
        correct_answers++;
    }
    if (quiz_answer14=="1"){
        correct_answers++;
    }
    if (quiz_answer15=="1"){
        correct_answers++;
    }
    if (quiz_answer16=="1"){
        correct_answers++;
    }
    if (quiz_answer17=="1"){
        correct_answers++;
    }
    if (quiz_answer18=="1"){
        correct_answers++;
    }
    if (quiz_answer19=="1"){
        correct_answers++;
    }
    if (quiz_answer20=="1"){
        correct_answers++;
    }
    if (quiz_answer21=="1"){
        correct_answers++;
    }
    if (quiz_answer22=="1"){
        correct_answers++;
    }
    if (quiz_answer23=="1"){
        correct_answers++;
    }

    if (correct_answers >=12){
        showPassedQuizDialog(activity, String.valueOf(correct_answers));

        final String url = "https://yoururl.com/api/unlock_airdrops.php?hash=b938A313ae04ed1SAA1239159bfed413cccb41bgfsdf33983b&correct_answers="+correct_answers+"&user_id="+user.getId();

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String response2 = response.getString("status");
                            if (response2.equals("1")) {
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
        Volley.newRequestQueue(context).add(jsonObjectRequest2);

    }
    else{
        showNotPassedQuizDialog(activity, String.valueOf(correct_answers));
    }
}

    private void getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();

    }

        public static void setWindowFlag(AppCompatActivity activity, final int bits, boolean on) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
    }

    @Override
    public void onButtonClicked(String text) {

    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
//It retrieves the latest version by scraping the content of current version from play store at runtime
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.profitz.app.";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();

            }catch (Exception e){
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion!=null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)){
                    if(!isFinishing()){ //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    }
                }
            }
            else
             //   background.start();
            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dostępna aktualizacja!");
        builder.setMessage("Nowa wersja aplikacji jest już dostępna w sklepie Play. Stale dbamy o Twój komfort użytkowania usuwając błędy i dodając nowe funkcje. Zaktualizuj Profitz do najnowszej wersji aby móc cieszyć się z jej użytkowania jeszcze bardziej!");
        builder.setPositiveButton("Zaaktualizuj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.profitz.app.")));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nie teraz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //  background.start();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }
    @Override
    public void showEmptyView(int nav) {
        mRvPromos.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        //     mBottomNavigation.getMenu().getItem(nav).setChecked(true);
    }
    public void hidecurrentfragment(){
        mRvPromos.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        setContentView(R.layout.fragment_home);
        //     Intent intent2 = new Intent(this, ReferFriend.class);
        //startActivity(intent2);
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

    public void snackbarAddAirdrop(int status){

                if (status==1){
               //     Snackbar snackbar = Snackbar.make(scrollLayout,  Html.fromHtml("<font color=\"#ffffff\">Airdrop został pomyślnie dodany i oczekuje na weryfikację.</font>"), Snackbar.LENGTH_LONG);
               //     snackbar.show();
                    Toasty.success(context, "Airdrop został pomyślnie dodany i oczekuje na weryfikację.").show();

                }
                else if (status==3){
                  //  Snackbar snackbar = Snackbar.make(scrollLayout,  Html.fromHtml("<font color=\"#ffffff\">Wystąpił błąd. Sprawdź połączenie z siecią.</font>"), Snackbar.LENGTH_LONG);
                    //snackbar.show();
                    Toasty.error(context, "Wystąpił błąd. Sprawdź połączenie z siecią.").show();

                }
                else{
                 //   Snackbar snackbar = Snackbar.make(scrollLayout, Html.fromHtml("<font color=\"#ffffff\">Wystąpił błąd. Spróbuj ponownie.</font>"), Snackbar.LENGTH_LONG);
                //    snackbar.setAnchorView(mBottomNavigation);
                //    snackbar.show();
                    Toasty.error(context, "Wystąpił błąd. Spróbuj ponownie.").show();

                }
    }

    @Override
    public void hideLoading() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                main_dialog.dismiss();
                handler.postDelayed(this, 1200);
            }
        }, 800);



    }

    @Override
    public void onPromoClick(Promo promo) {
        Intent DetailActivityIntent = new Intent(this, PromoDetailActivity.class);
        DetailActivityIntent.putExtra(PromoDetailActivity.INTENT_EXTRA_PROMO, promo);
        Log.i("extra", String.valueOf(promo));
        startActivity(DetailActivityIntent);
    }
    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
    private void setUpRecyclerView() {
       // HomeFeatureLayout homeFeatureLayout = new HomeFeatureLayout(this);

        //mLinearLayoutManager = new CarouselLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //mRvPromos.setLayoutManager(mLinearLayoutManager);
        //GridLayoutManager layoutManager
        // = new GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false);
       mPromosAdapter = new PromosAdapter(this, this,1, mRvPromos);
        mRvPromos.setNestedScrollingEnabled(true);
      //  mRvPromos.setHasFixedSize(true);
        mRvPromos.setFocusable(false);
      //  PagerSnapHelper snapHelper = new PagerSnapHelper();
        //snapHelper.attachToRecyclerView(mRvPromos);
        //mRvPromos.setItemViewCacheSize(20);
        mRvPromos.setDrawingCacheEnabled(true);
        mRvPromos.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
              // mRvPromos.setLayoutManager(layoutManager);
        //mRvPromos.setHasFixedSize(true);
        ///InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(mPromosAdapter);
        mRvPromos.setOffscreenPageLimit(2);
        mRvPromos.setPageTransformer(new SliderTransformer(2));
        mRvPromos.setAdapter(mPromosAdapter);
        mRvPromos.setClipToPadding(false);
        mRvPromos.setClipChildren(false);
        mRvPromos.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
     //  mRvPromos.setSlideOnFling(false);
       // mRvPromos.setSlideOnFlingThreshold(1500);
       /*mRvPromos.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());*/


        // SnapHelper snapHelper = new GravitySnapHelper(Gravity.END);
        //snapHelper.attachToRecyclerView(mRvPromos);
        Sentry.captureMessage("setuprecycler");

        showPromos(mMoviesList, mNavigation);
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayoutDiscount.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(60);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayoutDiscount.addView(dots[i]);
        }
        Log.d("curr", String.valueOf(currentPage));

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);

    }

    ViewPager.OnPageChangeListener discountviewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    public class DiscountViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private int[] imageResources = {R.drawable.image_discount_1, R.drawable.image_discount_2, R.drawable.image_discount_3};

        public DiscountViewPagerAdapter(Context context) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = layoutInflater.inflate(R.layout.layout_imges_discount, container, false);
            ImageView imageView = view.findViewById(R.id.imageView); // assuming you have an ImageView in your layout
            imageView.setImageResource(imageResources[position]);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return imageResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }



    private void initPresenter() {
            mMoviesPresenter = new PromosPresenter(
                    Repository.getInstance(RemoteDataSource.getInstance(),
                            LocalDataSource.getInstance(getApplicationContext())),
                    AppPreferences.getInstance(getApplicationContext()),
                    this,
                    SchedulerProvider.getInstance());

    }


    public static CharSequence getCountdownText(Context context, Date futureDate) {
        StringBuilder countdownText = new StringBuilder();

        // Calculate the time between now and the future date.
        long timeRemaining = futureDate.getTime() - new Date().getTime();

        // If there is no time between (ie. the date is now or in the past), do nothing
        if (timeRemaining > 0) {
            Resources resources = context.getResources();

            // Calculate the days/hours/minutes/seconds within the time difference.
            //
            // It's important to subtract the value from the total time remaining after each is calculated.
            // For example, if we didn't do this and the time was 25 hours from now,
            // we would get `1 day, 25 hours`.
            int days = (int) TimeUnit.MILLISECONDS.toDays(timeRemaining);
            timeRemaining -= TimeUnit.DAYS.toMillis(days);
            int hours = (int) TimeUnit.MILLISECONDS.toHours(timeRemaining);
            timeRemaining -= TimeUnit.HOURS.toMillis(hours);
            //   int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
            // timeRemaining -= TimeUnit.MINUTES.toMillis(minutes);
            //    int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(timeRemaining);

            // For each time unit, add the quantity string to the output, with a space.
            if (days > 0 && hours > 0) {
                countdownText.append("Zostało "+resources.getQuantityString(R.plurals.days, days, days) + " i "+resources.getQuantityString(R.plurals.hours, hours, hours));
                countdownText.append(" ");
            }
            else if (days > 0 && hours == 0) {
                countdownText.append("Zostało "+resources.getQuantityString(R.plurals.days, days, days));
                countdownText.append(" ");
            }
            else if (days == 0 && hours >0)
            {
                countdownText.append("Zostało "+resources.getQuantityString(R.plurals.hours, hours, hours));
                countdownText.append(" ");
            }
         /*   if (days > 0 || hours > 0 || minutes > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.minutes, minutes, minutes));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.seconds, seconds, seconds));
                countdownText.append(" ");
            } */
        }

        return countdownText.toString();
    }
    private void showTutorSequence(int millis) {
        MaterialShowcaseView.resetSingleUse(PromosActivity.this, SHOWCASE_ID);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(millis);
        final int COLOR_MASK = getResources().getColor(R.color.dark_bg_opacity);
        final int COLOR_DISMISS = getResources().getColor(R.color.colorAccent);

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {

                // Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        sequence.setOnItemDismissedListener(new MaterialShowcaseSequence.OnSequenceItemDismissedListener() {
            @Override
            public void onDismiss(MaterialShowcaseView itemView, int position) {

            }
        });

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mRvPromos.getChildAt(0).findViewById(R.id.relative_info_promo))
                        .setDismissText("Dalej")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("Tu wyświetla się ilość punktów przypisanych do twojego konta, które udało ci się zdobyć.")
                        .withRectangleShape(true)
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(mRvPromos.getChildAt(0).findViewById(R.id.text_how_long))
                        .setDismissText("Rozumiem")
                        .setMaskColour(COLOR_MASK)
                        .setDismissTextColor(COLOR_DISMISS)
                        .setContentText("W tym miejscu możesz zobaczyć ile twoje punkty mogą być warte w przeliczeniu na euro - w przypadku chęci wymiany ich. Puntky nie są walutą, także wirtualną i nie posiadają żadnej wartości, toteż nie można traktować ich jako pieniądz.")
                        .withRectangleShape(true)
                        .build()
        );

        sequence.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void goFullScreen() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void enableBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    protected void tintMenuItem(MenuItem menuItem, int color) {
        Drawable drawable = menuItem.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    protected void displayPhoneNumberIfCan(final PhoneNumberListener phoneNumberListener) {
        Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.READ_PHONE_STATE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        phoneNumberListener.onPhoneNumberFetched(tMgr.getLine1Number());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();
    }
}
