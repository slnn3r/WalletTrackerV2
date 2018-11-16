package com.example.slnn3r.wallettrackerv2.ui.category.categorymodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import io.realm.Realm
import io.realm.RealmConfiguration

class CreateCategoryViewModel : CategoryModelInterface.CreateCategoryViewModel {

    override fun createCategoryRealm(mContext: Context, categoryData: Category) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.CATEGORY_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val categoryRealm = realm.createObject(CategoryRealm::class.java, categoryData.categoryId)

            categoryRealm.categoryName = categoryData.categoryName
            categoryRealm.categoryType = categoryData.categoryType
            categoryRealm.categoryStatus = categoryData.categoryStatus
            categoryRealm.userUid = categoryData.userUid
        }
        realm.close()
    }
}