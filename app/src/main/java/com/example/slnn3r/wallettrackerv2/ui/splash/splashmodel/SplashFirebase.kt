package com.example.slnn3r.wallettrackerv2.ui.splash.splashmodel

import com.google.firebase.auth.FirebaseAuth

class SplashFirebase : SplashModelInterface.FirebaseAccess {

    private lateinit var mAuth: FirebaseAuth

    override fun checkFirebaseSession(): String {

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        return if (currentUser != null) {
            currentUser.displayName!!
        } else {
            ""
        }
    }
}