package com.mstar.frontend

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import com.mstar.frontend.domain.User
import com.mstar.frontend.services.UserService
import kotlinx.android.synthetic.main.activity_create_meeting.*

class CreateMeetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meeting)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<User>());
        users.adapter = adapter
        userButton.setOnClickListener {
            val userIntent = Intent(this, AddUserActivity::class.java)
            startActivity(userIntent)
        }

        mapButton.setOnClickListener {
            val userIntent = Intent(this, SetMeetingPlaceActivity::class.java)
            startActivity(userIntent)
        }

        UserService.getUsers().subscribe({
            Log.i("XD", it.toString())
            adapter.add(it)

            Log.i("XD", adapter.count.toString())

            adapter.notifyDataSetChanged()
        })
    }
}
