package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import io.reactivex.Observable

interface DashboardModelInterface {

    interface DashboardViewModel {
        fun firstTimeSetupRealm(mContext: Context, userUid: String)
        fun getTransactionRealm(mContext: Context, userUid: String, accountId: String):
                ArrayList<Transaction>

        fun getRecentMonthTransactionRealm(mContext: Context, userUid: String, accountId: String):
                Observable<ArrayList<Transaction>>
    }
}