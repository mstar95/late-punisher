package com.mstar.frontend

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.domain.Participant
import com.mstar.frontend.domain.User
import com.mstar.frontend.domain.formatter
import com.mstar.frontend.services.MeetingService
import com.mstar.frontend.services.UserService
import kotlinx.android.synthetic.main.activity_meeting_details.*


class MeetingDetails : AppCompatActivity(), OnMapReadyCallback {

    private var meeting: Meeting? = null
    private var usersList = UserService.users.toList()

    private lateinit var mMap: GoogleMap

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_details)
        val id = intent.getStringExtra("meeting")
        meeting = MeetingService.meetings.first { it._id == id }
        titleD.text = "meeting: " + meeting?.title
        address.text = "address: " + meeting?.placeName
        date.text = "date: " + meeting?.date?.format(formatter)
        organizerTxt.text = UserService.users.find { it.id == meeting?.organizer?.userId }.toString()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                usersList.filter {
                    meeting?.participants?.map { it.userId }?.contains(it.id) ?: false
                }
                        .map { prepareUserName(it) }
        )

        users.adapter = adapter

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        punishments.setOnClickListener {
            val dialog = PunishmentsDialogFragment()
            val args = Bundle()
            args.putString("_id", meeting!!._id)
            dialog.arguments = args
            dialog.show(supportFragmentManager, "NoticeDialogFragment")
        }

        accept.setOnClickListener {
            meeting?.participants = meeting?.participants!!
                    .map {
                        if (it.userId == UserService.userId)
                            Participant(it.userId, true, it.arrived)
                        else it
                    }
            MeetingService.updateMeeting(meeting!!).subscribe{this.recreate()}
        }

        if (meeting?.participants?.findLast { it.userId == UserService.userId }?.accepted != false) {
            accept.visibility = View.GONE
        }

    }

    private fun prepareUserName(user: User): String {
        var accumulator = if (user.id == UserService.userId) user.name + " (You)" else user.name
        return if (meeting?.participants?.find { it.userId == user.id }?.accepted == true)
            "$accumulator:  accepted"
        else accumulator + ":  invited"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;
        val pos = meeting?.lat
        mMap.addMarker(MarkerOptions().position(pos!!).title(meeting?.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10F), 2000, null);
    }
}
