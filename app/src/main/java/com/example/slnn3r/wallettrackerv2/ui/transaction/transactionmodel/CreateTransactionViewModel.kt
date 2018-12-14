package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration

class CreateTransactionViewModel : TransactionModelInterface.CreateTransactionViewModel {

    override fun createTransactionRealm(mContext: Context, transactionData: Transaction) {
        val realm: Realm
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm.executeTransaction {
            val creating = realm.createObject(TransactionRealm::class.java, transactionData.transactionId)

            val gson = Gson()
            val convertedCategory = gson.toJson(transactionData.category)
            val convertedAccount = gson.toJson(transactionData.account)

            creating.transactionDateTime = transactionData.transactionDateTime
            creating.transactionAmount = transactionData.transactionAmount
            creating.transactionRemark = transactionData.transactionRemark
            creating.category = convertedCategory
            creating.account = convertedAccount
        }
        realm.close()
    }
}