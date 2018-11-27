package com.example.slnn3r.wallettrackerv2.ui.report.reportmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

class ReportViewModel : ReportModelInterface.ReportViewModel {

    override fun getReportDataRealm(mContext: Context, userUid: String, accountId: String,
                                    startDate: Long, endDate: Long,
                                    isAllYear: Boolean): ArrayList<Transaction> {
        val realm: Realm?
        val transactionList = ArrayList<Transaction>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val transactionRealm: RealmResults<TransactionRealm> = if (!isAllYear) {
                realm.where(TransactionRealm::class.java)
                        .greaterThanOrEqualTo(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE,
                                startDate)
                        .lessThan(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE,
                                endDate)
                        .findAll()
            } else {
                realm.where(TransactionRealm::class.java)
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
                                    transactionRealmData.transactionId!!,
                                    transactionRealmData.transactionDateTime!!,
                                    transactionRealmData.transactionAmount,
                                    transactionRealmData.transactionRemark!!,
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
}