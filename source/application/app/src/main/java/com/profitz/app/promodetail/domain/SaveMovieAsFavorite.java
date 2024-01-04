package com.profitz.app.promodetail.domain;

import com.profitz.app.UseCase;import com.profitz.app.data.model.Promo;
import com.profitz.app.data.source.DataSource;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class SaveMovieAsFavorite extends UseCase<Boolean, SaveMovieAsFavorite.Params> {

    private final DataSource mRepository;

    public SaveMovieAsFavorite(DataSource repository,
                               Scheduler threadExecutor,
                               Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(Params params) {
        return mRepository.saveMovieAsFavorite(params.promo);
    }

    public static final class Params {

        private final Promo promo;

        private Params(Promo promo) {
            this.promo = promo;
        }

        public static Params forMovie(Promo promo) {
            return new Params(promo);
        }
    }
}