package org.devmaster.theweather.data.local

import android.arch.persistence.room.Room
import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.devmaster.theweather.TestUtil
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EntityReadAndWriteTest {

    private lateinit var mWeatherDao: WeatherDao
    private lateinit var mDatabase: AppDatabase


    @Before
    fun createDatabase() {
        val context: Context = InstrumentationRegistry.getTargetContext()
        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        mWeatherDao = mDatabase.weatherDao()
    }

    @After
    fun closeDatabase() {
        mDatabase.close()
    }

    @Test
    fun writeAndReadCurrentWeather() {
        // Given
        val id = 10L
        val currentWeather = TestUtil.createCurrentWeather(id)

        // When
        mWeatherDao.insert(currentWeather)
        val restoredCurrentWeather = mWeatherDao.getWeather().blockingGet()

        // Then
        assertThat(restoredCurrentWeather.id, equalTo(id))
    }

    @Test
    fun writeAndDeleteAllCurrentWeather() {
        // Given
        val id = 20L
        val currentWeather = TestUtil.createCurrentWeather(id)

        // When
        mWeatherDao.insert(currentWeather)
        mWeatherDao.deleteAll()

        // Then
        val restoredWeather = mWeatherDao.getWeather().blockingGet()
        assertNull(restoredWeather)
    }

}