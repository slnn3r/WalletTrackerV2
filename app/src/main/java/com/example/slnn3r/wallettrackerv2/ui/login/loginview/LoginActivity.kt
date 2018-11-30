package com.example.slnn3r.wallettrackerv2.ui.login.loginview

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.login.loginpresenter.LoginViewPresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginViewInterface.LoginView {

    private val mLoginViewPresenter: LoginViewPresenter = LoginViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupSignInButton()
    }

    override fun onStart() {
        super.onStart()
        mLoginViewPresenter.bindView(this)
        mGoogleSignInClient = mLoginViewPresenter.getGoogleSignInClient(this)
    }

    override fun onStop() {
        super.onStop()
        mLoginViewPresenter.unbindView() // Unbind the view when it stopped
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.LOGIN_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(this, message).show()
        return
    }

    override fun showLoadingDialog(loadingMessage: String) {
        progressDialog = ProgressDialog.show(this, null
                , loadingMessage)
    }

    override fun dismissLoadingDialog() {
        progressDialog.dismiss()
    }

    override fun signInSuccess() {

        val userData = mLoginViewPresenter.getSignedInUser()!!
        mLoginViewPresenter.retrieveData(this, userData.uid) //Sync now

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mLoginViewPresenter.executeGoogleSignIn(this, requestCode, resultCode, data!!)
    }

    private fun setupSignInButton() {
        btn_google_sign_in.setOnClickListener {
            launchGoogleLoginDialog()
        }
    }

    private fun launchGoogleLoginDialog() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constant.GoogleLoginApi.REQUEST_CODE)
    }

    fun finishLoad() {
        progressDialog.dismiss()
        val intent = Intent(applicationContext, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun loadFailed(message: String) {
        Log.e(Constant.LoggingTag.LOGIN_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(this, message).show()

        // Logout here
        return
    }
}
