package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionmodel.CreateTransactionViewModel
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview.TransactionViewInterface

class CreateTransactionPresenter : TransactionPresenterInterface.CreateTransactionPresenter,
        BasePresenter<TransactionViewInterface.CreateTransactionView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mCreateTransactionModel: CreateTransactionViewModel = CreateTransactionViewModel()

    override fun checkSwitchButton(isChecked: Boolean) {
        if (isChecked) {
            getView()!!.switchButtonExpenseMode()
        } else {
            getView()!!.switchButtonIncomeMode()
        }
    }

    override fun getAccountList(mContext: Context, userUid: String) {
        try {
            val dataList = baseModel.getAccountListByUserUidSync(mContext, userUid)
            getView()!!.populateAccountSpinner(dataList)
        } catch (e: Exception){
            getView()!!.onError(e.message.toString())
        }
    }

    override fun getCategoryList(mContext: Context, userUid: String, filterType: String) {
        try {
            val categoryList = baseModel.getCategoryListByUserUidWithFilterSync(
                    mContext, userUid, filterType)
            getView()!!.populateCategorySpinner(categoryList)
        } catch (e: Exception){
            getView()!!.onError(e.message.toString())
        }
    }

    override fun createTransaction() {
        try {
            mCreateTransactionModel.createTransactionRealm()
            getView()!!.createTransactionSuccess()
        }catch (e: Exception){
            getView()!!.onError(e.message.toString())
        }
    }
}