package com.mstar.frontend.clients

import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.domain.User
import io.reactivex.Observable
import retrofit2.http.*


interface BackendAPI {

    @GET("/meetings/")
    fun meetings(): Observable<List<Meeting>>

    @GET("/users/{id}/meetings/")
    fun meetingsByUser(@Path("id") id: String): Observable<List<Meeting>>

    @POST("/meetings/")
    fun addMeeting(@Body meeting: Meeting): Observable<Meeting>

    @PUT("/meetings/")
    fun updateMeeting(@Body meeting: Meeting): Observable<Meeting>

    @POST("/users/")
    fun addUser(@Body user: User): Observable<User>

    @GET("/users/")
    fun users():  Observable<List<User>>
}