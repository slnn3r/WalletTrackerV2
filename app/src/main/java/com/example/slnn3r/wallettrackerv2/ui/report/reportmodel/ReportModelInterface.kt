package com.example.slnn3r.wallettrackerv2.ui.report.reportmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface ReportModelInterface {

    interface ReportViewModel {
        fun getReportDataRealm(mContext: Context, userUid: String,
                               accountId: String?, startDate: Long, endDate: Long,
                               isAllYear: Boolean): ArrayList<Transaction>
    }
}