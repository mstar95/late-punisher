package com.mstar.frontend.domain

import android.os.Build
import android.support.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(" MM dd hh:mm")

data class Meeting(val id: Int,
                   val title: String,
                   val organizerId: Int,
                   val users: List<Int> = emptyList(),
                   val date: LocalDateTime,
                   val lat: LatLng,
                   val punishment: MutableList<Punishment>) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return "$title ${date.format(formatter)}"
    }
}

data class Punishment(val userId: Int,
                      val text: String)