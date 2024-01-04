package com.profitz.app.promos.adapters;


import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 100;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();

    Fragment getItem(int position);

    Object instantiateItem(ViewGroup container, int position);
}
