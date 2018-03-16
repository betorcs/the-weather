package org.devmaster.theweather.usecase

import org.devmaster.theweather.model.CurrentWeather


interface WeatherContract {

    interface View {
        fun showWeathers(weather: CurrentWeather)
        fun showErrorPlaceholder(error: Throwable)
        fun showProgressIndicator()
        fun hideProgressIndicator()
        fun hidePlaceholders()
    }

    interface Presenter {
        fun getWeather(lat: Double, log: Double)
        fun getWeather(location: String)
        fun onStop()
        fun refresh()
    }

}