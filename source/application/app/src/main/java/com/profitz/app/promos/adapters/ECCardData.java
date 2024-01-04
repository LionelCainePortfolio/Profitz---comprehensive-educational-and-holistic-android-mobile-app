package com.profitz.app.promos.adapters;

import java.util.List;

import androidx.annotation.DrawableRes;

public interface ECCardData<T> {

    @DrawableRes
    Integer getMainBackgroundResource();

    @DrawableRes
    Integer getHeadBackgroundResource();

    List<T> getListItems();
}