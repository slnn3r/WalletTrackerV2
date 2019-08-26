package com.example.slnn3r.wallettrackerv2.ui.history.historymodel

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.google.gson.Gson
import io.realm.*

class HistoryViewModel : HistoryModelInterface.HistoryViewModel {
    override fun getTransactionDataRealm(mContext: Context, userUid: String?,
                                         accountId: String?, startDate: Long, endDate: Long,
                                         remark: String?, isAllYear: Boolean): ArrayList<Transaction> {
        val realm: Realm
        val transactionList = ArrayList<Transaction>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm.executeTransaction {
            val transactionRealm: RealmResults<TransactionRealm>

            if (remark != "" && !isAllYear) {
                transactionRealm = realm.where(TransactionRealm::class.java)
                        .sort(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE, Sort.DESCENDING)
                        .greaterThanOrEqualTo(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE,
                                startDate)
                        .lessThan(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE,
                                endDate)
                        .contains(Constant.RealmVariableName.TRANSACTION_REMARK_VARIABLE,
                                remark!!, Case.INSENSITIVE)
                        .findAll()
            } else if (remark != "" && isAllYear) {
                transactionRealm = realm.where(TransactionRealm::class.java)
                        .sort(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE, Sort.DESCENDING)
                        .contains(Constant.RealmVariableName.TRANSACTION_REMARK_VARIABLE,
                                remark!!, Case.INSENSITIVE)
                        .findAll()
            } else if (isAllYear) {
                transactionRealm = realm.where(TransactionRealm::class.java)
                        .sort(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE, Sort.DESCENDING)
                        .findAll()
            } else {
                transactionRealm = realm.where(TransactionRealm::class.java)
                        .sort(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE, Sort.DESCENDING)
                        .greaterThanOrEqualTo(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE,
                                startDate)
                        .lessThan(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE,
                                endDate)
                        .findAll()
            }

            transactionRealm.forEach { transactionRealmData ->
                val gson = Gson()

                val accountJson = transactionRealmData.account
                val categoryJson = transactionRealmData.category

                val accountData = gson.fromJson<Account>(accountJson, Account::class.java)
                val categoryData = gson.fromJson<Category>(categoryJson, Category::class.java)

                if (accountData.accountId == accountId && accountData.userUid == userUid) {
                    transactionList.add(
                            Transaction(
                                    transactionRealmData.transactionId,
                                    transactionRealmData.transactionDateTime,
                                    transactionRealmData.transactionAmount,
                                    transactionRealmData.transactionRemark,
                                    categoryData,
                                    accountData
                            )
                    )
                }
            }
        }
        realm.close()
        return transactionList
    }

    override fun getFilterInputSharePreference(mContext: Context): SharedPreferences {
        return mContext.getSharedPreferences(Constant.KeyId.FILTER_INPUT_SHARE_PREF,
                AppCompatActivity.MODE_PRIVATE)
    }
}