package com.example.slnn3r.wallettrackerv2.ui.history.historypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.history.historymodel.HistoryViewModel
import com.example.slnn3r.wallettrackerv2.ui.history.historyview.HistoryViewInterface
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryViewPresenter : HistoryPresenterInterface.HistoryViewPresenter,
        BasePresenter<HistoryViewInterface.HistoryView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mHistoryViewModel: HistoryViewModel = HistoryViewModel()

    override fun getHistoryData(mContext: Context, userUid: String) {
        val previousInput = mHistoryViewModel.getPreviousInputSharePreference(mContext)

        if (previousInput.getString("previousAccount", "") != "") {

            val filteredData = ArrayList<Transaction>()
            lateinit var historyDataList: ArrayList<Transaction>
            var startDate: Long = 0
            var endDate: Long = 0

            val previousAccount = previousInput.getString("previousAccount", "")
            val previousCatType = previousInput.getString("previousCatType", "")
            val previousCategory = previousInput.getString("previousCategory", "")
            var previousRemark = previousInput.getString("previousRemark", "")
            val previousDateOption = previousInput.getString("previousDateOption", "")
            val previousDay = previousInput.getString("previousDay", "")
            val previousMonth = previousInput.getString("previousMonth", "")
            val previousYear = previousInput.getString("previousYear", "")
            val previousStartDate = previousInput.getString("previousStartDate", "")
            val previousEndDate = previousInput.getString("previousEndDate", "")

            ////
            lateinit var selectedAccountId: String
            val accountList = baseModel.getAccListByUserUidSync(mContext, userUid)
            accountList.forEach { data ->
                if (data.accountName == previousAccount) {
                    selectedAccountId = data.accountId
                }
            }

            if (previousDateOption == "Specific Date") {

                if (previousDay == "All Day" && previousMonth != "All Month") {
                    val tempCalender = Calendar.getInstance()

                    val cal = Calendar.getInstance()
                    cal.time = SimpleDateFormat("MMM", Locale.US).parse(previousMonth)
                    val month = (cal.get(Calendar.MONTH) + 1).toString()

                    val startAllDate = SimpleDateFormat("yyyy/$month/1", Locale.US)
                            .format(tempCalender.time)
                    startDate = Date.parse(startAllDate.toString())

                    val endDateCalendar = Calendar.getInstance()

                    endDateCalendar.set(Calendar.MONTH, month.toInt())

                    val maxDay = tempCalender.getActualMaximum(Calendar.DAY_OF_MONTH)
                    val endAllDate = SimpleDateFormat("yyyy/$month/"
                            + maxDay, Locale.US).format(tempCalender.time)

                    val date = Date(endAllDate)
                    endDateCalendar.time = date
                    endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                    endDate = Date.parse(endDateCalendar.time.toString())

                } else if (previousDay != "All Day" && previousMonth != "All Month") {
                    val cal = Calendar.getInstance()
                    cal.time = SimpleDateFormat("MMM", Locale.US).parse(previousMonth)
                    val month = (cal.get(Calendar.MONTH) + 1).toString()

                    val endDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                            .parse("$previousYear/$month/$previousDay")

                    startDate = Date.parse(endDateFormat.toString())

                    val endDateCalendar = Calendar.getInstance()
                    val date = Date(endDateFormat.toString())
                    endDateCalendar.time = date
                    endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                    endDate = Date.parse(endDateCalendar.time.toString())
                } else if (previousMonth == "All Month" && previousYear != "All Year") {
                    val tempCalender = Calendar.getInstance()
                    val startAllDate = SimpleDateFormat("$previousYear/1/1", Locale.US)
                            .format(tempCalender.time)
                    startDate = Date.parse(startAllDate.toString())

                    val endDateCalendar = Calendar.getInstance()
                    endDateCalendar.set(Calendar.YEAR, previousYear!!.toInt())

                    val endAllDate = SimpleDateFormat("$previousYear/12/31"
                            , Locale.US).format(tempCalender.time)

                    val date = Date(endAllDate)
                    endDateCalendar.time = date
                    endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                    endDate = Date.parse(endDateCalendar.time.toString())

                } else if (previousYear == "All Year") {
                    previousRemark = "All Year"
                }


            } else {
                val endDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
                        .parse(previousEndDate)

                val endDateCalendar = Calendar.getInstance()
                val date = Date(endDateFormat.toString())
                endDateCalendar.time = date
                endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                startDate = Date.parse(previousStartDate)
                endDate = Date.parse(endDateCalendar.time.toString())
            }

            historyDataList = mHistoryViewModel.getTransactionDataRealm(mContext, userUid,
                    selectedAccountId, startDate, endDate, previousRemark!!)

            historyDataList.forEach { data ->
                if (previousCatType == "All Type") {
                    if (previousCategory == "All Category") {
                        filteredData.add(data)

                    } else if (previousCategory != "All Category" &&
                            data.category.categoryName == previousCategory) {
                        filteredData.add(data)
                    }
                } else if (previousCatType != "All Type") {
                    if (previousCategory == "All Category" &&
                            data.category.categoryType == previousCatType) {
                        filteredData.add(data)
                    } else if (previousCategory != "All Category" &&
                            data.category.categoryName == previousCategory &&
                            data.category.categoryType == previousCatType) {
                        filteredData.add(data)
                    }
                }
            }

            getView()!!.populateHistoryData(filteredData)

        } else {
            val tempCalender = Calendar.getInstance()
            val startDate = SimpleDateFormat("yyyy/MM/1", Locale.US)
                    .format(tempCalender.time)

            val maxDay = tempCalender.getActualMaximum(Calendar.DAY_OF_MONTH)
            val endDate = SimpleDateFormat("yyyy/MM/"
                    + maxDay, Locale.US).format(tempCalender.time)

            val endDateCalendar = Calendar.getInstance()
            val date = Date(endDate)
            endDateCalendar.time = date
            endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

            val accountList = baseModel.getAccListByUserUidSync(mContext, userUid)
            val historyDataList = mHistoryViewModel.getTransactionDataRealm(mContext, userUid,
                    accountList[0].accountId, Date.parse(startDate),
                    Date.parse(endDateCalendar.time.toString()), "")

            getView()!!.populateHistoryData(historyDataList)
        }
    }

    override fun calculateHistoryData(transactionList: ArrayList<Transaction>) {

        val totalCount = transactionList.size
        var expCount = 0
        var incCount = 0
        var expTotal = 0.0
        var incTotal = 0.0
        var totalBalance: Double

        transactionList.forEach { data ->
            if (data.category.categoryType == "Expense") {
                expTotal += data.transactionAmount
                expCount += 1
            } else {
                incTotal += data.transactionAmount
                incCount += 1
            }
        }

        totalBalance = incTotal - expTotal

        getView()!!.populateSummaryHistoryData(totalCount, expCount, expTotal,
                incCount, incTotal, totalBalance)
    }


    override fun removePreviousInput(mContext: Context) {
        mHistoryViewModel.removePreviousInputSharePreference(mContext)
    }
}