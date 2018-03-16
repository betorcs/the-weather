package org.devmaster.theweather.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore

data class Sys(

        @ColumnInfo(name = "sys_type")
        var type: Int,

        @ColumnInfo(name = "sys_id")
        var id: Long,

        @ColumnInfo(name = "sys_message")
        var message: String,

        @ColumnInfo(name = "sys_country")
        var country: String,

        @ColumnInfo(name = "sys_sunrise")
        var sunrise: Long,

        @ColumnInfo(name = "sys_sunsut")
        var sunset: Long)