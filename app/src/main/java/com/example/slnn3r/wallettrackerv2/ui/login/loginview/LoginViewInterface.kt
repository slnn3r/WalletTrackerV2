package com.example.slnn3r.wallettrackerv2.ui.login.loginview

import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.google.firebase.auth.FirebaseUser

interface LoginViewInterface {

    interface LoginView : BaseView {

        fun showSignInLoading()
        fun dismissSignInLoading()
        fun signInSuccess(userFirebase: FirebaseUser)
        fun saveAccToSharePrefSuccess(displayName: String)
    }
}