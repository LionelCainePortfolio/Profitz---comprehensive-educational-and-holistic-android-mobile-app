package com.profitz.app.promos.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.profitz.app.promos.fragments.ExtendedInstructionFragment;
import com.profitz.app.promos.fragments.SimplifyInstructionFragment;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter2 extends FragmentStateAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabAdapter2(FragmentActivity fa) {
        super(fa);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SimplifyInstructionFragment();
            case 1:
                return new ExtendedInstructionFragment();

        }
        return new SimplifyInstructionFragment();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}