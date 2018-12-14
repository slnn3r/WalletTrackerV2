package com.example.slnn3r.wallettrackerv2.ui.splash.splashpresenter

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.splash.splashview.SplashViewInterface
import io.fabric.sdk.android.Fabric

class SplashViewPresenter : SplashPresenterInterface.SplashViewPresenter,
        BasePresenter<SplashViewInterface.SplashView>() {

    override fun launchCrashlytics(mContext: Context) {
        try {
            val fabric = Fabric.Builder(mContext)
                    .kits(Crashlytics())
                    .debuggable(true)  // Enables Crashlytics debugger
                    .build()
            Fabric.with(fabric)

        } catch (error: Exception) {
            getView()?.onError(error.toString())
        }
    }

    override fun loadSession() {
        try {
            val signedInUser = getSignedInUser()

            if (signedInUser == null) {
                getView()?.navigateToLogin()
            } else {
                getView()?.navigateToDashboard()
            }

        } catch (error: Exception) {
            getView()?.onError(error.toString())
        }
    }
}