package com.example.slnn3r.wallettrackerv2.base

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseUser
import java.lang.ref.WeakReference


open class BasePresenter<V : BaseView.Universal> {

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

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken, 0)
    }
}