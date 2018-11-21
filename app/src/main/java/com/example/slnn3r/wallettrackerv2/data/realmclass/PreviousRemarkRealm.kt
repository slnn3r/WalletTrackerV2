package com.example.slnn3r.wallettrackerv2.data.realmclass

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class PreviousRemarkRealm : RealmObject() {
    var remarkString: String? = null
}