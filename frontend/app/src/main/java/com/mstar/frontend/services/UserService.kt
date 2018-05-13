package com.mstar.frontend.services

import android.util.Log
import com.mstar.frontend.domain.User
import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.Subject

object UserService {
    private val users: MutableList<User> = mutableListOf()
    private val obs = PublishSubject.create<User>()

    fun addUser( user: User) {
        Log.i("Kurwa", "chuj")
        obs.onNext(user)
    }

    fun getUsers() : Observable<User> {
        return obs.asObservable()
    }
}