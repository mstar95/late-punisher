package com.mstar.frontend

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
                finish()
            }

            override fun failure(exception: TwitterException) {
                // Do something on failure
            }
        })
    }

    fun twit() {
        val twitterApiClient = TwitterCore.getInstance().apiClient
        val statusesService = twitterApiClient.statusesService
        val xd = statusesService.update("kek", null, null,
                null, null, null, null, null, null)
        xd.enqueue(object : Callback<Tweet>() {
            override fun success(result: Result<Tweet>) {
                Log.i("XD", result.data.text)
            }

            override fun failure(exception: TwitterException) {
                //Do something on failure
                Log.i("XD :(", "KEK :(")
            }
        })
    }
}
