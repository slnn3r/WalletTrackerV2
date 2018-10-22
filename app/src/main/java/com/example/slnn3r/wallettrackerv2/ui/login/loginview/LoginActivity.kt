package com.example.slnn3r.wallettrackerv2.ui.login.loginview

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.login.loginpresenter.LoginPresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginViewInterface.LoginView {

    private val mLoginPresenter: LoginPresenter = LoginPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_google_sign_in.setOnClickListener {
            launchGoogleLoginDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        mLoginPresenter.bindView(this)
        mGoogleSignInClient = mLoginPresenter.getGoogleSignInClient(this)
    }

    override fun onStop() {
        super.onStop()
        mLoginPresenter.unbindView() // Unbind the view when it stopped
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.LOGIN_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(this, message).show()
        return
    }

    override fun showLoadingDialog() {
        progressDialog = ProgressDialog.show(this, null
                , getString(R.string.sign_in_loading))    }

    override fun dismissLoadingDialog() {
        progressDialog.dismiss()
    }

    override fun signInSuccess(userFirebase: FirebaseUser) {

        Toast.makeText(this,
                getString(R.string.sign_in_success_message, userFirebase.displayName)
                , Toast.LENGTH_SHORT).show()

        val intent = Intent(applicationContext, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mLoginPresenter.executeGoogleSignIn(this, requestCode, resultCode, data!!)
    }

    private fun launchGoogleLoginDialog() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Constant.GoogleLoginApi.REQUEST_CODE)
    }
}
