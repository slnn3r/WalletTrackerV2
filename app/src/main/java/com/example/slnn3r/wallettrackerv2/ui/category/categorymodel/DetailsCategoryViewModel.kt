package com.example.slnn3r.wallettrackerv2.ui.category.categorymodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.realmclass.CategoryRealm
import io.realm.Realm
import io.realm.RealmConfiguration

class DetailsCategoryViewModel : CategoryModelInterface.DetailsCategoryViewModel {

    override fun editCategoryRealm(mContext: Context, categoryData: Category) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.CATEGORY_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val categoryRealm = realm.where(CategoryRealm::class.java)
                    .equalTo(Constant.RealmVariableName.CATEGORY_ID_VARIABLE,
                            categoryData.categoryId).findAll()

            categoryRealm.forEach { categoryRealmData ->
                categoryRealmData.categoryName = categoryData.categoryName
                categoryRealmData.categoryType = categoryData.categoryType
            }
        }
        realm.close()
    }

    override fun deleteCategoryRealm(mContext: Context, categoryId: String) {
        val realm: Realm?
        Realm.init(mContext)

        val config = RealmConfiguration.Builder()
                .name(Constant.RealmTableName.CATEGORY_REALM_TABLE)
                .build()

        realm = Realm.getInstance(config)

        realm!!.executeTransaction {
            val categoryRealm = realm.where(CategoryRealm::class.java)
                    .equalTo(Constant.RealmVariableName.CATEGORY_ID_VARIABLE,
                            categoryId).findAll()

            categoryRealm.forEach { categoryRealmData ->
                categoryRealmData.deleteFromRealm()
            }
        }
        realm.close()
    }
}