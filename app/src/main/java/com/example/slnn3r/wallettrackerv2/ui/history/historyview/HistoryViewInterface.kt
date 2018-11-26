package com.example.slnn3r.wallettrackerv2.ui.history.historyview

import android.content.SharedPreferences
import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface HistoryViewInterface {

    interface HistoryView : BaseView.Universal {
        fun populateHistoryData(transactionList: ArrayList<Transaction>)
        fun populateSummaryHistoryData(transCount: Int, expCount: Int, expTotal: Double,
                                       incCount: Int, incTotal: Double, totalBalance: Double)
    }

    interface HistoryFilterDialog : BaseView.Universal {
        fun populateAccountSpinner(accountList: ArrayList<Account>)
        fun populateCategorySpinner(categoryList: ArrayList<Category>)
        fun setupFilterInput(editor: SharedPreferences)
    }
}