package com.example.slnn3r.wallettrackerv2.ui.report.reportview

import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.objectclass.TransactionSummary
import com.github.mikephil.charting.data.BarEntry

interface ReportViewInterface {

    interface ReportView : BaseView.Universal {
        fun populateAccountSpinner(accountList: ArrayList<Account>)
        fun enableMonthSelection()
        fun disableMonthSelection()
        fun populateReportRecycleView(transactionSummaryList: ArrayList<TransactionSummary>,
                                      transactionList: ArrayList<Transaction>)

        fun populateSummaryGraph(entryList: ArrayList<BarEntry>, yAxisList: ArrayList<String>)
        fun setTotalTransactionLabel(transactionCount: Int)
    }
}