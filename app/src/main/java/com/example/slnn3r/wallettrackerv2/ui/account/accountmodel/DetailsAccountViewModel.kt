package com.example.slnn3r.wallettrackerv2.ui.account.accountmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration

class DetailsAccountViewModel : AccountModelInterface.DetailsAccountViewModel {

    override fun editAccountRealm(mContext: Context, accountData: Account) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.ACCOUNT_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val accountRealm =
                    realm.where(AccountRealm::class.java)
                            .equalTo(Constant.RealmVariableName.ACCOUNT_ID_VARIABLE,
                                    accountData.accountId).findAll()

            accountRealm.forEach { accountRealmData ->
                accountRealmData.accountName = accountData.accountName
                accountRealmData.accountDesc = accountData.accountDesc
            }
        }
        realm.close()
    }

    override fun deleteAccountRealm(mContext: Context, accountId: String) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.ACCOUNT_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val accountRealm =
                    realm.where(AccountRealm::class.java)
                            .equalTo(Constant.RealmVariableName.ACCOUNT_ID_VARIABLE,
                                    accountId).findAll()

            accountRealm.forEach { accountRealmData ->
                accountRealmData.deleteFromRealm()
            }
        }
        realm.close()

        deleteAccountTransaction(mContext, accountId)
    }

    private fun deleteAccountTransaction(mContext: Context, accountId: String) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val transactionRealm =
                    realm.where(TransactionRealm::class.java).findAll()

            transactionRealm.forEach { transactionRealmData ->
                val gson = Gson()
                val accountJson = transactionRealmData.account
                val accountData = gson.fromJson<Account>(accountJson, Account::class.java)

                if (accountData.accountId.equals(accountId, ignoreCase = true)) {
                    transactionRealmData.deleteFromRealm()
                }
            }
        }
        realm.close()
    }
}