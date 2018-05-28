package com.mstar.frontend

import android.app.Activity
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
import android.support.v4.app.ShareCompat.IntentBuilder
import com.google.android.gms.location.places.ui.PlacePicker
import android.widget.Toast
import com.google.android.gms.location.places.Place
import com.mstar.frontend.domain.Participant


class CreateMeetingActivity : AppCompatActivity() {

    var place: Place? = null
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private val PLACE_PICKER_REQUEST = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meeting)

        UserService.checkedUsers.clear()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<User>());
        usersLabel.adapter = adapter

        eventDate.text.append(LocalDateTime.now().plusHours(1).format(format).toString())
        userButton.setOnClickListener {
            val userIntent = Intent(this, AddUserActivity::class.java)
            startActivity(userIntent)
        }

        createButton.setOnClickListener {
            Log.i("new meeting", titleL.text.toString())
            MeetingService.addMeeting(Meeting(titleL.text.toString(),
                    Participant(UserService.userId, true),
                    UserService.checkedUsers.toList().map{ Participant(it) },
                    LocalDateTime.parse(eventDate.text, format),
                    place!!.address.toString(), place!!.latLng,
                    mutableListOf(Punishment(UserService.userId, punishment.text.toString()))))
            UserService.checkedUsers.clear()
            finish()
        }

        UserService.getCheckedUsers().subscribe({
            Log.i("check user", it.toString())
            adapter.add(it)
            adapter.notifyDataSetChanged()

        })


        mapButton.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        println("onActivityResult")
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                place = PlacePicker.getPlace(data, this)
                address.text = place?.address ?: ""
            }
        }
    }
}
