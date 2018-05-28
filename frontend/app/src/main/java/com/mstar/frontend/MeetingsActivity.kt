package com.mstar.frontend

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.services.GeoLocationService
import com.mstar.frontend.services.MeetingService
import com.mstar.frontend.services.PunisherService
import com.mstar.frontend.services.UserService
import kotlinx.android.synthetic.main.activity_meetings.*
import kotlinx.android.synthetic.main.content_meetings.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.abs

const val TIME_LAPSE = 20
const val TIME_LAPSE2 = 60

class MeetingsActivity : AppCompatActivity() {
    var meetings: List<Meeting> = listOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1337)
        setContentView(R.layout.activity_meetings)

        UserService.getUsers().subscribe()
        startService(Intent(this, GeoLocationService::class.java))
        //   if (AccessToken.getCurrentAccessToken() == null) {
        val loginIntent = Intent(this, TwitterLoginActivity::class.java)
        startActivity(loginIntent)
        //   }


        setSupportActionBar(toolbar)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>());

        meetingsList.adapter = adapter

        meetingsList.setOnItemClickListener { parent, view, position, id ->
            val meeting = meetings[position]
            val intent = Intent(baseContext, MeetingDetails::class.java)
            Log.i("XD", meeting._id.toString())
            intent.putExtra("meeting", meeting._id.toString())
            startActivity(intent)
        }

        fab.setOnClickListener {
            val createIntent = Intent(this, CreateMeetingActivity::class.java)
            startActivity(createIntent)
        }

        fab2.setOnClickListener {
           MeetingService.req()
        }
        MeetingService.getMeetings().subscribe {
            meetings = it.toList()
            adapter.clear()
            adapter.addAll(it.map {
                val now = LocalDateTime.now()
                prepareMeetingText(it, now)
            })
            adapter.notifyDataSetChanged()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareMeetingText(meeting: Meeting, now: LocalDateTime): String {
        var accumulator = meeting.toString()
        val user = UserService.findParticipantInMeeting(meeting)
        Log.i("MINUTES", ChronoUnit.MINUTES.between(meeting.date, now).toString())
        if (abs(ChronoUnit.MINUTES.between(meeting.date, now)) <= TIME_LAPSE
                && now.isBefore(meeting.date)) {
            accumulator += " starts soon"
            if (user?.accepted == true) {
                if (user.arrived) {
                    accumulator += ", u arrived"
                } else {
                    accumulator += ", hurry!!!"
                }
            }
        } else if (now.isBefore(meeting.date)) {
            accumulator += if (user?.accepted == true) {
                ", u accepted"
            } else {
                ", u not accepted"
            }
        } else if (abs(ChronoUnit.MINUTES.between(meeting.date, now)) <= TIME_LAPSE2
                && now.isAfter(meeting.date)) {
            accumulator += " event started"
            if(user?.arrived == true) {
                accumulator += ", u arrived"
            } else {
                if(user?.punished == true) {
                    accumulator += ", u are late, u was punished, check twitter wall :D"
                }
            }

        } else {
            accumulator += ", ended"
        }
        return accumulator
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_meetings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
