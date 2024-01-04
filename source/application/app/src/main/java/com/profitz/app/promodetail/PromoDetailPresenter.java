package com.profitz.app.promodetail;

import androidx.annotation.NonNull;

import com.profitz.app.data.model.Promo;
import com.profitz.app.data.source.DataSource;
import com.profitz.app.util.schedulers.BaseSchedulerProvider;

public class PromoDetailPresenter implements PromoDetailContract.Presenter {

    @NonNull
    private final PromoDetailContract.View mPromoDetailView;

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final Promo mPromo;
    private String mVideoKey;

    public PromoDetailPresenter(@NonNull PromoDetailContract.View promoDetailView,
                                @NonNull DataSource repository,
                                @NonNull BaseSchedulerProvider schedulerProvider,
                                @NonNull Promo promo) {
        mPromoDetailView = promoDetailView;
        mRepository = repository;
        mSchedulerProvider = schedulerProvider;
        mPromo = promo;

        mPromoDetailView.setPresenter(this);

    }

    @Override
    public void subscribe() {
        getPromo();

    }

    @Override
    public void unsubscribe() {

    }
    @Override
    public void sharePromo() {
        mPromoDetailView.sharePromo(mPromo.getTitle(), mVideoKey);
    }

    private void getPromo() {
        mPromoDetailView.showPromo(mPromo);
    }


    @Override
    public void saveOrDeletePromoAsFavorite() {
        if (mPromo.isFavorite()) {
            deletePromoFromFavorites();
        } else {
            savePromoAsFavorite();
        }
    }


    private void savePromoAsFavorite() {

    }

    private void deletePromoFromFavorites() {

    }

}