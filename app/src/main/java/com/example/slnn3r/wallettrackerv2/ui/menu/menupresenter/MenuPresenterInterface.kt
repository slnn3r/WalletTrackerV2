package com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter

import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface MenuPresenterInterface {

    interface MenuViewPresenter{
        fun executeGoogleSignOut(mGoogleSignInClient: GoogleSignInClient)
        fun navigationDrawerSelection(item: MenuItem)

        fun checkNavigationStatus(isNavigated: String, selectedHistoryScreen: String,
                                  isBackButton: Boolean, currentScreen: Int?,
                                  doubleBackToExitPressedOnce: Boolean)
    }
}