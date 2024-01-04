
package com.profitz.app.promos;

import com.profitz.app.base.BasePresenter;
import com.profitz.app.base.BaseView;
import com.profitz.app.data.model.Promo;

import java.util.List;

public interface PromosContract {

    interface View extends BaseView<Presenter> {

        void showPromos(List<Promo> promos, int nav);

        void showEmptyView(int nav);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter {

        void getPopularPromos();

        void getTopRatedPromos();

        void getFavoritePromos();

        void getReferFriend();
    }
}