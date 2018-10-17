package com.example.slnn3r.wallettrackerv2.ui.login.loginmodel

import android.content.Context
import android.support.v7.app.AppCompatActivity

class LoginSharePreference: LoginModelInterface.SharePreferenceAccess{

    override fun saveAccToSharePrefExecute(mContext: Context, jsonUserProfile: String) {

        /*
        val editor = mContext.getSharedPreferences("UserProfileKey", AppCompatActivity.MODE_PRIVATE)!!.edit()
        editor.putString("UserProfileKey", jsonUserProfile)
        editor.apply()
        editor.commit()
        */
    }

}