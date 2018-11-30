package com.example.slnn3r.wallettrackerv2.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.PreviousRemark
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.PreviousRemarkRealm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

class BaseModel {

    fun getSignedInUserFirebase(): FirebaseUser? {
        val mAuth = FirebaseAuth.getInstance()
        return mAuth.currentUser
    }

    // Synchronous
    fun getAccListByUserUidSync(mContext: Context, userUid: String): ArrayList<Account> {
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

    fun getCatListByUserUidWithFilterSync(mContext: Context, userUid: String,
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

    fun getRemarkRealm(mContext: Context): ArrayList<PreviousRemark> {
        val realm: Realm?
        val previousRemarkList = ArrayList<PreviousRemark>()

        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.PREVIOUSREMARK_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val previousRemarkRealm = realm.where(PreviousRemarkRealm::class.java)
                    .findAll()

            previousRemarkRealm.forEach { previousRemarkRealmData ->
                previousRemarkList.add(PreviousRemark(previousRemarkRealmData.remarkString!!))
            }
        }
        realm.close()
        return previousRemarkList
    }

    fun saveRemarkRealm(mContext: Context, remarkString: String) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.PREVIOUSREMARK_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val creating = realm.createObject(PreviousRemarkRealm::class.java)
            creating.remarkString = remarkString
        }
        realm.close()
    }

    // SharePreference
    fun getSelectedAccountSharePreference(mContext: Context, userUid: String): String {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE)
        return editor.getString(Constant.KeyId.SELECTED_ACCOUNT_KEY, "")!!
    }

    fun saveSelectedAccountSharePreference(mContext: Context, selectedAccount: String, userUid: String) {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString(Constant.KeyId.SELECTED_ACCOUNT_KEY, selectedAccount)
        editor.apply()
        editor.commit()
    }

    fun getBackupSettingSharePreference(mContext: Context, userUid: String): Boolean {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE)
        return editor.getBoolean(Constant.KeyId.BACKUP_SETTING_KEY, true)
    }

    fun saveBackupSettingSharePreference(mContext: Context, userUid: String, backupSetting: Boolean) {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE).edit()
        editor.putBoolean(Constant.KeyId.BACKUP_SETTING_KEY, backupSetting)
        editor.apply()
        editor.commit()
    }

    fun getBackupTypeSharePreference(mContext: Context, userUid: String): String {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE)
        return editor.getString(Constant.KeyId.BACKUP_TYPE_KEY, "")!!
    }

    fun saveBackTypeSharePreference(mContext: Context, userUid: String, backupType: String) {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString(Constant.KeyId.BACKUP_TYPE_KEY, backupType)
        editor.apply()
        editor.commit()
    }

    fun getBackupDateTimeSharePreference(mContext: Context, userUid: String): String {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE)
        return editor.getString(Constant.KeyId.BACKUP_DATETIME_KEY, "")!!
    }

    fun saveBackupDateTimeSharePreference(mContext: Context, userUid: String, backupDateTime: String) {
        val editor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE).edit()
        editor.putString(Constant.KeyId.BACKUP_DATETIME_KEY, backupDateTime)
        editor.apply()
        editor.commit()
    }

    fun removeUniversalSharePreference(mContext: Context, userUid: String) {
        val sharePrefEditor = mContext.getSharedPreferences(Constant.KeyId.SHARE_PREF + userUid,
                AppCompatActivity.MODE_PRIVATE).edit()
        sharePrefEditor.clear()
        sharePrefEditor.apply()
        sharePrefEditor.commit()
    }

    fun removeFilterInputSharePreference(mContext: Context) {
        val filterInputEditor = mContext.getSharedPreferences(Constant.KeyId.FILTER_INPUT_SHARE_PREF,
                AppCompatActivity.MODE_PRIVATE).edit()
        filterInputEditor.clear()
        filterInputEditor.apply()
        filterInputEditor.commit()
    }
}