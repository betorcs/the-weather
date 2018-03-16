package org.devmaster.theweather.usecase

import io.reactivex.Observable
import org.devmaster.theweather.DataHelper
import org.devmaster.theweather.RxPluginHelper
import org.devmaster.theweather.data.WeatherRepository
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@FixMethodOrder(MethodSorters.JVM)
class WeatherPresenterTest {

    private lateinit var mPresenter: WeatherPresenter

    @Mock
    private lateinit var mMockView: WeatherContract.View

    @Mock
    private lateinit var mMockRepository: WeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mPresenter = WeatherPresenter(mMockRepository, mMockView)

        RxPluginHelper.setup()
    }

    @After
    fun tearDown() {
        RxPluginHelper.reset()
    }

    @Test
    fun getWeatherSuccess() {
        // Given
        val lat = 22.0
        val lon = 64.0
        val expected = DataHelper.createCurrentWeather(23L)

        // Prepare
        `when`(mMockRepository.getWeather(lat, lon))
                .thenReturn(Observable.just(expected))

        // When
        mPresenter.getWeather(lat, lon)

        // Then
        verify(mMockView).setProgressIndicator(true)
        verify(mMockView).showWeathers(expected)
        verify(mMockView).setProgressIndicator(false)
    }


    @Test
    fun getWeatherFailure() {
        // Given
        val location = "ourinhos"

        // Prepare
        val error = RuntimeException()
        `when`(mMockRepository.getWeather(location))
                .thenReturn(Observable.error(error))

        // When
        mPresenter.getWeather(location)

        // Then
        verify(mMockView).setProgressIndicator(true)
        verify(mMockView).showErrorPlaceholder(error)
        verify(mMockView).setProgressIndicator(false)
    }

}