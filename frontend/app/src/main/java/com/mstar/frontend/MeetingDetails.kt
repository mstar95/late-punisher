package com.mstar.frontend

import android.app.DialogFragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.services.MeetingService
import com.mstar.frontend.services.UserService
import kotlinx.android.synthetic.main.activity_meeting_details.*


class MeetingDetails : AppCompatActivity(), OnMapReadyCallback {

    private var meeting: Meeting? = null
    private var usersList = UserService.users.toList()

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_details)
        val id: Int = intent.getStringExtra("meeting").toInt()
        meeting = MeetingService.meetings.first { it.id == id }
        titleD.text = meeting?.title
        address.text = "${meeting?.lat?.longitude} ${meeting?.lat?.longitude}"

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                usersList.filter{meeting?.users?.contains(it.id) ?: false})
        users.adapter = adapter

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        punishments.setOnClickListener{
            val dialog = PunishmentsDialogFragment()
            val args = Bundle()
            args.putInt("id", meeting!!.id)
            dialog.arguments = args
            dialog.show(supportFragmentManager , "NoticeDialogFragment")
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;
        val pos = meeting?.lat
        mMap.addMarker(MarkerOptions().position(pos!!).title(meeting?.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10F), 2000, null);
    }
}
