package com.example.slnn3r.wallettrackerv2.base

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration

class BaseModel {

    fun getSignedInUserFirebase(): FirebaseUser? {
        val mAuth = FirebaseAuth.getInstance()
        return mAuth.currentUser
    }

    // Asynchronous
    fun getAccountListByUserUidAsync(mContext: Context, userUid: String):
            Observable<ArrayList<Account>> {

        val realm: Realm?
        val accountList = ArrayList<Account>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name("account.realm")
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val getAccountData = realm.where(AccountRealm::class.java)
                    .equalTo("userUid", userUid)
                    .findAll()

            getAccountData.forEach { dataList ->
                accountList.add(
                        Account(
                                dataList.accountId!!,
                                dataList.accountName!!,
                                dataList.accountInitialBalance,
                                dataList.userUid!!,
                                dataList.accountStatus!!
                        )
                )
            }
        }

        realm.close()
        return Observable.just(accountList)
    }


    // Synchronous
    fun getAccountListByUserUidSync(mContext: Context, userUid: String): ArrayList<Account> {

        val realm: Realm?
        val accountList = ArrayList<Account>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name("account.realm")
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val getAccountData = realm.where(AccountRealm::class.java)
                    .equalTo("userUid", userUid)
                    .findAll()

            getAccountData.forEach { dataList ->
                accountList.add(
                        Account(
                                dataList.accountId!!,
                                dataList.accountName!!,
                                dataList.accountInitialBalance,
                                dataList.userUid!!,
                                dataList.accountStatus!!
                        )
                )
            }
        }

        realm.close()
        return accountList
    }
}