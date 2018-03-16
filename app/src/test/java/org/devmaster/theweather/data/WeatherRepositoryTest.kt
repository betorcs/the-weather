package org.devmaster.theweather.data

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.devmaster.theweather.RxPluginHelper
import org.devmaster.theweather.data.local.WeatherDao
import org.devmaster.theweather.data.network.WeatherApi
import org.devmaster.theweather.model.CurrentWeather
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class WeatherRepositoryTest {

    @Mock
    private lateinit var mMockWeatherApi: WeatherApi

    @Mock
    private lateinit var mMockWeatherDao: WeatherDao

    private lateinit var mRepository: WeatherRepository


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mRepository = WeatherRepository(mMockWeatherApi, mMockWeatherDao)

        RxPluginHelper.reset()
    }

    @After
    fun tearDown() {
        RxPluginHelper.reset()
    }

    @Test
    fun getWeather_returnOnlyFromApi() {
        // Given
        val location = "bauru"

        // Expected
        val mockResultFromApi = Mockito.mock(CurrentWeather::class.java)

        `when`(mMockWeatherDao.getWeather()).thenReturn(Maybe.empty())
        `when`(mMockWeatherApi.getWeather(location)).thenReturn(Observable.just(mockResultFromApi))

        // When
        val testObserver = TestObserver<CurrentWeather>()
        mRepository.getWeather(location).subscribe(testObserver)

        // Then
        testObserver.assertNoErrors()
                .assertValueAt(0, mockResultFromApi)
                .assertComplete()
    }

    @Test
    fun getWeather_returnFromDbAndApi() {
        // Given
        val location = "bauru"

        // Expected
        val mockResultFromDb = Mockito.mock(CurrentWeather::class.java)
        val mockResultFromApi = Mockito.mock(CurrentWeather::class.java)

        `when`(mMockWeatherDao.getWeather()).thenReturn(Maybe.just(mockResultFromDb))
        `when`(mMockWeatherApi.getWeather(location)).thenReturn(Observable.just(mockResultFromApi))

        // When
        val testObserver = TestObserver<CurrentWeather>()
        mRepository.getWeather(location).subscribe(testObserver)

        // Then
        testObserver.assertNoErrors()
                .assertValueAt(0, mockResultFromDb)
                .assertValueAt(1, mockResultFromApi)
                .assertComplete()
    }

    @Test
    fun getWeather_returnFromDbAndApiThrowError() {
        // Given
        val location = "bauru"

        // Expected
        val mockResultFromDb = Mockito.mock(CurrentWeather::class.java)

        `when`(mMockWeatherDao.getWeather()).thenReturn(Maybe.just(mockResultFromDb))
        `when`(mMockWeatherApi.getWeather(location)).thenReturn(Observable.error(RuntimeException()))

        // When
        val testObserver = TestObserver<CurrentWeather>()
        mRepository.getWeather(location).subscribe(testObserver)

        // Then
        testObserver.assertValueAt(0, mockResultFromDb)
                .assertError(RuntimeException::class.java)
    }


}