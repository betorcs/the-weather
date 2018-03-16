package org.devmaster.theweather.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.devmaster.theweather.BuildConfig
import org.devmaster.theweather.data.network.WeatherApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*

@Module
class NetworkModule {

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()

                    val httpUrl = originalRequest.url().newBuilder()
                            .addQueryParameter("lang", Locale.getDefault().isO3Language)
                            .addQueryParameter("units", "metric")
                            .addQueryParameter("appid", BuildConfig.WEATHER_APP_ID)
                            .build()

                    val request = originalRequest.newBuilder()
                            .url(httpUrl)
                            .build()

                    chain.proceed(request)
                }
                .build()
    }

    @Provides
    fun providesObjectMapper(): ObjectMapper {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        return objectMapper
    }


    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }


    @Provides
    fun providesWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}