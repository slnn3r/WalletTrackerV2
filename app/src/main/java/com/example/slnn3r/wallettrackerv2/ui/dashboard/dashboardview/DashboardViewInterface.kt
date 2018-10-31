package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardview

import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account

interface DashboardViewInterface {

    interface DashboardView : BaseView.Universal{

        fun populateAccountSpinner(accountList: ArrayList<Account>)
        fun proceedToFirstTimeSetup()
        fun firstTimeSetupSuccess()
    }
}