package com.example.slnn3r.wallettrackerv2.ui.history.historypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
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
        val simpleDateFormat = SimpleDateFormat((Constant.Format.DATE_FORMAT), Locale.US)
        val simpleMonthFormat = SimpleDateFormat((Constant.Format.MONTH_FORMAT), Locale.US)

        val filterInput = mHistoryViewModel.getFilterInputSharePreference(mContext)

        if (filterInput.getString(Constant.KeyId.FILTER_INPUT_ACCOUNT, "") != "") {
            val filteredData = ArrayList<Transaction>()
            lateinit var historyDataList: ArrayList<Transaction>
            var startDate: Long = 0
            var endDate: Long = 0

            val filterAccount =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_ACCOUNT, "")
            val filterCatType =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_CATTYPE, "")
            val filterCategory =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_CATEGORY, "")
            var filterRemark =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_REMARK, "")
            val filterDateOption =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_DATEOPTION, "")
            val filterDay =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_DAY, "")
            val filterMonth =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_MONTH, "")
            val filterYear =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_YEAR, "")
            val filterStartDate =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_STARTDATE, "")
            val filterEndDate =
                    filterInput.getString(Constant.KeyId.FILTER_INPUT_ENDDATE, "")

            lateinit var selectedAccountId: String
            val accountList = baseModel.getAccListByUserUidSync(mContext, userUid)
            accountList.forEach { data ->
                if (data.accountName == filterAccount) {
                    selectedAccountId = data.accountId
                }
            }

            if (filterDateOption == Constant.ConditionalKeyword.SPECIFIC_DATE) {
                if (filterDay == Constant.ConditionalKeyword.All_DAY_STATUS &&
                        filterMonth != Constant.ConditionalKeyword.All_MONTH_STATUS) {
                    val tempCalender = Calendar.getInstance()

                    val cal = Calendar.getInstance()
                    cal.time = simpleMonthFormat.parse(filterMonth)
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

                } else if (filterDay != Constant.ConditionalKeyword.All_DAY_STATUS &&
                        filterMonth != Constant.ConditionalKeyword.All_MONTH_STATUS) {
                    val cal = Calendar.getInstance()
                    cal.time = simpleMonthFormat.parse(filterMonth)
                    val month = (cal.get(Calendar.MONTH) + 1).toString()

                    val endDateFormat =
                            simpleDateFormat.parse("$filterYear/$month/$filterDay")

                    startDate = Date.parse(endDateFormat.toString())

                    val endDateCalendar = Calendar.getInstance()
                    val date = Date(endDateFormat.toString())
                    endDateCalendar.time = date
                    endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                    endDate = Date.parse(endDateCalendar.time.toString())
                } else if (filterMonth == Constant.ConditionalKeyword.All_MONTH_STATUS &&
                        filterYear != Constant.ConditionalKeyword.All_YEAR_STATUS) {
                    val tempCalender = Calendar.getInstance()
                    val startAllDate = SimpleDateFormat("$filterYear/1/1", Locale.US)
                            .format(tempCalender.time)
                    startDate = Date.parse(startAllDate.toString())

                    val endDateCalendar = Calendar.getInstance()
                    endDateCalendar.set(Calendar.YEAR, filterYear!!.toInt())

                    val endAllDate = SimpleDateFormat("$filterYear/12/31"
                            , Locale.US).format(tempCalender.time)

                    val date = Date(endAllDate)
                    endDateCalendar.time = date
                    endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                    endDate = Date.parse(endDateCalendar.time.toString())

                } else if (filterYear == Constant.ConditionalKeyword.All_YEAR_STATUS) {
                    filterRemark = Constant.ConditionalKeyword.All_YEAR_STATUS
                }

            } else {
                val endDateFormat = simpleDateFormat.parse(filterEndDate)

                val endDateCalendar = Calendar.getInstance()
                val date = Date(endDateFormat.toString())
                endDateCalendar.time = date
                endDateCalendar.add(Calendar.DAY_OF_MONTH, 1)

                startDate = Date.parse(filterStartDate)
                endDate = Date.parse(endDateCalendar.time.toString())
            }

            historyDataList = mHistoryViewModel.getTransactionDataRealm(mContext, userUid,
                    selectedAccountId, startDate, endDate, filterRemark!!)

            historyDataList.forEach { data ->
                if (filterCatType == Constant.ConditionalKeyword.All_TYPE_STATUS) {
                    if (filterCategory == Constant.ConditionalKeyword.ALL_CATEGORY_STATUS) {
                        filteredData.add(data)

                    } else if (filterCategory != Constant.ConditionalKeyword.ALL_CATEGORY_STATUS &&
                            data.category.categoryName == filterCategory) {
                        filteredData.add(data)
                    }
                } else if (filterCatType != Constant.ConditionalKeyword.All_TYPE_STATUS) {
                    if (filterCategory == Constant.ConditionalKeyword.ALL_CATEGORY_STATUS &&
                            data.category.categoryType == filterCatType) {
                        filteredData.add(data)
                    } else if (filterCategory != Constant.ConditionalKeyword.ALL_CATEGORY_STATUS &&
                            data.category.categoryName == filterCategory &&
                            data.category.categoryType == filterCatType) {
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
            val selectedAccount = baseModel.getSelectedAccountSharePreference(mContext, userUid)
            var selectedAccountId = ""

            accountList.forEach { data ->
                if (selectedAccount.equals(data.accountName, ignoreCase = false)) {
                    selectedAccountId = data.accountId
                }
            }

            val historyDataList = mHistoryViewModel.getTransactionDataRealm(mContext, userUid,
                    selectedAccountId, Date.parse(startDate),
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
        val totalBalance: Double

        transactionList.forEach { data ->
            if (data.category.categoryType == Constant.ConditionalKeyword.EXPENSE_STATUS) {
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
}