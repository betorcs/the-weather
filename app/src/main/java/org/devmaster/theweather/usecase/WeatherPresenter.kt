package org.devmaster.theweather.usecase

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.devmaster.theweather.data.WeatherRepository
import org.devmaster.theweather.model.CurrentWeather


class WeatherPresenter(private val mRepository: WeatherRepository,
                       private val mView: WeatherContract.View)
    : WeatherContract.Presenter {

    private var mDisposable: Disposable? = null
    private var mObservable: Observable<CurrentWeather>? = null

    override fun getWeather(lat: Double, log: Double) = observe(mRepository.getWeather(lat, log))

    override fun getWeather(location: String) = observe(mRepository.getWeather(location))

    override fun refresh() {
        mObservable?.let { observe(it) }
    }

    private fun observe(observable: Observable<CurrentWeather>) {
        mObservable = observable

        mView.setProgressIndicator(true)
        mDisposable = observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weather ->
                    mView.showWeathers(weather)
                }, { error ->
                    mView.showErrorPlaceholder(error)
                    mView.setProgressIndicator(false)
                }, {
                    mView.setProgressIndicator(false)
                })
    }

    override fun onStop() {
        mDisposable?.dispose()
    }

}