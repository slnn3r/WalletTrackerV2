package com.example.slnn3r.wallettrackerv2.ui.splash.splashpresenter

import android.content.Context

interface SplashPresenterInterface {

    interface SplashPresenter {
        fun launchCrashlytics(mContext: Context)
        fun loadSession()
    }
}