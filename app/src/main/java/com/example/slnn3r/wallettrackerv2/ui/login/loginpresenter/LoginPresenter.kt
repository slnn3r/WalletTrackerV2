package com.example.slnn3r.wallettrackerv2.ui.login.loginpresenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.slnn3r.wallettrackermvp.Model.ObjectClass.UserProfile
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.login.loginmodel.LoginModelInterface
import com.example.slnn3r.wallettrackerv2.ui.login.loginmodel.LoginSharePreference
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginViewInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson

class LoginPresenter : LoginPresenterInterface.LoginPresenter,
        BasePresenter<LoginViewInterface.LoginView>() {

    private val mSharePreferenceModel: LoginModelInterface.SharePreferenceAccess = LoginSharePreference()

    override fun executeGoogleLogin(mContext: Context, requestCode: Int,
                                    resultCode: Int, data: Intent) {

        if (resultCode != 0) { // Close the Google Sign In Dialog Manually or Unexpectedly will NOT return 0
            if (requestCode == 1) {
                getView()!!.showSignInLoading()
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val accountGoogle = handleSignInResult(task)
                firebaseAuthentication(mContext, accountGoogle)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): GoogleSignInAccount? {

        var account: GoogleSignInAccount? = null

        try {
            account = completedTask.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            getView()!!.onError(e.message.toString())
        }

        return account
    }

    private fun firebaseAuthentication(mContext: Context, accountGoogle: GoogleSignInAccount?) {

        if (accountGoogle == null) {
            return
        }

        val mAuth = FirebaseAuth.getInstance()

        val credential = GoogleAuthProvider.getCredential(accountGoogle.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(mContext as Activity) { task ->

                    if (task.isSuccessful) {
                        val userFirebase = mAuth.currentUser!!
                        getView()!!.signInSuccess(userFirebase)
                    }
                }
                .addOnFailureListener {
                    getView()!!.onError(it.message.toString())
                }
    }

    override fun saveAccToSharePref(mContext: Context, userFirebase: FirebaseUser) {

        val gson = Gson()
        val userProfile = UserProfile(userFirebase.uid, userFirebase.displayName.toString(), userFirebase.email.toString(), userFirebase.photoUrl.toString())
        val jsonUserProfile = gson.toJson(userProfile)

        mSharePreferenceModel.saveAccToSharePrefExecute(mContext, jsonUserProfile)

        getView()!!.saveAccToSharePrefSuccess(userFirebase.displayName!!)
    }
}