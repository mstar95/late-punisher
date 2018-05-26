package com.mstar.frontend.services

import com.mstar.frontend.domain.User
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object UserService {
    val users: MutableList<User> =  mutableListOf(User("XD", 0),
                User("Kek", 1))
    private val obs = PublishSubject.create<User>()

    fun addUser( user: User) {
        obs.onNext(user)
    }


    fun getUsers() : Observable<User> {
        return obs
    }
}