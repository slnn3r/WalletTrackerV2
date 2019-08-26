package com.example.slnn3r.wallettrackerv2.ui.history.historymodel

import android.content.Context
import android.content.SharedPreferences
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface HistoryModelInterface {

    interface HistoryViewModel {
        fun getFilterInputSharePreference(mContext: Context): SharedPreferences

        fun getTransactionDataRealm(mContext: Context, userUid: String?,
                                    accountId: String?, startDate: Long,
                                    endDate: Long, remark: String?, isAllYear: Boolean): ArrayList<Transaction>
    }

    interface HistoryFilterDialogModel {
        fun getFilterInputSharePreference(mContext: Context): SharedPreferences

        fun saveFilterInputSharePreference(mContext: Context, filterAccount: String,
                                           filterCatType: String,
                                           filterCategory: String,
                                           filterRemark: String,
                                           filterDateOption: String,
                                           filterDay: String,
                                           filterMonth: String,
                                           filterYear: String,
                                           filterStartDate: String,
                                           filterEndDate: String)
    }
}