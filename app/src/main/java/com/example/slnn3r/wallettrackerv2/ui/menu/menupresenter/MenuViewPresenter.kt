package com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter

import android.view.MenuItem
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuViewInterface
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class MenuViewPresenter : MenuPresenterInterface.MenuViewPresenter,
        BasePresenter<MenuViewInterface.MenuView>() {

    override fun executeGoogleSignOut(mGoogleSignInClient: GoogleSignInClient) {
        try {
            FirebaseAuth.getInstance().signOut()
        } catch (error: Exception) {
            getView()!!.onError(error.toString())
        }

        mGoogleSignInClient.revokeAccess()
                .addOnFailureListener {
                    getView()!!.onError(it.message.toString())
                    return@addOnFailureListener
                }

        mGoogleSignInClient.signOut()
                .addOnFailureListener {
                    getView()!!.onError(it.message.toString())
                    return@addOnFailureListener
                }

        getView()!!.signOutSuccess()
    }

    override fun navigationDrawerSelection(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_account -> {
                getView()!!.closeDrawer()
                getView()!!.proceedToAccountScreen()
            }

            R.id.nav_category -> {
                getView()!!.closeDrawer()
                getView()!!.proceedToCategoryScreen()
            }

            R.id.nav_history -> {
                getView()!!.closeDrawer()
                getView()!!.proceedToHistoryScreen()
            }

            R.id.nav_report -> {
                getView()!!.displayDrawerDropDown()
            }

            R.id.nav_sub_line_graph -> {
                getView()!!.closeDrawer()
                getView()!!.proceedToLineReportScreen()
            }

            R.id.nav_sub_bar_graph -> {
                getView()!!.closeDrawer()
                getView()!!.proceedToBarReportScreen()
            }

            R.id.nav_backup -> {
                getView()!!.closeDrawer()
            }

            R.id.nav_sign_out -> {
                getView()!!.proceedToSignOut()
            }
        }
    }

    override fun checkNavigationStatus(isNavigated: String, selectedHistoryScreen: String,
                                       isBackButton: Boolean, currentScreen: Int?,
                                       isOpenDrawer: Boolean, doubleBackToExitPressedOnce: Boolean) {
        // Check if Screen is navigated or not
        if (isNavigated == "MenuNavGraph") {
            getView()!!.setupNavigationFlow()
        } else if (isNavigated == "HistoryNavGraph") {
            getView()!!.proceedToHistoryScreen()
        } else if (isNavigated == "NavDisable") {
            // Do Nothing for the ToolBar at Dialogfragment Display
        } else {
            if (isBackButton) {
                if (isOpenDrawer) {
                    getView()!!.closeDrawer()
                } else if (currentScreen == R.id.dashboardFragment) {
                    if (doubleBackToExitPressedOnce) {
                        getView()!!.superOnPressBack()
                    } else {
                        getView()!!.displayDoubleTabExitMessage()
                    }
                } else {
                    getView()!!.superOnPressBack() // not sure when will hit this?
                }
            } else {
                //displaySyncDateTime()
                getView()!!.openDrawer()
            }
        }
    }
}