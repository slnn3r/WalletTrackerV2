package com.example.slnn3r.wallettrackerv2.ui.report.reportpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.report.reportmodel.ReportViewModel
import com.example.slnn3r.wallettrackerv2.ui.report.reportview.ReportViewInterface
import java.text.SimpleDateFormat
import java.util.*

class ReportViewPresenter: ReportPresenterInterface.ReportViewPresenter,
        BasePresenter<ReportViewInterface.ReportView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mReportViewModel: ReportViewModel = ReportViewModel()

    override fun getAccountList(mContext: Context, userUid: String) {
        try {
            val dataList = baseModel.getAccListByUserUidSync(mContext, userUid)
            getView()!!.populateAccountSpinner(dataList)
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun checkDateFilter(yearSelection: String, monthSelection: String) {
        if (yearSelection == Constant.ConditionalKeyword.All_YEAR_STATUS ||
                monthSelection == Constant.ConditionalKeyword.All_MONTH_STATUS) {
            getView()!!.disableMonthSelection()
        }

        if (yearSelection != Constant.ConditionalKeyword.All_YEAR_STATUS &&
                monthSelection == Constant.ConditionalKeyword.All_MONTH_STATUS) {
            getView()!!.enableMonthSelection()
        }
    }

    override fun getReportData(mContext: Context, userUid: String,
                               selectedAccount: String, accountList: ArrayList<Account>,
                               selectedMonth: String, selectedYear: String) {
        val simpleMonthFormat = SimpleDateFormat((Constant.Format.MONTH_FORMAT), Locale.US)

        try {
            var startDate: Long = 0
            var endDate: Long = 0
            var isAllYear = false

            lateinit var accountId:String
            accountList.forEach { data->
                if(selectedAccount.equals(data.accountName, ignoreCase = true)){
                    accountId = data.accountId
                }
            }

            if(selectedMonth != Constant.ConditionalKeyword.All_MONTH_STATUS){
                val tempCalender = Calendar.getInstance()

                val cal = Calendar.getInstance()
                cal.time = simpleMonthFormat.parse(selectedMonth)
                val month = (cal.get(Calendar.MONTH) + 1).toString()

                val startAllDate = SimpleDateFormat("$selectedYear/$month/1", Locale.US)
                        .format(tempCalender.time)
                startDate = Date.parse(startAllDate.toString())

                val endDateCalendar = Calendar.getInstance()

                endDateCalendar.set(Calendar.MONTH, month.toInt())

                val maxDay = tempCalender.getActualMaximum(Calendar.DAY_OF_MONTH)
                val endAllDate = SimpleDateFormat("$selectedYear/$month/"
                        + maxDay, Locale.US).format(tempCalender.time)

                val date = Date(endAllDate)
                endDateCalendar.time = date
                endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                endDate = Date.parse(endDateCalendar.time.toString())

                isAllYear = false
            } else if (selectedMonth == Constant.ConditionalKeyword.All_MONTH_STATUS &&
                    selectedYear != Constant.ConditionalKeyword.All_YEAR_STATUS) {
                val tempCalender = Calendar.getInstance()
                val startAllDate = SimpleDateFormat("$selectedYear/1/1", Locale.US)
                        .format(tempCalender.time)
                startDate = Date.parse(startAllDate.toString())

                val endDateCalendar = Calendar.getInstance()
                endDateCalendar.set(Calendar.YEAR, selectedYear.toInt())

                val endAllDate = SimpleDateFormat("$selectedYear/12/31"
                        , Locale.US).format(tempCalender.time)

                val date = Date(endAllDate)
                endDateCalendar.time = date
                endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                endDate = Date.parse(endDateCalendar.time.toString())

                isAllYear = false
            } else if (selectedYear == Constant.ConditionalKeyword.All_YEAR_STATUS) {
                isAllYear = true
            }

            val transactionList = mReportViewModel.getReportDataRealm(mContext,userUid, accountId, startDate, endDate, isAllYear)

            getView()!!.populateSummaryGraph()
            getView()!!.populateReportRecycleView(transactionList)

        }catch (e: Exception){
            getView()!!.onError(e.message.toString())
        }
    }
}