package com.profitz.app;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public abstract class UseCase<T, Params> {

    private final Scheduler mThreadExecutor;
    private final Scheduler mPostExecutionThread;
    private final CompositeDisposable mDisposables;

    public UseCase(Scheduler threadExecutor, Scheduler postExecutionThread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mDisposables = new CompositeDisposable();
    }

    public abstract Observable<T> buildUseCaseObservable(Params params);


    public void execute(DisposableObserver<T> observer, Params params) {

        final Observable<T> observable = this.buildUseCaseObservable(params)
                                             .subscribeOn(mThreadExecutor)
                                             .observeOn(mPostExecutionThread);
        addDisposable(observable.subscribeWith(observer));
    }


    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }


    private void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }
}
