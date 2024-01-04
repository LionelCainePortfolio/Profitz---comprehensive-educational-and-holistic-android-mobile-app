package com.profitz.app.promos;

import androidx.annotation.NonNull;

import com.profitz.app.base.BaseObserver;
import com.profitz.app.data.Constants;
import com.profitz.app.data.model.Promo;
import com.profitz.app.data.source.DataSource;
import com.profitz.app.data.source.preferences.Preferences;
import com.profitz.app.promos.domain.GetFavoritePromos;
import com.profitz.app.promos.domain.GetPopularPromos;
import com.profitz.app.promos.domain.GetTopRatedPromos;
import com.profitz.app.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.sentry.Sentry;

public class PromosPresenter implements PromosContract.Presenter {

    private static final int NAV_POPULAR_PROMOS = 0;
    private static final int NAV_TOP_RATED_PROMOS = 1;
    private static final int NAV_FAVORITE_PROMOS = 2;

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final Preferences mPreferences;

    @NonNull
    private final PromosContract.View mPromosView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final GetPopularPromos mGetPopularPromos;
    private final GetTopRatedPromos mGetTopRatedPromos;
    private final GetFavoritePromos mGetFavoritePromos;


    public PromosPresenter(@NonNull DataSource repository,
                           @NonNull Preferences preferences,
                           @NonNull PromosContract.View PromosView,
                           @NonNull BaseSchedulerProvider schedulerProvider) {
        mRepository = repository;
        mPreferences = preferences;
        mPromosView = PromosView;

        mSchedulerProvider = schedulerProvider;

        mPromosView.setPresenter(this);

        mGetPopularPromos = new GetPopularPromos(mRepository,
                mSchedulerProvider.computation(),
                mSchedulerProvider.ui());
        mGetTopRatedPromos = new GetTopRatedPromos(mRepository,
                mSchedulerProvider.computation(),
                mSchedulerProvider.ui());
        mGetFavoritePromos = new GetFavoritePromos(mRepository,
                mSchedulerProvider.computation(),
                mSchedulerProvider.ui());

    }

    @Override
    public void subscribe() {

        switch (mPreferences.getCurrentDisplayedPromos()) {
            case Constants.PROMOS_POPULAR:
                getPopularPromos();
                break;
            case Constants.PROMOS_TOP_RATED:
                getTopRatedPromos();
                break;
            case Constants.PROMOS_FAVORITE:
                getFavoritePromos();
            default:
                break;
        }
    }

    @Override
    public void unsubscribe() {
        mGetPopularPromos.dispose();
        mGetTopRatedPromos.dispose();
        mGetFavoritePromos.dispose();
    }

    @Override
    public void getReferFriend(){

    }

    @Override
    public void getPopularPromos() {
        mPromosView.showLoading();
        mGetPopularPromos.execute(new PopularPromosListObserver(), null);
    }

    @Override
    public void getTopRatedPromos() {
        mPromosView.showLoading();
        mGetTopRatedPromos.execute(new TopRatedPromosListObserver(), null);
    }

    @Override
    public void getFavoritePromos() {
        mPromosView.showLoading();
        mGetFavoritePromos.execute(new TopRatedPromosListObserver(), null);
    }

    private void showPromos(List<Promo> promos, int nav) {
        if (promos.isEmpty()) {
            mPromosView.showEmptyView(nav);
        } else {
            mPromosView.showPromos(promos, nav);
        }
    }





    private final class PopularPromosListObserver extends BaseObserver<List<Promo>> {

        @Override
        public void onNext(List<Promo> promos) {

            Sentry.captureMessage("from presenter");
            showPromos(promos, NAV_POPULAR_PROMOS);
        }

        @Override
        public void onComplete() {
            mPromosView.hideLoading();
            mPreferences.setCurrentDisplayedPromos(Constants.PROMOS_POPULAR);
        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class TopRatedPromosListObserver extends BaseObserver<List<Promo>> {

        @Override
        public void onNext(List<Promo> promos) {
            showPromos(promos, NAV_TOP_RATED_PROMOS);
        }

        @Override
        public void onComplete() {
            mPromosView.hideLoading();
            mPreferences.setCurrentDisplayedPromos(Constants.PROMOS_TOP_RATED);
        }

        @Override
        public void onError(Throwable e) {

        }
    }


    private final class FavoritePromosListObserver extends BaseObserver<List<Promo>> {

        @Override
        public void onNext(List<Promo> promos) {
            showPromos(promos, NAV_FAVORITE_PROMOS);
        }

        @Override
        public void onComplete() {
            mPromosView.hideLoading();
            mPreferences.setCurrentDisplayedPromos(Constants.PROMOS_FAVORITE);
        }


    }


}