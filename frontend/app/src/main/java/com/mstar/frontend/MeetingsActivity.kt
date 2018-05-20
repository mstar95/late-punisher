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
import com.google.android.gms.maps.model.LatLng
import com.mstar.frontend.domain.Meeting
import com.mstar.frontend.domain.Punishment
import com.mstar.frontend.services.MeetingService
import kotlinx.android.synthetic.main.activity_meetings.*
import kotlinx.android.synthetic.main.content_meetings.*
import java.time.LocalDateTime


class MeetingsActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1337)
        setContentView(R.layout.activity_meetings)

        //   if (AccessToken.getCurrentAccessToken() == null) {
        val loginIntent = Intent(this, TwitterLoginActivity::class.java)
        startActivity(loginIntent)
        //   }

        setSupportActionBar(toolbar)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<Meeting>());
        meetingsList.adapter = adapter

        meetingsList.setOnItemClickListener { parent, view, position, id ->
            val meeting = parent.getItemAtPosition(position) as Meeting
            val intent = Intent(baseContext, MeetingDetails::class.java)
            Log.i("XD", meeting.id.toString())
            intent.putExtra("meeting", meeting.id.toString())
            startActivity(intent)
        }

        fab.setOnClickListener {
            val createIntent = Intent(this, CreateMeetingActivity::class.java)
            startActivity(createIntent)
        }
        MeetingService.getMeetings().subscribe {
            adapter.add(it)
            adapter.notifyDataSetChanged()
        }
        meetings().forEach { MeetingService.addMeeting(it) }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun meetings(): List<Meeting> {
        return mutableListOf(Meeting(0, "XD", 0, listOf(0,1, 2, 3), LocalDateTime.now(), LatLng(0.0, 0.0),
                mutableListOf(Punishment(0, "Kara kara"))),
                Meeting(0, "Kek", 0, listOf(1), LocalDateTime.now().minusDays(1), LatLng(0.0, 0.0),
                        mutableListOf(Punishment(0, "Kara kara"))))
    }
}
