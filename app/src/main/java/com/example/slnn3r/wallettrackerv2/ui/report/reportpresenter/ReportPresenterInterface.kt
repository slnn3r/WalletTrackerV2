package com.example.slnn3r.wallettrackerv2.ui.report.reportpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account

interface ReportPresenterInterface {

    interface ReportViewPresenter{
        fun getAccountList(mContext: Context, userUid: String)
        fun checkDateFilter(yearSelection: String, monthSelection: String)
        fun getReportData(mContext: Context, userUid: String,
                          selectedAccount: String, accountList: ArrayList<Account>,
                          selectedMonth: String, selectedYear: String)
    }
}