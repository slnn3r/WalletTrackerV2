package com.example.slnn3r.wallettrackerv2.ui.report.reportpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.objectclass.TransactionSummary
import com.example.slnn3r.wallettrackerv2.ui.report.reportmodel.ReportViewModel
import com.example.slnn3r.wallettrackerv2.ui.report.reportview.ReportViewInterface
import com.github.mikephil.charting.data.BarEntry
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportViewPresenter : ReportPresenterInterface.ReportViewPresenter,
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

            lateinit var accountId: String
            accountList.forEach { data ->
                if (selectedAccount.equals(data.accountName, ignoreCase = true)) {
                    accountId = data.accountId
                }
            }

            if (selectedMonth != Constant.ConditionalKeyword.All_MONTH_STATUS) {
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

            val transactionList = mReportViewModel.getReportDataRealm(mContext, userUid, accountId,
                    startDate, endDate, isAllYear)
            val categoryList = baseModel.getCatListByUserUidWithFilterSync(mContext, userUid,
                    Constant.ConditionalKeyword.All_TYPE_STATUS)

            val transactionSummaryList = processTransactionSummaryList(mContext, categoryList, transactionList)

            val entries = processEntries(transactionList)
            val yAxisLabel = ArrayList<String>()
            yAxisLabel.add(mContext.getString(R.string.mp_barChart_balance_title))
            yAxisLabel.add(mContext.getString(R.string.mp_barChart_expense_title))
            yAxisLabel.add(mContext.getString(R.string.mp_barChart_income_title))

            getView()!!.populateSummaryGraph(entries, yAxisLabel)
            getView()!!.populateReportRecycleView(transactionSummaryList, transactionList)

        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    private fun processEntries(transactionList: ArrayList<Transaction>): ArrayList<BarEntry> {
        val entries = ArrayList<BarEntry>()
        var incomeAmount = 0.0
        var expenseAmount = 0.0
        val balanceAmount: Double

        transactionList.forEach { data ->
            if (data.category.categoryType == Constant.ConditionalKeyword.EXPENSE_STATUS) {
                expenseAmount += data.transactionAmount
            } else {
                incomeAmount += data.transactionAmount
            }
        }

        balanceAmount = incomeAmount - expenseAmount

        entries.add(BarEntry(0f, balanceAmount.toFloat()))
        entries.add(BarEntry(1f, expenseAmount.toFloat()))
        entries.add(BarEntry(2f, incomeAmount.toFloat()))

        return entries
    }

    private fun processTransactionSummaryList(mContext: Context,
                                              categoryList: ArrayList<Category>,
                                              transactionList: ArrayList<Transaction>):
            ArrayList<TransactionSummary> {
        val transactionSummaryList = ArrayList<TransactionSummary>()

        var category = ""
        var count = 0
        var amount = 0.0
        var type = ""

        for (i in categoryList) {
            val categoryName = i.categoryName
            val categoryType = i.categoryType

            for (j in transactionList) {
                if (categoryName.equals(j.category.categoryName, ignoreCase = true) &&
                        categoryType.equals(j.category.categoryType, ignoreCase = true)) {
                    category = j.category.categoryName
                    type = j.category.categoryType
                    count += 1

                    if (type == Constant.ConditionalKeyword.EXPENSE_STATUS) {
                        amount -= j.transactionAmount
                    } else {
                        amount += j.transactionAmount
                    }
                }
            }

            if (count != 0) {
                transactionSummaryList.add(
                        TransactionSummary(
                                category,
                                mContext.getString(R.string.tv_countRow_format, count),
                                mContext.getString(R.string.tv_amountRow_format, amount),
                                type
                        )
                )

                category = ""
                count = 0
                amount = 0.0
                type = ""
            }
        }
        return transactionSummaryList
    }
}