package com.example.slnn3r.wallettrackerv2.ui.login.loginpresenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.login.loginmodel.LoginViewModel
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginViewInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewPresenter : LoginPresenterInterface.LoginViewPresenter,
        BasePresenter<LoginViewInterface.LoginView>() {

    private val mLoginViewModel: LoginViewModel = LoginViewModel()

    override fun executeGoogleSignIn(mContext: Context, requestCode: Int,
                                     resultCode: Int, data: Intent) {
        if (resultCode != 0) { // Close the Google Sign In Dialog Manually or Unexpectedly will NOT return 0
            if (requestCode == 1) {
                getView()!!.showLoadingDialog(mContext.getString(R.string.sign_in_loading_message))
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val accountGoogle = handleSignInResult(task)
                firebaseAuthentication(mContext, accountGoogle)
            }
        } else {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

            if (!isConnected) {
                getView()!!.onError(mContext.getString(R.string.no_internet_message))
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): GoogleSignInAccount? {
        var account: GoogleSignInAccount? = null

        try {
            account = completedTask.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            getView()!!.dismissLoadingDialog()
            getView()!!.onError(e.message.toString())
        }

        return account
    }

    private fun firebaseAuthentication(mContext: Context, accountGoogle: GoogleSignInAccount?) {
        if (accountGoogle == null) { // might null as handleSignInResult catch exception
            return
        }

        val mAuth = FirebaseAuth.getInstance()

        val credential = GoogleAuthProvider.getCredential(accountGoogle.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(mContext as Activity) { task ->
                    if (task.isSuccessful) {
                        getView()!!.dismissLoadingDialog()
                        getView()!!.signInSuccess()
                    }
                }
                .addOnFailureListener {
                    getView()!!.dismissLoadingDialog()
                    getView()!!.onError(it.message.toString())
                }
    }

    override fun retrieveData(mContext: Context, userUid: String) {
        getView()!!.showLoadingDialog(mContext.getString(R.string.sync_loading_message))
        mLoginViewModel.retrieveDataFirebase(mContext, userUid)
    }

    override fun forceSignOut(mGoogleSignInClient: GoogleSignInClient) {
        try {
            FirebaseAuth.getInstance().signOut()
        } catch (error: Exception) {
            getView()!!.onError(error.toString())
        }

        mGoogleSignInClient.revokeAccess()
                .addOnFailureListener {
                    getView()!!.onError(it.message.toString())
                    return@addOnFailureListener
                }

        mGoogleSignInClient.signOut()
                .addOnFailureListener {
                    getView()!!.onError(it.message.toString())
                    return@addOnFailureListener
                }
    }
}