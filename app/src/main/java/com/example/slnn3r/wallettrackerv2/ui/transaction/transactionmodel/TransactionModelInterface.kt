package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction

interface TransactionModelInterface {

    interface CreateTransactionViewModel {
        fun createTransactionRealm(mContext: Context, transactionData: Transaction)
    }

    interface DetailsTransactionViewModel {
        fun editTransactionRealm(mContext: Context, transactionData: Transaction)
        fun deleteTransactionRealm(mContext: Context, transactionId: String?)
    }
}