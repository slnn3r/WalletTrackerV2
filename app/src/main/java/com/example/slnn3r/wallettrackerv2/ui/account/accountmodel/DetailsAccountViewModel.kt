package com.example.slnn3r.wallettrackerv2.ui.account.accountmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
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
                accountRealmData.accountInitialBalance = accountData.accountInitialBalance
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
    }
}