package com.mstar.frontend

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.mstar.frontend.domain.User
import com.mstar.frontend.services.UserService
import kotlinx.android.synthetic.main.activity_add_user.*
import kotlinx.android.synthetic.main.content_add_user.*

class AddUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        setSupportActionBar(toolbar)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getUsers());
        usersToAdd.adapter = adapter
        usersToAdd.setOnItemClickListener { parent, view, position, id ->
            val itemAtPosition = parent.getItemAtPosition(position) as User
            UserService.checkUser(itemAtPosition)
            finish()

        }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private fun getUsers() = UserService.users.filter {
                !UserService.checkedUsers.map { it }.contains(it.id)
            }.filter{it.id != UserService.userId }

}
