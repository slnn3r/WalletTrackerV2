package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardpresenter

import android.content.Context

interface DashboardPresenterInterface {

    interface DashboardViewInterface {
        fun getAllAccountData(mContext: Context, userUid: String)
        fun firstTimeSetup(mContext: Context, userUid: String)
        fun getTransactionData(mContext: Context, userUid: String, accountId: String)
    }
}