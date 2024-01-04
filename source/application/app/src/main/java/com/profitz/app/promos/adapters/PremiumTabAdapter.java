package com.profitz.app.promos.adapters;

import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.profitz.app.util.PremiumViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PremiumTabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public PremiumTabAdapter(FragmentManager fm) {
        super(fm);

    }
    @NotNull
   @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);
    }
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Nullable
   // @Override
   // public CharSequence getPageTitle(int position) {
       // return mFragmentTitleList.get(position);
   // }
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    private int mCurrentPosition = -1;
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
            Fragment fragment = (Fragment) object;
            PremiumViewPager pager = (PremiumViewPager) container;
            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                //pager.measureCurrentView(fragment.getView());
            }
        }
    }
}