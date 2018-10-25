package com.example.slnn3r.wallettrackerv2.ui.menu.menuview

import com.example.slnn3r.wallettrackerv2.base.BaseView

interface MenuViewInterface {

    interface MenuView : BaseView.Universal {

        fun proceedToAccountScreen()
        fun proceedToCategoryScreen()

        fun proceedToHistoryScreen()
        fun proceedToHistorySpecific()
        fun proceedToHistoryRange()

        fun proceedToLineReportScreen()
        fun proceedToBarReportScreen()

        fun proceedToSignOut()
        fun signOutSuccess()

        fun displayDoubleTabExitMessage()
        fun superOnPressBack()

        fun setupNavigationFlow()

        fun displayDrawerDropDown()
        fun openDrawer()
        fun closeDrawer()
    }
}