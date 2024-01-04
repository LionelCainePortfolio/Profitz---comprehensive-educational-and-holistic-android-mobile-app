package com.profitz.app.promodetail;

import com.profitz.app.base.BasePresenter;
import com.profitz.app.base.BaseView;
import com.profitz.app.data.model.Promo;

public interface PromoDetailContract {

    interface View extends BaseView<Presenter> {

        void showPromo(Promo promo);

        void updateSavedPromo();

        void updateDeletedPromo();

        void sharePromo(String promo, String video);


        void showLoading();

        void hideLoading();

        void showMessage(String message);

    }

    interface Presenter extends BasePresenter {

        void saveOrDeletePromoAsFavorite();

        void sharePromo();
    }
}