package com.example.slnn3r.wallettrackerv2.data.realmclass

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class CategoryRealm : RealmObject() {

    @PrimaryKey
    var categoryId: String? = null
    var categoryName: String? = null
    var categoryType: String? = null
    var categoryStatus: String? = null
    var userUid: String? = null
}