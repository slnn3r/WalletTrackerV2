package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionpresenter

import android.content.Context

interface TransactionPresenterInterface {

    interface CreateTransactionPresenter {
        fun checkSwitchButton(isChecked: Boolean)

        fun getAccountList(mContext: Context, userUid: String)
        fun getCategoryList(mContext: Context, userUid: String, filterType: String)
        fun createTransaction()
    }

}