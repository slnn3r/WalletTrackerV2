package com.example.slnn3r.wallettrackerv2.ui.login.loginview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackerv2.R

class LoginActivity : AppCompatActivity(), LoginViewInterface.LoginView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
