package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.defaultdata.CategoryDefaultData
import com.example.slnn3r.wallettrackerv2.data.realmclass.AccountRealm
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*

class DashboardViewModel: DashboardModelInterface.DashboardViewModel {

    override fun firstTimeSetupRealm(mContext: Context, userUid: String) {
        val uniqueAccountId = UUID.randomUUID().toString()
        val defaultAccountName = "Personal"
        val defaultAccountBalance = 0.0
        val defaultAccountStatus = "Default"

        var realm: Realm?
        Realm.init(mContext)

        val accountTableConfig = RealmConfiguration.Builder()
                .name("account.realm")
                .build()

        realm = Realm.getInstance(accountTableConfig)

        realm!!.executeTransaction {

            val creating = realm!!.createObject(AccountRealm::class.java, uniqueAccountId)

            creating.accountName = defaultAccountName
            creating.accountInitialBalance = defaultAccountBalance
            creating.userUid = userUid
            creating.accountStatus = defaultAccountStatus
        }

        realm.close()


        val categoryTableConfig = RealmConfiguration.Builder()
                .name("category.realm")
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

}