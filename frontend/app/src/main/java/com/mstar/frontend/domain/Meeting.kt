package com.mstar.frontend.domain

import android.os.Build
import android.support.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(" MM dd hh:mm")

data class Meeting(val title: String,
                   val organizer: Participant,
                   var participants: List<Participant> = emptyList(),
                   val date: LocalDateTime,
                   val placeName: String,
                   val lat: LatLng,
                   val punishment: MutableList<Punishment>,
                   val _id: String? = null) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return "$title ${date.format(formatter)}"
    }
}

data class Punishment(val userId: String,
                      val text: String)

data class Participant(val userId: String,
                       var accepted: Boolean = false,
                       var arrived: Boolean = false,
                       var punished: Boolean = false)