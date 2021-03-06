package com.example.slnn3r.wallettrackerv2.ui.login.loginview

import com.example.slnn3r.wallettrackerv2.base.BaseView

interface LoginViewInterface {

    interface LoginView : BaseView.Universal {
        fun signInSuccess()
        fun showLoadingDialog(loadingMessage: String)
        fun dismissLoadingDialog()
    }
}