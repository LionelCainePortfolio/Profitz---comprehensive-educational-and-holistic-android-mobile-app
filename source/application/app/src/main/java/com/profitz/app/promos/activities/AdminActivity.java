package com.profitz.app.promos.activities;


import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.tabs.TabLayout;
import com.profitz.app.R;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.TabAdapter;
import com.profitz.app.promos.fragments.AdminTab1PeopleFragment;
import com.profitz.app.promos.fragments.AdminTab2DonesFragment;
import com.profitz.app.promos.fragments.AdminTab3ReviewsFragment;
import com.profitz.app.promos.fragments.AdminTab4CommentsFragment;
import com.profitz.app.promos.fragments.AdminTab5NotificationsFragment;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.util.SwipeableWrapContentViewPager;

public class AdminActivity extends AppCompatActivity implements  BottomSheetDialogReportBug.BottomSheetListener {
Context context = this;
    String username;
    private TabAdapter adapterTab;
    private TabLayout tabLayout;
    private SwipeableWrapContentViewPager viewPager;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_admin);
        setTitle("Administracja");

        User user2 = MyPreferenceManager.getInstance( this ).getUser();
        int IDuser = Integer.parseInt(user2.getId());
        username = user2.getName();
    //    String imageUrl = "https://yoururl.com/api/images/users/"+IDuser+".png?type=small";
     //   ImageView imageView = (ImageView) findViewById(R.id.image_avatar);
      //  Picasso.with(context).load(imageUrl)
     //           .transform(new CircleTransform())
       //         .placeholder(R.drawable.user_placeholder_avatar)
           //     .error(R.drawable.user_placeholder_avatar)
             //   .into(imageView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // data to populate the RecyclerView with

        viewPager = (SwipeableWrapContentViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapterTab = new TabAdapter(getSupportFragmentManager());
        adapterTab.addFragment(new AdminTab1PeopleFragment(), "Tab 1");
        adapterTab.addFragment(new AdminTab2DonesFragment(), "Tab 2");
        adapterTab.addFragment(new AdminTab3ReviewsFragment(), "Tab 3");
        adapterTab.addFragment(new AdminTab4CommentsFragment(), "Tab 4");
        adapterTab.addFragment(new AdminTab5NotificationsFragment(), "Tab 5");
        viewPager.setAdapter(adapterTab);
        tabLayout.setupWithViewPager(viewPager);
        int[] tabIcons = {
                R.drawable.ic_baseline_people_alt_24,
                R.drawable.ic_baseline_done_unselected_24,
                R.drawable.ic_baseline_review_24_single_unselected,
                R.drawable.ic_baseline_comment_24_unselected,
                R.drawable.ic_baseline_notifications_24_unselected

        };
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
                    args.putString("source","AdminActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nestedSV);
        scrollView.setFillViewport(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0)
                {
                    tab.setIcon(R.drawable.ic_baseline_people_alt_24);
                   // tabLayout.getTabAt(0).setIcon(tabIcons[0]);
                    tabLayout.getTabAt(4).setIcon(R.drawable.ic_baseline_notifications_24_unselected);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_review_24_single_unselected);                   tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_comment_24_unselected);
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_comment_24_unselected);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_done_unselected_24);

                }
                else if (tab.getPosition()==1)
                {
                    tab.setIcon(R.drawable.ic_baseline_done_selected_24);
                    tabLayout.getTabAt(4).setIcon(R.drawable.ic_baseline_notifications_24_unselected);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_people_alt_24_unselected);
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_comment_24_unselected);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_review_24_single_unselected);                   tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_comment_24_unselected);

                }
                else if (tab.getPosition()==2)
                {
                    tab.setIcon(R.drawable.ic_baseline_review_24);
                    tabLayout.getTabAt(4).setIcon(R.drawable.ic_baseline_notifications_24_unselected);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_people_alt_24_unselected);
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_comment_24_unselected);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_done_unselected_24);

                }
                else if (tab.getPosition()==3)
                {
                    tab.setIcon(R.drawable.ic_baseline_comment_24_selected);
                    tabLayout.getTabAt(4).setIcon(R.drawable.ic_baseline_notifications_24_unselected);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_people_alt_24_unselected);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_done_unselected_24);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_review_24_single_unselected);

                }
                else if (tab.getPosition()==4)
                {
                    tab.setIcon(R.drawable.ic_baseline_notifications_24);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_people_alt_24_unselected);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_review_24_single_unselected);
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_comment_24_unselected);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_done_unselected_24);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

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
        getMenuInflater().inflate(R.menu.detail_my_informations_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.need_help_from_my_informations) {//mMovieDetailPresenter.shareMovie();
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