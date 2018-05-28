package com.mstar.frontend.clients

import android.util.Log
import com.google.gson.GsonBuilder
import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.domain.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object BackendClient {

    private const val URL = "http://10.0.2.2:3000"

    var gson = GsonBuilder()
            .setLenient()
            .create()

    var retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    var backendAPI = retrofit.create(BackendAPI::class.java)

    fun addMeeting(meeting: Meeting): Observable<Meeting> {
        Log.i("add", meeting.toString())
        return backendAPI.addMeeting(meeting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun updateMeeting(meeting: Meeting): Observable<Meeting> {
        Log.i("update", meeting.toString())
        return backendAPI.updateMeeting(meeting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun meetings(): Observable<List<Meeting>> {
        Log.i("load", "meetingss")
        return backendAPI.meetings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun meetingsByUser(userId: String): Observable<List<Meeting>> {
        Log.i("load", "meetings")
        return backendAPI.meetingsByUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun addUser(user: User): Observable<User> {
        Log.i("add", user.toString())
        return backendAPI.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun users(): Observable<List<User>> {
        Log.i("load", "users")
        return backendAPI.users()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}
