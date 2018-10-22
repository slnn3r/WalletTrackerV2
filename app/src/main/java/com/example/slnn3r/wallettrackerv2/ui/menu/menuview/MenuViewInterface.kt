package com.example.slnn3r.wallettrackerv2.ui.menu.menuview

import com.example.slnn3r.wallettrackerv2.base.BaseView

interface MenuViewInterface {

    interface MenuView : BaseView.Universal {
        fun proceedToSignOut()
        fun signOutSuccess()

        fun displayDrawerDropDown()
        fun closeDrawer()
    }
}