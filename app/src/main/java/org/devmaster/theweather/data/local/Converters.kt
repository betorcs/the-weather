package org.devmaster.theweather.data.local

import android.arch.persistence.room.TypeConverter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.devmaster.theweather.model.Weather

object Converters {

    @JvmStatic
    @TypeConverter
    fun fromWeather(value: String): ArrayList<Weather> {
        val typedValue = object : TypeReference<List<Weather>>() {}
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(value, typedValue)
    }

    @JvmStatic
    @TypeConverter
    fun fromArrayList(values: ArrayList<Weather>): String {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.writeValueAsString(values)
    }

}