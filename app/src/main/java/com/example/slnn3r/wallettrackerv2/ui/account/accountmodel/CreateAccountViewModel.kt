package com.example.slnn3r.wallettrackerv2.ui.account.accountmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import io.realm.Realm
import io.realm.RealmConfiguration

class CreateAccountViewModel : AccountModelInterface.CreateAccountViewModel {

    override fun createAccountRealm(mContext: Context, accountData: Account) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.ACCOUNT_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val accountRealm = realm.createObject(AccountRealm::class.java, accountData.accountId)

            accountRealm.accountName = accountData.accountName
            accountRealm.accountInitialBalance = accountData.accountInitialBalance
            accountRealm.userUid = accountData.userUid
            accountRealm.accountStatus = accountData.accountStatus
        }

        realm.close()
    }
}