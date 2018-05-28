package com.mstar.frontend.services

import android.util.Log
import com.mstar.frontend.clients.BackendClient
import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.domain.User
import com.twitter.sdk.android.core.TwitterCore
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object UserService {
    val users: MutableList<User> =  mutableListOf()
    val checkedUsers: MutableList<String> =  mutableListOf()
    var userId = "994543825170452480"
    private val obs = PublishSubject.create<List<User>>()
    private val checkedObs = PublishSubject.create<User>()

    init {
        obs.subscribe({ users.clear()
            users.addAll(it)})
        checkedObs.subscribe{ checkedUsers.add(it.id)}

    }

    fun logUser( user: User) {
        BackendClient.addUser(user).subscribe({Log.e("OK", it.toString())} ,
                { Log.e("ERR", it.toString())})
    }

    fun checkUser( user: User) {
        checkedObs.onNext(user)
    }

    fun getCheckedUsers(): PublishSubject<User>{
        return checkedObs
    }


    fun getUsers() : Observable<List<User>> {
        BackendClient.users().subscribe({obs.onNext(it)})
        return obs
    }

    fun findParticipantInMeeting(meeting: Meeting) =
            (meeting.participants + meeting.organizer).find { it.userId == UserService.userId }
}