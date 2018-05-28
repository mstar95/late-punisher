package com.mstar.frontend.services

import android.location.Location
import android.os.Build
import android.support.annotation.RequiresApi
import com.mstar.frontend.domain.Meeting
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import android.location.LocationManager
import android.os.Message
import android.util.Log
import com.mstar.frontend.domain.Participant
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import kotlin.math.abs


object PunisherService {
    const val TIME_LAPSE = 20
    const val DISTANCE = 200
    @RequiresApi(Build.VERSION_CODES.O)
    fun performLocationChange(location: Location) {
        MeetingService.getMeetings().subscribe{ performMeetings(it, location)
        punish(it)}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun performMeetings(meetings: List<Meeting>, location: Location) {
        val now = LocalDateTime.now()
        val meetings = meetings.filter { filterMeeting(it, now) }
                .map { performMeeting(it, location) }
                .filter { it.second }
                .map { it.first }
        MeetingService.updateMeetings(meetings)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterMeeting(meeting: Meeting, now: LocalDateTime): Boolean {
        return abs(ChronoUnit.MINUTES.between(meeting.date, now)) <= TIME_LAPSE &&
                now.isBefore(meeting.date)
    }

    private fun performMeeting(meeting: Meeting, location: Location): Pair<Meeting, Boolean> {
        return if (meeting.organizer.userId == UserService.userId )
            performMeetingForOrganizer(meeting, location)
        else performMeetingForParticipant(meeting, location)

    }

    private fun performMeetingForOrganizer(meeting: Meeting, location: Location): Pair<Meeting, Boolean> {
         if (!meeting.organizer.arrived && checkLocation(meeting, location)) {
             meeting.organizer.arrived = true
             return Pair(meeting, true)
         }
        return Pair(meeting, false)
    }

    private fun performMeetingForParticipant(meeting: Meeting, location: Location): Pair<Meeting, Boolean> {
        val participant = meeting.participants.findLast { it.userId == UserService.userId }
        if (checkParticipant(participant) && checkLocation(meeting, location)) {
            participant?.arrived = true
            return Pair(meeting, true)
        }
        return Pair(meeting, false)
    }

    private fun checkParticipant(participant: Participant?) =
            (participant != null && participant.accepted
                    && !participant.arrived)

    private fun checkLocation(meeting: Meeting, location: Location): Boolean {
        val temp = Location(LocationManager.GPS_PROVIDER)
        temp.latitude = meeting.lat.latitude
        temp.longitude = meeting.lat.longitude
        val distance = location.distanceTo(temp)

        Log.i("DISTANCE", "$distance")

        return distance <= DISTANCE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun punish( meetings: List<Meeting>) {
        punishMeetings(meetings)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun punishMeetings(meetings: List<Meeting>) {
        val now = LocalDateTime.now()
        val meetings = meetings.filter { abs(ChronoUnit.MINUTES.between(it.date, now)) <= 2 &&
                now.isAfter(it.date) }
                .map { punishMeeting(it) }
                .filter { it.second }
                .map { it.first }
        MeetingService.updateMeetings(meetings)
    }

    private fun punishMeeting(meeting: Meeting): Pair<Meeting, Boolean> {
        val participant = UserService.findParticipantInMeeting(meeting)
        if(participant != null && checkParticipant(participant) && !participant.punished) {
            PUNISH_HIM(meeting)
            participant.punished = true
            return Pair(meeting, true)
        }
        return Pair(meeting, false)
    }

    private fun PUNISH_HIM(meeting: Meeting) {
        val punishment = meeting.punishment.shuffled()[0].text
        twit(punishment)
    }

    private fun twit(punishment :String) {
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val xd = statusesService.update(punishment, null, null,
                null, null, null, null, null, null)
        xd.enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>) {
                Log.i("punishment", result.data.text)
            }

            override fun failure(exception: TwitterException) {
                //Do something on failure
                Log.i("punishment :(", "KEK :(")
            }
        })
    }
}