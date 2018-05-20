package com.mstar.frontend.services

import com.google.android.gms.maps.model.LatLng
import com.mstar.frontend.domain.Meeting
import rx.Observable
import rx.subjects.PublishSubject

object MeetingService {
    private val locObs = PublishSubject.create<LatLng>()
    private val meetingOb = PublishSubject.create<Meeting>()
    val meetings = mutableListOf<Meeting>()

    fun addLoc(latLng: LatLng) {
        locObs.onNext(latLng)
    }

    fun getLoc(): Observable<LatLng> {
        return locObs.asObservable()
    }

    fun addMeeting(meeting: Meeting) {
        meetings.add(meeting)
        meetingOb.onNext(meeting)
    }

    fun getMeetings(): Observable<Meeting> {
        return meetingOb.asObservable()
    }
}