package com.example.slnn3r.wallettrackerv2.base

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

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
                                accountRealmData.accountInitialBalance,
                                accountRealmData.userUid!!,
                                accountRealmData.accountStatus!!
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
                                accountRealmData.accountInitialBalance,
                                accountRealmData.userUid!!,
                                accountRealmData.accountStatus!!
                        )
                )
            }
        }

        realm.close()
        return accountList
    }

    fun getCategoryListByUserUidWithFilterSync(mContext: Context, userUid: String,
                                                        filterType: String): ArrayList<Category> {

        val userUidRef = Constant.RealmVariableName.USER_UID_VARIABLE
        val categoryTypeRef = Constant.RealmVariableName.CATEGORY_TYPE_VARIABLE

        val realm: Realm?
        val categoryList = ArrayList<Category>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.CATEGORY_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {

            val categoryRealm: RealmResults<CategoryRealm>?
            val incomeType = Constant.ConditionalKeyword.INCOME_STATUS
            val expenseType = Constant.ConditionalKeyword.EXPENSE_STATUS

            categoryRealm =
                    when (filterType) {
                        incomeType -> {
                            realm.where(CategoryRealm::class.java)
                                    .equalTo(userUidRef, userUid)
                                    .equalTo(categoryTypeRef, incomeType)
                                    .findAll()
                        }
                        expenseType -> {
                            realm.where(CategoryRealm::class.java)
                                    .equalTo(userUidRef, userUid)
                                    .equalTo(categoryTypeRef, expenseType)
                                    .findAll()
                        }
                        else -> {
                            realm.where(CategoryRealm::class.java)
                                    .equalTo(userUidRef, userUid)
                                    .findAll()
                        }
                    }

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

}