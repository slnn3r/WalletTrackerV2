package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration

class DetailsTransactionViewModel : TransactionModelInterface.DetailsTransactionViewModel {

    override fun editCategoryRealm(mContext: Context, transactionData: Transaction) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val transactionRealm = realm.where(TransactionRealm::class.java)
                    .equalTo(Constant.RealmVariableName.TRANSACTION_ID_VARIABLE,
                            transactionData.transactionId).findAll()

            val gson = Gson()
            val convertedCategory = gson.toJson(transactionData.category)
            val convertedAccount = gson.toJson(transactionData.account)

            transactionRealm.forEach { transactionRealmData ->
                transactionRealmData.transactionAmount = transactionData.transactionAmount
                transactionRealmData.transactionRemark = transactionData.transactionRemark
                transactionRealmData.transactionDate = transactionData.transactionDate
                transactionRealmData.transactionTime = transactionData.transactionTime
                transactionRealmData.category = convertedCategory
                transactionRealmData.account = convertedAccount
            }
        }
        realm.close()
    }

    override fun deleteCategoryRealm(mContext: Context, transactionId: String) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val transactionRealm = realm.where(TransactionRealm::class.java)
                    .equalTo(Constant.RealmVariableName.TRANSACTION_ID_VARIABLE,
                            transactionId).findAll()

            transactionRealm.forEach { transactionRealmData ->
                transactionRealmData.deleteFromRealm()
            }
        }
        realm.close()
    }
}