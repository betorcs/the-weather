package org.devmaster.theweather.data.network

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object RestApiTestHelper {

    private fun streamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val string = reader.lineSequence().joinToString(separator = "\n")
        reader.close()
        return string
    }

    private fun stringFromFile(ctx: Context, filePath: String): String {
        val stream = ctx.resources.assets.open(filePath)
        val string = streamToString(stream)
        stream.close()
        return string
    }

    fun bodyResponseSuccess200(ctx: Context): String {
        return stringFromFile(ctx, "weather_200_ok.json")
    }

    fun bodyResponseUnauthorized401(ctx: Context): String {
        return stringFromFile(ctx, "weather_401_unauthorized.json")
    }

    fun bodyResponseNotFound404(ctx: Context): String {
        return stringFromFile(ctx, "weather_404_not_found.json")
    }

}