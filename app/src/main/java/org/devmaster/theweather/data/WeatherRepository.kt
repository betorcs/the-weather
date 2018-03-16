package org.devmaster.theweather.data

import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import org.devmaster.theweather.data.local.WeatherDao
import org.devmaster.theweather.data.network.WeatherApi
import org.devmaster.theweather.model.CurrentWeather

class WeatherRepository(private val mWeatherApi: WeatherApi, private val mWeatherDao: WeatherDao) {

    fun getWeather(city: String): Flowable<CurrentWeather> {
        return Maybe.concat(
                getWeatherFromDb(),
                getWeatherFromApi(city)
        )
    }

    fun getWeather(lat: Double, log: Double): Flowable<CurrentWeather> {
        return Maybe.concat(
                getWeatherFromDb(),
                getWeatherFromApi(lat, log)
        )
    }

    private fun getWeatherFromDb(): Maybe<CurrentWeather> {
        return mWeatherDao.getWeather()
    }

    private fun getWeatherFromApi(city: String): Maybe<CurrentWeather> {
        return mWeatherApi.getWeather(city)
                .doOnSuccess { weather ->
                    storeWeathersInDb(weather)
                }
    }

    private fun getWeatherFromApi(lat: Double, log: Double): Maybe<CurrentWeather> {
        return mWeatherApi.getWeather(lat, log)
                .doOnSuccess { weather ->
                    storeWeathersInDb(weather)
                }
    }

    private fun storeWeathersInDb(weather: CurrentWeather) {
        Completable.fromCallable {
            mWeatherDao.deleteAll()
            mWeatherDao.insert(weather)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe()
    }

}