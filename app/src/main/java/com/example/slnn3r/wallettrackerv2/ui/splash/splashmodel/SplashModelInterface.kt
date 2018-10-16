package com.example.slnn3r.wallettrackerv2.ui.splash.splashmodel

interface SplashModelInterface {

    interface FirebaseAccess {
        fun checkFirebaseSession(): String
    }
}