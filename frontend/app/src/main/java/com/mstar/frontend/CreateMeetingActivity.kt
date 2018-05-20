package com.mstar.frontend

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.domain.Punishment
import com.mstar.frontend.domain.User
import com.mstar.frontend.services.MeetingService
import com.mstar.frontend.services.UserService
import kotlinx.android.synthetic.main.activity_create_meeting.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateMeetingActivity : AppCompatActivity() {

    var addressVal: LatLng? = null
    val usersList = mutableListOf<Int>()
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meeting)


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<User>());
        usersLabel.adapter = adapter

        eventDate.text.append( LocalDateTime.now().plusHours(1).format(format).toString())
        userButton.setOnClickListener {
            val userIntent = Intent(this, AddUserActivity::class.java)
            startActivity(userIntent)
        }

        mapButton.setOnClickListener {
            val userIntent = Intent(this, SetMeetingPlaceActivity::class.java)
            startActivity(userIntent)
        }

        createButton.setOnClickListener {
            Log.i("XD", titleL.text.toString())
            MeetingService.addMeeting(Meeting(3,titleL.text.toString(), 0, usersList,
                    LocalDateTime.parse(eventDate.text, format), addressVal!!,
                    mutableListOf(Punishment(0, punishment.text.toString()))))
            finish()
        }

        UserService.getUsers().subscribe({
            adapter.add(it)
            adapter.notifyDataSetChanged()
            usersList.add(it.id)
        })

        MeetingService.getLoc().subscribe {
            address.text =  "address: ${it.latitude} + ${it.longitude}"
            addressVal = it
        }
    }
}
