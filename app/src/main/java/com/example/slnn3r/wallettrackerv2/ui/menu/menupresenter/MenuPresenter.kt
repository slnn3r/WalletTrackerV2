package com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter

import android.view.MenuItem
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuViewInterface
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class MenuPresenter : MenuPresenterInterface.MenuPresenter,
        BasePresenter<MenuViewInterface.MenuView>() {

    override fun navigationDrawerSelection(item: MenuItem) {

        when (item.itemId) {
            R.id.nav_account -> {
                getView()!!.closeDrawer()
            }

            R.id.nav_category -> {
                getView()!!.closeDrawer()
            }

            R.id.nav_history -> {
                getView()!!.closeDrawer()
            }

            R.id.nav_report -> {
                getView()!!.displayDrawerDropDown()
            }

            R.id.nav_sub_line_graph -> {
                getView()!!.closeDrawer()
            }

            R.id.nav_sub_bar_graph -> {
                getView()!!.closeDrawer()
            }

            R.id.nav_backup -> {
                getView()!!.closeDrawer()
            }

            R.id.nav_sign_out -> {
                getView()!!.proceedToSignOut()
            }
        }
    }

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
}