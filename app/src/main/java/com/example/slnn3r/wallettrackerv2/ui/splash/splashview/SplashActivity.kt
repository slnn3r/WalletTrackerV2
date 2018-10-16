package com.example.slnn3r.wallettrackerv2.ui.splash.splashview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.LogConstant
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginActivity
import com.example.slnn3r.wallettrackerv2.ui.splash.splashpresenter.SplashPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog

class SplashActivity : AppCompatActivity(), SplashViewInterface.SplashView {

    private var mPresenter: SplashPresenter = SplashPresenter()
    private val mCustomAlertDialog: CustomAlertDialog = CustomAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.bindView(this)
        mPresenter.launchCrashlytics(this)
        mPresenter.loadSession()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.unbindView() // Unbind the view when it stopped
    }

    override fun navigateToLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigateToDashboard(userName: String) {

    }

    override fun onError(message: String) {
        Log.e(LogConstant.splashLogging, message)
        mCustomAlertDialog.errorMessageDialog(this, message).show()
    }
}
