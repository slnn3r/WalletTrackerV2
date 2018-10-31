package com.example.slnn3r.wallettrackerv2.data.realmclass

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class AccountRealm : RealmObject() {

    @PrimaryKey
    var accountId: String? = null
    var accountName: String? = null
    var accountInitialBalance: Double = 0.0
    var userUid: String? = null
    var accountStatus: String? = null
}
