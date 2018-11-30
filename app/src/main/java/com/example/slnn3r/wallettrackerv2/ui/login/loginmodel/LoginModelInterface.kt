package com.example.slnn3r.wallettrackerv2.ui.login.loginmodel

import android.content.Context

interface LoginModelInterface {

    interface LoginViewModel {
        fun retrieveDataFirebase(mContext: Context, userUid: String)
    }
}