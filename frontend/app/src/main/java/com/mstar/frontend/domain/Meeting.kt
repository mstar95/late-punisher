package com.mstar.frontend.domain

import android.os.Build
import android.support.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(" MM dd hh:mm")

data class Meeting(val title: String,
                   val organizerId: Int,
                   val users: List<Int> = emptyList(),
                   val date: LocalDateTime,
                   val lat: Int,
                   val lng: Int) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return "$title ${date.format(formatter)}"
    }
}