package com.example.slnn3r.wallettrackerv2.ui.splash.splashpresenter

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.splash.splashmodel.SplashFirebase
import com.example.slnn3r.wallettrackerv2.ui.splash.splashmodel.SplashModelInterface
import com.example.slnn3r.wallettrackerv2.ui.splash.splashview.SplashViewInterface
import io.fabric.sdk.android.Fabric

class SplashPresenter : SplashPresenterInterface.SplashPresenter,
        BasePresenter<SplashViewInterface.SplashView>() {

    private val mFirebaseModel: SplashModelInterface.FirebaseAccess = SplashFirebase()

    override fun launchCrashlytics(mContext: Context) {

        try {
            val fabric = Fabric.Builder(mContext)
                    .kits(Crashlytics())
                    .debuggable(true)  // Enables Crashlytics debugger
                    .build()
            Fabric.with(fabric)

        } catch (error: Exception) {
            getView()!!.onError(error.toString())
        }
    }

    override fun loadSession() {

        try {
            val sessionUserName = mFirebaseModel.checkFirebaseSession()

            if (sessionUserName.isEmpty()) {
                getView()!!.navigateToLogin()
            } else {
                getView()!!.navigateToDashboard(sessionUserName)
            }

        } catch (error: Exception) {
            getView()!!.onError(error.toString())
        }
    }
}