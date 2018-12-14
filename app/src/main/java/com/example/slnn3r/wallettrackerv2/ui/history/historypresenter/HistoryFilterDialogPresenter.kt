package com.example.slnn3r.wallettrackerv2.ui.history.historypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.history.historymodel.HistoryFilterDialogModel
import com.example.slnn3r.wallettrackerv2.ui.history.historyview.HistoryViewInterface

class HistoryFilterDialogPresenter : HistoryPresenterInterface.HistoryFilterDialogPresenter,
        BasePresenter<HistoryViewInterface.HistoryFilterDialog>() {

    private val baseModel: BaseModel = BaseModel()
    private val mHistoryDialogModel: HistoryFilterDialogModel = HistoryFilterDialogModel()

    override fun getAccountList(mContext: Context, userUid: String?) {
        try {
            val dataList = baseModel.getAccListByUserUidSync(mContext, userUid)
            getView()?.populateAccountSpinner(dataList)
        } catch (e: Exception) {
            getView()?.onError(e.message.toString())
        }
    }

    override fun getCategoryList(mContext: Context, userUid: String?, filterType: String) {
        try {
            val categoryList = baseModel.getCatListByUserUidWithFilterSync(
                    mContext, userUid, filterType)
            getView()?.populateCategorySpinner(categoryList)
        } catch (e: Exception) {
            getView()?.onError(e.message.toString())
        }
    }

    override fun getFilterInput(mContext: Context) {
        val data = mHistoryDialogModel.getFilterInputSharePreference(mContext)

        if (data.getString(Constant.KeyId.FILTER_INPUT_ACCOUNT, "") != "") {
            getView()?.setupFilterInput(mHistoryDialogModel.getFilterInputSharePreference(mContext))
        }
    }

    override fun saveFilterInput(mContext: Context, filterAccount: String,
                                 filterCatType: String,
                                 filterCategory: String,
                                 filterRemark: String,
                                 filterDateOption: String,
                                 filterDay: String,
                                 filterMonth: String,
                                 filterYear: String,
                                 filterStartDate: String,
                                 filterEndDate: String) {

        mHistoryDialogModel.saveFilterInputSharePreference(mContext,
                filterAccount,
                filterCatType,
                filterCategory,
                filterRemark,
                filterDateOption,
                filterDay,
                filterMonth,
                filterYear,
                filterStartDate,
                filterEndDate)
    }
}