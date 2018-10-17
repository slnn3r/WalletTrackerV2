package com.example.slnn3r.wallettrackerv2.ui.login.loginmodel

import android.content.Context
import com.google.firebase.auth.FirebaseUser

interface LoginModelInterface {

    interface SharePreferenceAccess {
        fun saveAccToSharePrefExecute(mContext: Context, jsonUserProfile: String)
    }
}