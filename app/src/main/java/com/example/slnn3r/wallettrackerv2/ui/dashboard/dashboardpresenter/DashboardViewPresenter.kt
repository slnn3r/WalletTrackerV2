package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardmodel.DashboardViewModel
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardview.DashboardViewInterface
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewPresenter : DashboardPresenterInterface.DashboardViewInterface,
        BasePresenter<DashboardViewInterface.DashboardView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mDashboardViewModel: DashboardViewModel = DashboardViewModel()

    override fun getAllAccountData(mContext: Context, userUid: String) {
        try {
            val dataList = baseModel.getAccListByUserUidSync(mContext, userUid)

            if (dataList.size < 1 && getView() != null) {
                getView()!!.proceedToFirstTimeSetup()
            } else {
                getView()!!.populateAccountSpinner(dataList)
            }
        } catch (e: Exception) {
            if (getView() != null) {
                getView()!!.onError(e.message.toString())
            }
        }
    }

    override fun firstTimeSetup(mContext: Context, userUid: String) {
        try {
            mDashboardViewModel.firstTimeSetupRealm(mContext, userUid)
            if (getView() != null) {
                getView()!!.firstTimeSetupSuccess()
            }
        } catch (e: Exception) {
            if (getView() != null) {
                getView()!!.onError(e.message.toString())
            }
        }
    }

    override fun getTransactionData(mContext: Context, userUid: String, accountId: String) {
        try {
            if (getView() != null) {
                getView()!!.populateTransactionRecycleView(
                        mDashboardViewModel.getTransactionRealm(mContext, userUid, accountId))
            }
        } catch (e: Exception) {
            if (getView() != null) {
                getView()!!.onError(e.message.toString())
            }
        }
    }

    override fun getRecentExpenseTransaction(mContext: Context, userUid: String, accountId: String) {
        mDashboardViewModel.getRecentMonthTransactionRealm(mContext, userUid, accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Transaction>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(dataList: ArrayList<Transaction>) {
                        val entries = ArrayList<Entry>()
                        val xAxisLabel = ArrayList<String>()

                        val sdf = SimpleDateFormat(Constant.Format.DATE_FORMAT, Locale.US)

                        for (a in 0..30) {
                            val tempCalendar = Calendar.getInstance()

                            tempCalendar.add(Calendar.DAY_OF_MONTH, -a)

                            val thisDay = tempCalendar.get(Calendar.DAY_OF_MONTH)
                            val thisMonth = tempCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())

                            xAxisLabel.add(thisDay.toString() + thisMonth)

                            val compareDate = sdf.format(tempCalendar.time)
                            var expense = 0.0

                            /*
                            val realm: Realm?
                            Realm.init(mContext)

                            val config = RealmConfiguration.Builder()
                                    .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                                    .build()

                            realm = Realm.getInstance(config)

                            realm!!.executeTransaction {
                                val creating = realm.createObject(TransactionRealm::class.java, UUID.randomUUID().toString())

                                val gson = Gson()
                                val convertedCategory = gson.toJson(dataList[0].category)
                                val convertedAccount = gson.toJson(dataList[0].account)

                                creating.transactionDateTime = Date.parse(compareDate)
                                creating.transactionAmount = 10.0
                                creating.transactionRemark = "test"
                                creating.category = convertedCategory
                                creating.account = convertedAccount
                            }
                            realm.close()
                            */

                            dataList.forEach { data ->
                                if (sdf.format(data.transactionDateTime) == compareDate &&
                                        data.category.categoryType == Constant.ConditionalKeyword.EXPENSE_STATUS) {
                                    expense += data.transactionAmount
                                }
                            }
                            entries.add(BarEntry(a.toFloat(), expense.toFloat()))
                        }

                        if (getView() != null) {
                            getView()!!.populateExpenseGraph(mContext, entries, xAxisLabel)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (getView() != null) {
                            getView()!!.onError(e.message.toString())
                        }
                    }

                    override fun onComplete() {
                    }
                })
    }
}