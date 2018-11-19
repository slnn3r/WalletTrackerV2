package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.defaultdata.CategoryDefaultData
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.google.gson.Gson
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardViewModel : DashboardModelInterface.DashboardViewModel {

    override fun firstTimeSetupRealm(mContext: Context, userUid: String) {
        val uniqueAccountId = UUID.randomUUID().toString()
        val defaultAccountName = Constant.DefaultValue.DEFAULT_ACCOUNT_NAME
        val defaultAccountBalance = Constant.DefaultValue.DEFAULT_ACCOUNT_DESC
        val defaultAccountStatus = Constant.DefaultValue.DEFAULT_STATUS

        var realm: Realm?
        Realm.init(mContext)

        val accountTableConfig = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.ACCOUNT_REALM_TABLE)
                .build()

        realm = Realm.getInstance(accountTableConfig)

        realm!!.executeTransaction {
            val creating = realm!!.createObject(AccountRealm::class.java, uniqueAccountId)

            creating.accountName = defaultAccountName
            creating.accountDesc = defaultAccountBalance
            creating.userUid = userUid
            creating.accountStatus = defaultAccountStatus
        }
        realm.close()

        val categoryTableConfig = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.CATEGORY_REALM_TABLE)
                .build()

        realm = Realm.getInstance(categoryTableConfig)

        realm!!.executeTransaction {
            for (item in CategoryDefaultData().getListItem()) {
                val uniqueCategoryId = UUID.randomUUID().toString()
                val creating = realm.createObject(CategoryRealm::class.java, uniqueCategoryId)

                creating.categoryName = item.categoryName
                creating.categoryType = item.categoryType
                creating.categoryStatus = item.categoryStatus
                creating.userUid = userUid
            }
        }
        realm.close()
    }

    override fun getTransactionRealm(mContext: Context, userUid: String, accountId: String): ArrayList<Transaction> {
        val realm: Realm?
        val transactionList = ArrayList<Transaction>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val transactionRealm = realm.where(TransactionRealm::class.java)
                    .sort(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE, Sort.DESCENDING)
                    .findAll()

            var count = 0

            transactionRealm.forEach { transactionRealmData ->
                val gson = Gson()

                val accountJson = transactionRealmData.account
                val categoryJson = transactionRealmData.category

                val accountData = gson.fromJson<Account>(accountJson, Account::class.java)
                val categoryData = gson.fromJson<Category>(categoryJson, Category::class.java)

                if (accountData.accountId == accountId && accountData.userUid == userUid
                        && count < Constant.ConditionalFigure.MAX_RECENT_TRANSACTION_LIST) {
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
                    count += 1
                }
            }
        }
        realm.close()
        return transactionList
    }

    override fun getRecentMonthTransactionRealm(mContext: Context, userUid: String, accountId: String):
            Observable<ArrayList<Transaction>> {
        val realm: Realm?
        val transactionList = ArrayList<Transaction>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        val sdf = SimpleDateFormat(Constant.Format.DATE_FORMAT, Locale.US)

        val tempCalender = Calendar.getInstance()
        tempCalender.add(Calendar.DAY_OF_MONTH, 1)
        val todayDate = Date.parse(sdf.format(tempCalender.time))
        tempCalender.add(Calendar.DAY_OF_MONTH, -31)
        val previous30DaysDate = Date.parse(sdf.format(tempCalender.time))

        realm!!.executeTransaction {
            val transactionRealm = realm.where(TransactionRealm::class.java)
                    .sort(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE, Sort.DESCENDING)
                    .between(Constant.RealmVariableName.TRANSACTION_DATETIME_VARIABLE,
                            previous30DaysDate, todayDate)
                    .findAll()

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
        return Observable.just(transactionList)
    }
}