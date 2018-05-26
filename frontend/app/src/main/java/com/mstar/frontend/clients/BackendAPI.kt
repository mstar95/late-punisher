package com.mstar.frontend.clients

import com.mstar.frontend.domain.Meeting
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface BackendAPI {

    @GET("/meetings/")
    fun meetings(): Observable<List<Meeting>>

    @POST("/meetings/")
    fun addMeeting(@Body meeting: Meeting): Observable<Meeting>
}