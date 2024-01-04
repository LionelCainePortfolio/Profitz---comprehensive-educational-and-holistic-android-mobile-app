package com.profitz.app.promos.domain;

import com.profitz.app.UseCase;import com.profitz.app.data.model.Promo;
import com.profitz.app.data.source.DataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetTopRatedPromos extends UseCase<List<Promo>, Void> {

    private final DataSource mRepository;

    public GetTopRatedPromos(DataSource repository,
                             Scheduler threadExecutor,
                             Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<List<Promo>> buildUseCaseObservable(Void unused) {
        return mRepository.getTopRatedMovies();
    }
}