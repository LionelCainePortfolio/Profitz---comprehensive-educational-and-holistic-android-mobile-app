package com.profitz.app.promos.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.profitz.app.R;import com.profitz.app.promos.adapters.SectionsPagerAdapter;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.profitz.app.promos.fragments.FragmentStandardLicence1;
import com.profitz.app.promos.fragments.FragmentStandardLicence2;
import com.profitz.app.promos.fragments.FragmentStandardLicence3;
import com.legendmohe.slidingdrawabletablayout.SlidingDrawableTabLayout;
import com.squareup.seismic.ShakeDetector;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


public class BuyLicenceActivity extends AppCompatActivity implements  BottomSheetDialogReportBug.BottomSheetListener {

    SlidingDrawableTabLayout mTabs;
    View mIndicator;
    ViewPager mViewPager;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int indicatorWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_licence);

        //Assign view reference
        mTabs = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewPager);

        //Set up the view pager and fragments
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FragmentStandardLicence1.newInstance(), "7 dni");
        adapter.addFragment(FragmentStandardLicence2.newInstance(), "30 dni");
        adapter.addFragment(FragmentStandardLicence3.newInstance(), "8");
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);

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
                    args.putString("source","BuyLicenceActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });
        //////

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