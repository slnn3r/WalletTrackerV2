package com.example.slnn3r.wallettrackerv2.ui.report.reportview

import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface ReportViewInterface {

    interface ReportView : BaseView.Universal{
        fun populateAccountSpinner(accountList: ArrayList<Account>)
        fun enableMonthSelection()
        fun disableMonthSelection()
        fun populateReportRecycleView(transactionList: ArrayList<Transaction>)
        fun populateSummaryGraph()
    }
}