package com.example.slnn3r.wallettrackerv2.ui.splash.splashview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginActivity
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.ui.splash.splashpresenter.SplashViewPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog

class SplashActivity : AppCompatActivity(), SplashViewInterface.SplashView {

    private val mSplashPresenter: SplashViewPresenter = SplashViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        mSplashPresenter.bindView(this)
        mSplashPresenter.launchCrashlytics(this)
        mSplashPresenter.loadSession()
    }

    override fun onStop() {
        super.onStop()
        mSplashPresenter.unbindView() // Unbind the view when it stopped
    }

    override fun navigateToLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigateToDashboard() {
        val intent = Intent(applicationContext, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.SPLASH_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(this, message).show()
        return
    }
}
