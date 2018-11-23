package com.example.slnn3r.wallettrackerv2.ui.history.historypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.history.historymodel.HistoryFilterDialogModel
import com.example.slnn3r.wallettrackerv2.ui.history.historyview.HistoryViewInterface

class HistoryFilterDialogPresenter : HistoryPresenterInterface.HistoryFilterDialogPresenter,
        BasePresenter<HistoryViewInterface.HistoryFilterDialog>() {

    private val baseModel: BaseModel = BaseModel()
    private val mHistoryDialogModel: HistoryFilterDialogModel = HistoryFilterDialogModel()

    override fun getAccountList(mContext: Context, userUid: String) {
        try {
            val dataList = baseModel.getAccListByUserUidSync(mContext, userUid)
            getView()!!.populateAccountSpinner(dataList)
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun getCategoryList(mContext: Context, userUid: String, filterType: String) {
        try {
            val categoryList = baseModel.getCatListByUserUidWithFilterSync(
                    mContext, userUid, filterType)
            getView()!!.populateCategorySpinner(categoryList)
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun getPreviousInput(mContext: Context) {

        val data = mHistoryDialogModel.getPreviousInputSharePreference(mContext)

        if (data.getString("previousAccount","")!=""){
            getView()!!.setupPreviousInput(mHistoryDialogModel.getPreviousInputSharePreference(mContext))
        }else{
            getView()!!.setupDefaultInput()
        }

    }

    override fun savePreviousInput(mContext: Context, previousAccount: String,
                                   previousCatType: String,
                                   previousCategory: String,
                                   previousRemark: String,
                                   previousDateOption: String,
                                   previousDay: String,
                                   previousMonth: String,
                                   previousYear: String,
                                   previousStartDate: String,
                                   previousEndDate: String) {

        mHistoryDialogModel.savePreviousInputSharePreference(mContext,
                previousAccount,
                previousCatType,
                previousCategory,
                previousRemark,
                previousDateOption,
                previousDay,
                previousMonth,
                previousYear,
                previousStartDate,
                previousEndDate)
    }
}