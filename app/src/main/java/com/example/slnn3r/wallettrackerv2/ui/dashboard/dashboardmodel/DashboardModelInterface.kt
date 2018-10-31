package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardmodel

import android.content.Context

interface DashboardModelInterface {

    interface DashboardViewModel {
        fun firstTimeSetupRealm(mContext: Context, userUid: String)
    }

}