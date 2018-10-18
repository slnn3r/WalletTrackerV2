package com.example.slnn3r.wallettrackerv2.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class BaseModel {

    fun getSignedInUserFirebase(): FirebaseUser? {
        val mAuth = FirebaseAuth.getInstance()
        return mAuth.currentUser
    }
}