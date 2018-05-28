package com.mstar.frontend

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mstar.frontend.domain.User
import com.mstar.frontend.services.UserService
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.activity_twitter_login.*


class TwitterLoginActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        loginButton.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Twitter.initialize(this)
        setContentView(R.layout.activity_twitter_login)
        loginButton.setCallback(object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                logUserOnBackend(result.data)
                finish()
            }

            override fun failure(exception: TwitterException) {
                // Do something on failure
            }
        })
    }

    fun logUserOnBackend(session: TwitterSession) {
        Log.i("login", session.userName + " " + session.userId)
        UserService.userId = session.userId.toString()
        UserService.logUser(User( session.userName, session.userId.toString()))
        UserService.getUsers()
    }
}
