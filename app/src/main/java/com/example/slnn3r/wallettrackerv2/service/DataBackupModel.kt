package com.example.slnn3r.wallettrackerv2.service

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort

class DataBackupModel {

    fun getAllAccountDataByUserUid(mContext: Context, userUid: String): ArrayList<Account> {
        val realm: Realm?
        val accountList = ArrayList<Account>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.ACCOUNT_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val accountRealm = realm.where(AccountRealm::class.java)
                    .equalTo(Constant.RealmVariableName.USER_UID_VARIABLE, userUid)
                    .findAll()

            accountRealm.forEach { accountRealmData ->
                accountList.add(
                        Account(
                                accountRealmData.accountId!!,
                                accountRealmData.accountName!!,
                                accountRealmData.accountDesc!!,
                                accountRealmData.userUid!!,
                                accountRealmData.accountStatus!!
                        )
                )
            }
        }
        realm.close()
        return accountList
    }

    fun getAllCategoryDataByUserUid(mContext: Context, userUid: String): ArrayList<Category> {
        val realm: Realm?
        val categoryList = ArrayList<Category>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.CATEGORY_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val categoryRealm = realm.where(CategoryRealm::class.java)
                    .equalTo(Constant.RealmVariableName.USER_UID_VARIABLE, userUid)
                    .findAll()

            categoryRealm.forEach { categoryRealmData ->
                categoryList.add(
                        Category(
                                categoryRealmData.categoryId!!,
                                categoryRealmData.categoryName!!,
                                categoryRealmData.categoryType!!,
                                categoryRealmData.categoryStatus!!,
                                userUid
                        )
                )
            }
        }
        realm.close()
        return categoryList
    }

    fun getAllTransactionDataByUserUid(mContext: Context, userUid: String): ArrayList<Transaction> {
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

            transactionRealm.forEach { transactionRealmData ->
                val gson = Gson()

                val accountJson = transactionRealmData.account
                val categoryJson = transactionRealmData.category

                val accountData = gson.fromJson<Account>(accountJson, Account::class.java)
                val categoryData = gson.fromJson<Category>(categoryJson, Category::class.java)

                if (accountData.userUid == userUid) {
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