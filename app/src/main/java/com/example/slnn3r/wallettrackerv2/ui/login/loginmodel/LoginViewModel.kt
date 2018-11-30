package com.example.slnn3r.wallettrackerv2.ui.login.loginmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.firebaseclass.AccountFirebase
import com.example.slnn3r.wallettrackerv2.data.firebaseclass.CategoryFirebase
import com.example.slnn3r.wallettrackerv2.data.firebaseclass.TransactionFirebase
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.TransactionRealm
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration

class LoginViewModel : LoginModelInterface.LoginViewModel {

    override fun retrieveDataFirebase(mContext: Context, userUid: String) {
        val database = FirebaseDatabase.getInstance()

        val transactionCategoryList = ArrayList<CategoryFirebase>()
        val transactionList = ArrayList<TransactionFirebase>()
        val walletAccountList = ArrayList<AccountFirebase>()

        database.reference.child(Constant.FirebasePushKey.CATEGORY_FIREBASE_KEY)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {

                            val message = dataSnapshot.getValue(CategoryFirebase::class.java)

                            if (message!!.userUid == userUid) {
                                transactionCategoryList.add(dataSnapshot.getValue(CategoryFirebase::class.java)!!)
                            }
                        }

                        syncTransactionCategoryRealm(mContext, userUid, transactionCategoryList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        (mContext as LoginActivity).loadFailed(databaseError.message.toString())
                    }
                })


        database.reference.child(Constant.FirebasePushKey.ACCOUNT_FIREBASE_KEY)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {

                            val message = dataSnapshot.getValue(AccountFirebase::class.java)

                            if (message!!.userUid == userUid) {
                                walletAccountList.add(dataSnapshot.getValue(AccountFirebase::class.java)!!)
                            }
                        }

                        syncWalletAccountRealm(mContext, userUid, walletAccountList)
                        (mContext as LoginActivity).finishLoad() // Load all then trigger this to open to DashBoard
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        (mContext as LoginActivity).loadFailed(databaseError.message.toString())
                    }
                })


        database.reference.child(Constant.FirebasePushKey.TRANSACTION_FIREBASE_KEY)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {

                            val message = dataSnapshot.getValue(TransactionFirebase::class.java)

                            if (message!!.account.userUid == userUid) {
                                transactionList.add(dataSnapshot.getValue(TransactionFirebase::class.java)!!)
                            }
                        }

                        syncTransaction(mContext, userUid, transactionList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        (mContext as LoginActivity).loadFailed(databaseError.message.toString())
                    }
                })
    }

    private fun syncWalletAccountRealm(mainContext: Context, userID: String,
                                       walletAccountList: ArrayList<AccountFirebase>) {

        try {
            var realm: Realm? = null
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(Constant.RealmTableName.ACCOUNT_REALM_TABLE)
                    .build()

            realm = Realm.getInstance(config)

            realm!!.executeTransaction {

                val getWalletAccount = realm.where(AccountRealm::class.java)
                        .equalTo(Constant.RealmVariableName.USER_UID_VARIABLE, userID).findAll()

                getWalletAccount.forEach { dataList ->
                    dataList.deleteFromRealm()
                }

                if (walletAccountList.size != 0) {
                    walletAccountList.forEach { data ->
                        val creating = realm.createObject(AccountRealm::class.java, data.accountId)
                        creating.accountName = data.accountName
                        creating.accountDesc = data.accountDesc
                        creating.accountStatus = data.accountStatus
                        creating.userUid = data.userUid
                    }
                }
            }

            realm.close()

        } catch (e: Exception) {
            (mainContext as LoginActivity).loadFailed(e.message.toString())
        }
    }


    private fun syncTransactionCategoryRealm(mainContext: Context, userID: String,
                                             transactionCategoryList: ArrayList<CategoryFirebase>) {

        try {

            var realm: Realm? = null
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(Constant.RealmTableName.CATEGORY_REALM_TABLE)
                    .build()

            realm = Realm.getInstance(config)

            realm!!.executeTransaction {

                val getTransactionCategory = realm.where(CategoryRealm::class.java)
                        .equalTo(Constant.RealmVariableName.USER_UID_VARIABLE, userID).findAll()

                getTransactionCategory.forEach { dataList ->
                    dataList.deleteFromRealm()
                }

                if (transactionCategoryList.size != 0) {
                    transactionCategoryList.forEach { data ->

                        val creating = realm.createObject(CategoryRealm::class.java, data.categoryId)
                        creating.categoryName = data.categoryName
                        creating.categoryStatus = data.categoryStatus
                        creating.categoryType = data.categoryType
                        creating.userUid = data.userUid
                    }
                }
            }

            realm.close()

        } catch (e: Exception) {
            (mainContext as LoginActivity).loadFailed(e.message.toString())
        }
    }


    private fun syncTransaction(mainContext: Context, userID: String, transactionList: ArrayList<TransactionFirebase>) {

        try {

            var realm: Realm? = null
            Realm.init(mainContext)

            val config = RealmConfiguration.Builder()
                    .name(Constant.RealmTableName.TRANSACTION_REALM_TABLE)
                    .build()

            realm = Realm.getInstance(config)

            realm!!.executeTransaction {

                val getTrx = realm.where(TransactionRealm::class.java).findAll()
                val gson = Gson()

                getTrx.forEach { dataList ->

                    val transactionAccountData = gson.fromJson<AccountFirebase>(dataList.account, AccountFirebase::class.java)

                    if (transactionAccountData.userUid == userID) {
                        dataList.deleteFromRealm()
                    }
                }

                if (transactionList.size != 0) {
                    transactionList.forEach { data ->

                        val convertedCategory = gson.toJson(data.category)
                        val convertedAccount = gson.toJson(data.account)

                        val creating = realm.createObject(TransactionRealm::class.java, data.transactionId)
                        creating.transactionDateTime = data.transactionDateTime
                        creating.transactionAmount = data.transactionAmount
                        creating.transactionRemark = data.transactionRemark
                        creating.category = convertedCategory
                        creating.account = convertedAccount

                    }
                }
            }

            realm.close()

        } catch (e: Exception) {
            (mainContext as LoginActivity).loadFailed(e.message.toString())
        }
    }

}