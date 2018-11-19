package com.example.slnn3r.wallettrackerv2.ui.splash.splashview

import com.example.slnn3r.wallettrackerv2.base.BaseView

interface SplashViewInterface {

    interface SplashView : BaseView.Universal {
        fun navigateToLogin()
        fun navigateToDashboard()
    }
}