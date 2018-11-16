package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardmodel.DashboardViewModel
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardview.DashboardViewInterface
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DashboardViewPresenter : DashboardPresenterInterface.DashboardViewInterface,
        BasePresenter<DashboardViewInterface.DashboardView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mDashboardViewModel: DashboardViewModel = DashboardViewModel()

    override fun getAllAccountData(mContext: Context, userUid: String) {
        baseModel.getAccListByUserUidAsync(mContext, userUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Account>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(dataList: ArrayList<Account>) {
                        if (dataList.size < 1) {
                            getView()!!.proceedToFirstTimeSetup()
                        } else {
                            getView()!!.populateAccountSpinner(dataList)
                        }
                    }

                    override fun onError(e: Throwable) {
                        getView()!!.onError(e.message.toString())
                    }

                    override fun onComplete() {
                    }
                })
    }

    override fun firstTimeSetup(mContext: Context, userUid: String) {
        try {
            mDashboardViewModel.firstTimeSetupRealm(mContext, userUid)
            getView()!!.firstTimeSetupSuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun getTransactionData(mContext: Context, userUid: String, accountId: String) {
        mDashboardViewModel.getTransactionRealm(mContext, userUid, accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Transaction>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(dataList: ArrayList<Transaction>) {
                        if (dataList.size < 1) {
                            getView()!!.populateTransactionRecycleView(dataList)
                        } else {
                            getView()!!.populateTransactionRecycleView(dataList)
                        }
                    }

                    override fun onError(e: Throwable) {
                        getView()!!.onError(e.message.toString())
                    }

                    override fun onComplete() {
                    }
                })
    }
}