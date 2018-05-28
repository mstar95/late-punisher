package com.mstar.frontend.services

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.mstar.frontend.clients.BackendClient
import com.mstar.frontend.domain.Meeting
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


object MeetingService {
    private val locObs = PublishSubject.create<LatLng>()
    private val meetingOb = PublishSubject.create<List<Meeting>>()
    val meetings = mutableListOf<Meeting>()

    init {
        meetingOb.subscribe({
            meetings.clear()
            meetings.addAll(it)
        })
    }

    fun addLoc(latLng: LatLng) {
        locObs.onNext(latLng)
    }

    fun getLoc(): Observable<LatLng> {
        return locObs
    }

    fun addMeeting(meeting: Meeting) {
        BackendClient.addMeeting(meeting)
                .subscribe({ req() },
                        { Log.e("ERR", it.toString()) })
    }

    fun updateMeeting(meeting: Meeting): Observable<Meeting> {
        val ob = BackendClient.updateMeeting(meeting)
        ob.subscribe({ req() },
                { Log.e("ERR", it.toString()) })
        return ob
    }

    fun getMeetings(): Observable<List<Meeting>> {
        req()
        return meetingOb
    }

    fun req() {
        BackendClient.meetings().subscribe({ meetingOb.onNext(it) },
                { Log.i("ERROR:", it.message) })
    }

    fun updateMeetings(meeting: List<Meeting>) {
        Observable.fromIterable(meeting.map(BackendClient::updateMeeting))
                //execute in parallel
                .flatMap{ task -> task.observeOn(Schedulers.io()) }
                //wait, until all task are executed
                //be aware, all your observable should emit onComplemete event
                //otherwise you will wait forever
                //could implement more intelligent logic. eg. check that everything is successful
                .subscribe{
                    Log.i("HOW MANNY", "HOW MANY")
                    req()
                }

    }

}