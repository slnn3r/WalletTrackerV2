package com.example.slnn3r.wallettrackerv2.ui.menu.menuview

import com.example.slnn3r.wallettrackerv2.base.BaseView

interface MenuViewInterface {

    interface MenuView : BaseView.Universal {
        fun setupNavigationFlow()

        fun displayDrawerDropDown()
        fun openDrawer()
        fun closeDrawer()

        fun proceedToAccountScreen()
        fun proceedToCategoryScreen()

        fun proceedToHistoryScreen()

        fun proceedToLineReportScreen()
        fun proceedToBarReportScreen()

        fun proceedToSignOut()
        fun signOutSuccess()

        fun displayDoubleTabExitMessage()
        fun superOnPressBack()
    }
}