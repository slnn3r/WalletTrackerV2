package com.example.slnn3r.wallettrackerv2.ui.history.historypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface HistoryPresenterInterface {

    interface HistoryViewPresenter {
        fun getHistoryData(mContext: Context, userUid: String)
        fun calculateHistoryData(transactionList: ArrayList<Transaction>)
        fun removePreviousInput(mContext: Context)
    }

    interface HistoryFilterDialogPresenter {
        fun getAccountList(mContext: Context, userUid: String)
        fun getCategoryList(mContext: Context, userUid: String, filterType: String)
        fun getPreviousInput(mContext: Context)
        fun savePreviousInput(mContext: Context, previousAccount: String,
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