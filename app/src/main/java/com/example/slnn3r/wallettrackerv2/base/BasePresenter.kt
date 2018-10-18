package com.example.slnn3r.wallettrackerv2.base

import android.content.Context
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import java.lang.ref.WeakReference

open class BasePresenter<V : BaseView> {

    lateinit var view: WeakReference<V>
    private val baseModel: BaseModel = BaseModel()

    fun bindView(view: V) {
        this.view = WeakReference(view)
    }

    fun unbindView() {
        this.view.clear()
    }

    fun getView(): V? {
        return this.view.get()
    }

    fun getGoogleSignInClient(mContext: Context): GoogleSignInClient {
        val gso = // Never return null
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(Constant.GoogleLoginApi.REQUEST_ID_TOKEN)
                        .requestEmail()
                        .build()

        return GoogleSignIn.getClient(mContext, gso)
    }

    fun getSignedInUser(): FirebaseUser? {
        return baseModel.getSignedInUserFirebase()
    }
}