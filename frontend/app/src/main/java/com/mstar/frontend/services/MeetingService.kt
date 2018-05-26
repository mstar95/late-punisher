package com.mstar.frontend.services

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.mstar.frontend.clients.BackendClient
import com.mstar.frontend.domain.Meeting
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


object MeetingService {
    private val locObs = PublishSubject.create<LatLng>()
    private val meetingOb = PublishSubject.create<Meeting>()
    val meetings = mutableListOf<Meeting>()

    fun addLoc(latLng: LatLng) {
        locObs.onNext(latLng)
    }

    fun getLoc(): Observable<LatLng> {
        return locObs
    }

    fun addMeeting(meeting: Meeting) {
     //   BackendClient.addMeeting(meeting)
        meetings.add(meeting)
        meetingOb.onNext(meeting)
    }

    fun getMeetings(): Observable<Meeting> {
        req()
        return meetingOb
    }

    fun req() {
        BackendClient.meetings().subscribe({ Log.i("MEETINGS", it.toString())},
                { Log.i("ERROR:", it.message)})
    }
}