package com.example.slnn3r.wallettrackerv2.ui.history.historypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface HistoryPresenterInterface {

    interface HistoryViewPresenter {
        fun getHistoryData(mContext: Context, userUid: String?)
        fun calculateHistoryData(transactionList: ArrayList<Transaction>)
    }

    interface HistoryFilterDialogPresenter {
        fun getAccountList(mContext: Context, userUid: String?)
        fun getCategoryList(mContext: Context, userUid: String?, filterType: String)
        fun getFilterInput(mContext: Context)
        fun saveFilterInput(mContext: Context, filterAccount: String,
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