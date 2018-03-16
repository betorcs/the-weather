package org.devmaster.theweather.data.network

import io.reactivex.Observable
import org.devmaster.theweather.model.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double): Observable<CurrentWeather>

    @GET("weather")
    fun getWeather(@Query("q") location: String): Observable<CurrentWeather>

}