package com.example.slnn3r.wallettrackerv2.ui.login.loginpresenter

import android.content.Context
import android.content.Intent

interface LoginPresenterInterface {

    interface LoginViewPresenter {
        fun executeGoogleSignIn(mContext: Context, requestCode: Int, resultCode: Int, data: Intent)
        fun retrieveData(mContext: Context, userUid: String)
    }
}