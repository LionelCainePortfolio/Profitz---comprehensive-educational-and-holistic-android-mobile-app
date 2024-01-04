package com.profitz.app.promos.activities;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.adapters.TabAdapter;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.BottomSheetDialogUpgradeLevel;
import com.profitz.app.promos.fragments.MyInformationsTab1Fragment;
import com.profitz.app.util.SwipeableWrapContentViewPager;
import com.google.android.material.tabs.TabLayout;
import com.squareup.seismic.ShakeDetector;

public class MyInformationsActivity extends AppCompatActivity implements BottomSheetDialogUpgradeLevel.BottomSheetListener, BottomSheetDialogReportBug.BottomSheetListener  {
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

        setContentView( R.layout.activity_my_informations);
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
                    args.putString("source","MyInformationsActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////
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


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
        ft.replace(R.id.frame_container, new MyInformationsTab1Fragment());
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        ft.commit();
/*
        viewPager = (SwipeableWrapContentViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapterTab = new TabAdapter(getSupportFragmentManager());
        adapterTab.addFragment(new MyInformationsTab1Fragment(), "Tab 1");
        adapterTab.addFragment(new MyInformationsTab2Fragment(), "Tab 2");
        adapterTab.addFragment(new MyInformationsTab3Fragment(), "Tab 3");
        viewPager.setAdapter(adapterTab);
        tabLayout.setupWithViewPager(viewPager);
        int[] tabIcons = {
                R.drawable.ic_baseline_home_24,
                R.drawable.ic_baseline_notifications_24_unselected,
                R.drawable.ic_baseline_person_24_unselected
        };
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nestedSV);
        scrollView.setFillViewport(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0)
                {
                    tab.setIcon(R.drawable.ic_baseline_home_24);
                   // tabLayout.getTabAt(0).setIcon(tabIcons[0]);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_notifications_24_unselected);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_person_24_unselected);
                }
                else if (tab.getPosition()==1)
                {
                    tab.setIcon(R.drawable.ic_baseline_notifications_24);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24_unselected);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_person_24_unselected);
                }
                else if (tab.getPosition()==2)
                {
                    tab.setIcon(R.drawable.ic_baseline_person_24);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_notifications_24_unselected);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24_unselected);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
*/
    }
    public void refreshActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
    public void onButtonClicked(String text) {

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