package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionmodel.CreateTransactionViewModel
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview.TransactionViewInterface
import java.util.*

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

    override fun createTransaction(mContext: Context, transactionData: Transaction, userUid: String,
                                   selectedAccount: String, selectedCategory: String,
                                   accountList: ArrayList<Account>,
                                   categoryList: ArrayList<Category>) {
        try {
            lateinit var accountData: Account
            lateinit var categoryData: Category

            // Detect the newly Selected Account/Category and Overwrite the initial data to the updated(newly selected) one
            accountList.forEach { data ->
                // 100% will detect and overwrite even though detect same Account Data (no side affect)
                if (data.accountName.equals(selectedAccount, ignoreCase = true)) {
                    accountData = data
                }
            }

            categoryList.forEach { data ->
                // 100% will detect and overwrite even though detect same Account Data (no side affect)
                if (data.categoryName.equals(selectedCategory, ignoreCase = true)) {
                    categoryData = data
                }
            }

            // create new Transaction Data with Account, Category Data, formatted Time within it
            val finalizedTransactionData = Transaction(
                    transactionData.transactionId,
                    transactionData.transactionDateTime,
                    transactionData.transactionAmount,
                    transactionData.transactionRemark,
                    categoryData,
                    accountData
            )

            mCreateTransactionModel.createTransactionRealm(mContext, finalizedTransactionData)
            getView()!!.createTransactionSuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}