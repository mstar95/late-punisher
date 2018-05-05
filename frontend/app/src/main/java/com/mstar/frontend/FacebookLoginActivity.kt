package com.mstar.frontend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_facebook_login.*


class FacebookLoginActivity : AppCompatActivity() {

    private var mCallbackManager: CallbackManager? = null
    private var publishPermissionsRequested = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)

        mCallbackManager = CallbackManager.Factory.create();
        //loginButton.setPublishPermissions(arrayListOf("publish_actions"));
        LoginManager.getInstance().registerCallback(
                mCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.i("MainActivity", "Facebook token: " + loginResult.accessToken.token)
                        if (!publishPermissionsRequested) {
                            getPublishPermissions()
                        } else {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }

                    override fun onCancel() {
                        setResult(Activity.RESULT_CANCELED)
                        Log.i("MainActivity", "Facebook onCancel.")
                        finish()
                    }

                    override fun onError(e: FacebookException) {
                        Log.i("MainActivity", "Facebook onError.")
                    }
                }
        )
    }

    fun getPublishPermissions() {
        publishPermissionsRequested = true
        LoginManager.getInstance().logInWithPublishPermissions(this, arrayListOf("publish_pages"))
    }
}
