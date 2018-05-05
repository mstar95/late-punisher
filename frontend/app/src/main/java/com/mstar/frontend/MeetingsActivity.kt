package com.mstar.frontend

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_meetings.*
import kotlinx.android.synthetic.main.content_meetings.*
import android.content.Intent
import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphResponse
import com.facebook.GraphRequest
import org.json.JSONObject







class MeetingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meetings)

     //   if (AccessToken.getCurrentAccessToken() == null) {
            val loginIntent = Intent(this, FacebookLoginActivity::class.java)
            startActivity(loginIntent)
     //   }

        setSupportActionBar(toolbar)
        val listItems = arrayOf("a","b")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        meetingsList.adapter = adapter
        fab.setOnClickListener { fetchEvents()
        }
    }

    private fun fetchEvents() {
        val jsonObject = JSONObject()
        jsonObject.put("message" ,"xd")
        val request2 = GraphRequest.newPostRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",jsonObject
        ) {
            Log.i("MainActivity", it.toString())
        }
        val request = GraphRequest.newGraphPathRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions"
        ) {
            Log.i("MainActivity", it.toString())
        }

        request.executeAsync()
        request2.executeAsync()
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
