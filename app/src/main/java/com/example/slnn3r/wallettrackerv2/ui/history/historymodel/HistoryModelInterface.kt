package com.example.slnn3r.wallettrackerv2.ui.history.historymodel

import android.content.Context
import android.content.SharedPreferences
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface HistoryModelInterface {

    interface HistoryViewModel {
        fun getPreviousInputSharePreference(mContext: Context): SharedPreferences
        fun removePreviousInputSharePreference(mContext: Context)


        fun getTransactionDataRealm(mContext: Context, userUid: String,
                                    accountId: String, startDate: Long,
                                    endDate: Long, remark: String): ArrayList<Transaction>
    }

    interface HistoryFilterDialogModel {
        fun getPreviousInputSharePreference(mContext: Context): SharedPreferences

        fun savePreviousInputSharePreference(mContext: Context, previousAccount: String,
                                             previousCatType: String,
                                             previousCategory: String,
                                             previousRemark: String,
                                             previousDateOption: String,
                                             previousDay: String,
                                             previousMonth: String,
                                             previousYear: String,
                                             previousStartDate: String,
                                             previousEndDate: String)
    }

}