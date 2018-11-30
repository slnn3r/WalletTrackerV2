package com.example.slnn3r.wallettrackerv2.base

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.example.slnn3r.wallettrackerv2.constant.Constant
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
                activity.currentFocus?.windowToken, 0)
    }

    fun getSelectedAccount(mContext: Context, userUid: String): String {
        return baseModel.getSelectedAccountSharePreference(mContext, userUid)
    }

    fun saveSelectedAccount(mContext: Context, selectedAccount: String, userUid: String) {
        baseModel.saveSelectedAccountSharePreference(mContext, selectedAccount, userUid)
    }

    fun clearSelectedAccountSharePreference(mContext: Context, userUid: String) {
        baseModel.removeUniversalSharePreference(mContext, userUid)
    }

    fun clearFilterInputSharePreference(mContext: Context) {
        baseModel.removeFilterInputSharePreference(mContext)
    }

    fun getRemark(mContext: Context): ArrayList<String> {
        val stringList = ArrayList<String>()

        try {
            val previousRemarkList = baseModel.getRemarkRealm(mContext)

            previousRemarkList.forEach { data ->
                stringList.add(data.remarkString)
            }
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
        return stringList
    }

    fun saveRemark(mContext: Context, remarkString: String) {
        try {
            val previousRemarkList = baseModel.getRemarkRealm(mContext)

            if (previousRemarkList.size > 0) {
                var matchCount = 0
                previousRemarkList.forEach { data ->
                    if (data.remarkString.equals(remarkString, ignoreCase = true)) {
                        matchCount += 1
                    }
                }

                if (matchCount < 1) {
                    baseModel.saveRemarkRealm(mContext, remarkString)
                }
            } else {
                baseModel.saveRemarkRealm(mContext, remarkString)
            }
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}