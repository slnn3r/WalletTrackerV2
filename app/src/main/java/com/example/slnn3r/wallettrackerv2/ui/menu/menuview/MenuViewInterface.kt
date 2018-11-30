package com.example.slnn3r.wallettrackerv2.ui.menu.menuview

import com.example.slnn3r.wallettrackerv2.base.BaseView

interface MenuViewInterface {

    interface MenuView : BaseView.Universal {
        fun setupNavigationFlow()

        fun openDrawer()
        fun closeDrawer()

        fun proceedToAccountScreen()
        fun proceedToCategoryScreen()

        fun proceedToHistoryScreen()
        fun proceedToReportScreen()

        fun executeBackupOnBackground()

        fun proceedToSettingScreen()
        fun proceedToSignOut()
        fun signOutSuccess()

        fun displayDoubleTabExitMessage()
        fun superOnPressBack()

        fun updateDrawerBackupDateTime(backupDateTime: String)

        fun executePeriodicalBackup()

        fun backupOnBackgroundStart()
        fun backupPeriodicallyStart()
    }
}