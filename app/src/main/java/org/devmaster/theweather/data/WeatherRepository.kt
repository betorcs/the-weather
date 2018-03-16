package org.devmaster.theweather.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.devmaster.theweather.data.local.WeatherDao
import org.devmaster.theweather.data.network.WeatherApi
import org.devmaster.theweather.model.CurrentWeather

class WeatherRepository(private val mWeatherApi: WeatherApi, private val mWeatherDao: WeatherDao) {

    fun getWeather(city: String): Observable<CurrentWeather> {
        return Observable.concatArray(
                getWeatherFromDb(),
                getWeatherFromApi(city)
        )
    }

    fun getWeather(lat: Double, log: Double): Observable<CurrentWeather> {
        return Observable.concatArray(
                getWeatherFromDb(),
                getWeatherFromApi(lat, log)
        )
    }

    private fun getWeatherFromDb(): Observable<CurrentWeather> {
        return mWeatherDao.getWeather()
                .toObservable()
    }

    private fun getWeatherFromApi(city: String): Observable<CurrentWeather> {
        return mWeatherApi.getWeather(city)
                .doOnNext { weather ->
                    storeWeathersInDb(weather)
                }
    }

    private fun getWeatherFromApi(lat: Double, log: Double): Observable<CurrentWeather> {
        return mWeatherApi.getWeather(lat, log)
                .doOnNext { weather ->
                    storeWeathersInDb(weather)
                }
    }

    private fun storeWeathersInDb(weather: CurrentWeather) {
        Observable.fromCallable {
            mWeatherDao.deleteAll()
            mWeatherDao.insert(weather)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe()
    }

}