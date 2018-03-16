package org.devmaster.theweather.usecase

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.devmaster.theweather.data.WeatherRepository
import org.devmaster.theweather.model.CurrentWeather


class WeatherPresenter(private val mRepository: WeatherRepository,
                       private val mView: WeatherContract.View)
    : WeatherContract.Presenter {

    private var mDisposable: Disposable? = null
    private var mLastFlowable: Flowable<CurrentWeather>? = null

    override fun getWeather(lat: Double, log: Double) {
        observe(mRepository.getWeather(lat, log))
    }

    override fun getWeather(location: String) {
        observe(mRepository.getWeather(location))
    }

    override fun refresh() {
        mLastFlowable?.let { flowable ->
            observe(flowable)
        }
    }

    private fun observe(flowable: Flowable<CurrentWeather>) {

        mLastFlowable = flowable

        flowable
                .compose(applySchedulers())
                .compose(updateView())
                .subscribe()
    }

    private fun applySchedulers(): (Flowable<CurrentWeather>) -> Flowable<CurrentWeather> {
        return { flowable ->
            flowable
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun updateView(): (Flowable<CurrentWeather>) -> Flowable<CurrentWeather> {
        return { flowable ->
            flowable
                    .doOnSubscribe({ run { mView.showProgressIndicator() } })
                    .doOnNext(mView::showWeathers)
                    .doOnError(mView::showErrorPlaceholder)
                    .doAfterTerminate(mView::hideProgressIndicator)
        }
    }


    override fun onStop() {
        mDisposable?.dispose()
    }

}