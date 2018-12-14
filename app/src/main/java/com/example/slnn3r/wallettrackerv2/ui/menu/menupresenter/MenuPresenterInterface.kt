package com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter

import android.content.Context
import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface MenuPresenterInterface {

    interface MenuViewPresenter {
        fun executeGoogleSignOut(mGoogleSignInClient: GoogleSignInClient)
        fun navigationDrawerSelection(item: MenuItem)

        fun checkNavigationStatus(isNavigated: String, isBackButton: Boolean, currentScreen: Int?,
                                  isOpenDrawer: Boolean, doubleBackToExitPressedOnce: Boolean)

        fun checkBackupSetting(mContext: Context, userUid: String?)
        fun checkBackupDateTime(mContext: Context, userUid: String?)

        fun backupDataManually(mContext: Context, userUid: String?)
        fun backupDataPeriodically(mContext: Context, userUid: String?)

        fun stopBackupDataPeriodically(mContext: Context)
    }
}