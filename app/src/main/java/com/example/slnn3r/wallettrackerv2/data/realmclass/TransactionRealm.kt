package com.example.slnn3r.wallettrackerv2.data.realmclass

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class TransactionRealm : RealmObject() {

    @PrimaryKey
    var transactionId: String? = null
    var transactionDateTime: Long? = null
    var transactionAmount: Double? = 0.0
    var transactionRemark: String? = null
    var category: String? = null
    var account: String? = null
}