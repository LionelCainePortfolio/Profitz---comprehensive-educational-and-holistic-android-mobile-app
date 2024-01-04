package com.profitz.app.util;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private final MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    // private final LiveData<String> mText = (LiveData<String>) Transformations.map(mIndex, (Function<Integer, Integer>) input -> null);
    private final LiveData<String> mText;

    public PageViewModel(LiveData<String> mText) {
        this.mText = mText;
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}
