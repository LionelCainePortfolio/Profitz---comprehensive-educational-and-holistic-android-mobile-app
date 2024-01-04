package com.profitz.app.base;

import io.reactivex.observers.DisposableObserver;


public class BaseObserver<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        // no-op by default.
    }

    @Override
    public void onComplete() {
        // no-op by default.
    }

    @Override
    public void onError(Throwable exception) {
        // no-op by default.
    }
}