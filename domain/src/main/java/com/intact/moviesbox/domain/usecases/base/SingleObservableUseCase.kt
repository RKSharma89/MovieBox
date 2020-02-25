package com.intact.moviesbox.domain.usecases.base

import io.reactivex.Scheduler
import io.reactivex.Single

abstract class SingleObservableUseCase<T, in Param> constructor(
    private val backgroundScheduler: Scheduler,
    private val foregroundScheduler: Scheduler
) {

    protected abstract fun generateObservable(params: Param? = null): Single<T>

    /**
     * function to generate the custom observable with params
     * @param params parameters to generate observable
     * subscribing on background scheduler
     * observing on foreground scheduler
     */
    fun buildUseCase(params: Param? = null) =
        generateObservable(params)
            .subscribeOn(backgroundScheduler)
            .observeOn(foregroundScheduler)
}