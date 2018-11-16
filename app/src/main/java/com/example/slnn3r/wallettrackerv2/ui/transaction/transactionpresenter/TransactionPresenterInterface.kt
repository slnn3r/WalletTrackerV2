package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionpresenter

import android.content.Context
import android.widget.ArrayAdapter
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.leinardi.android.speeddial.SpeedDialActionItem

interface TransactionPresenterInterface {

    interface CreateTransactionPresenter {
        fun checkSwitchButton(isChecked: Boolean)
        fun getAccountList(mContext: Context, userUid: String)
        fun getCategoryList(mContext: Context, userUid: String, filterType: String)
        fun createTransaction(mContext: Context, transactionData: Transaction,
                              userUid: String, selectedAccount: String, selectedCategory: String,
                              accountList: ArrayList<Account>, categoryList: ArrayList<Category>)
    }

    interface DetailsTransactionPresenter {
        fun checkSwitchButton(isChecked: Boolean)
        fun checkSelectedCategoryType(filterType: String)

        fun checkCategoryData(categoryList: ArrayList<Category>, transactionArgData: Transaction,
                              dataAdapter: ArrayAdapter<String>, categoryNameList: ArrayList<String>,
                              initialLaunch: Boolean)

        fun actionCheck(menuItem: SpeedDialActionItem)

        fun getAccountList(mContext: Context, userUid: String)
        fun getCategoryList(mContext: Context, userUid: String, filterType: String)

        fun editTransaction(mContext: Context, transactionData: Transaction, userUid: String,
                            selectedAccount: String, selectedCategory: String,
                            accountList: ArrayList<Account>, categoryList: ArrayList<Category>,
                            initialTransactionData: Transaction)

        fun deleteTransaction(mContext: Context, transactionId: String)
    }
}