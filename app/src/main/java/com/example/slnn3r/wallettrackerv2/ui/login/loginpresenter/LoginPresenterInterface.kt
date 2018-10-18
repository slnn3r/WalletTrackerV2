package com.example.slnn3r.wallettrackerv2.ui.login.loginpresenter

import android.content.Context
import android.content.Intent

interface LoginPresenterInterface {

    interface LoginPresenter {
        fun executeGoogleLogin(mContext: Context, requestCode: Int, resultCode: Int, data: Intent)
    }
}