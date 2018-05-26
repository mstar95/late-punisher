package com.mstar.frontend.clients

import android.util.Log
import com.google.gson.GsonBuilder
import com.mstar.frontend.domain.Meeting
import io.reactivex.Observable
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

    fun addMeeting(meeting: Meeting) {
        Log.i("add", meeting.toString())
        backendAPI.addMeeting(meeting)
                .subscribeOn(Schedulers.io())
                .subscribe({Log.i("OK", "OK")},
                { Log.e("OK", it.toString())})
    }

    fun meetings(): Observable<List<Meeting>> {
        Log.i("load", "meetingss")
        return backendAPI.meetings()
                .subscribeOn(Schedulers.io())
    }
}