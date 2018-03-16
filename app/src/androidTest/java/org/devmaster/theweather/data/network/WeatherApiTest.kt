package org.devmaster.theweather.data.network

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.devmaster.theweather.model.CurrentWeather
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

@RunWith(AndroidJUnit4::class)
class WeatherApiTest {

    @get:Rule
    private val server = MockWebServer()

    private lateinit var mWeatherApi: WeatherApi

    private lateinit var mContext: Context

    @Before
    fun setUp() {
        mContext = InstrumentationRegistry.getContext()

        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE

        val retrofit = Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        mWeatherApi = retrofit.create(WeatherApi::class.java)
    }


    @Test
    fun testSuccess200() {
        server.enqueue(MockResponse()
                .setBody(RestApiTestHelper.bodyResponseSuccess200(mContext)))

        // When
        val testObserver = TestObserver<CurrentWeather>()
        mWeatherApi.getWeather("any location").subscribe(testObserver)

        // Then
        testObserver.awaitTerminalEvent()
        testObserver.assertNoErrors()
                .assertValue { currentWeather ->
                    currentWeather.name == "Bauru"
                }
                .assertComplete()
    }

    @Test
    fun testFailedUnauthorized401() {
        server.enqueue(MockResponse()
                .setResponseCode(401)
                .setBody(RestApiTestHelper.bodyResponseUnauthorized401(mContext)))

        // When
        val testObserver = TestObserver<CurrentWeather>()
        mWeatherApi.getWeather("Any local").subscribe(testObserver)

        // Then
        testObserver.awaitTerminalEvent()
        testObserver.assertErrorMessage("HTTP 401 Client Error")
    }

    @Test
    fun testFailedNotFound404() {
        server.enqueue(MockResponse()
                .setResponseCode(404)
                .setBody(RestApiTestHelper.bodyResponseNotFound404(mContext)))

        // When
        val testObserver = TestObserver<CurrentWeather>()
        mWeatherApi.getWeather("Any local").subscribe(testObserver)

        // Then
        testObserver.awaitTerminalEvent()
        testObserver.assertErrorMessage("HTTP 404 Client Error")
    }

}