package com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.account.accountmodel.CreateAccountViewModel
import com.example.slnn3r.wallettrackerv2.ui.account.accountview.AccountViewInterface
import com.example.slnn3r.wallettrackerv2.util.InputValidation

class CreateAccountPresenter : AccountPresenterInterface.CreateAccountPresenter,
        BasePresenter<AccountViewInterface.CreateAccountView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mCreateAccountModel: CreateAccountViewModel = CreateAccountViewModel()
    private val validation = InputValidation()

    override fun validateAccountNameInput(mContext: Context, userUid: String,
                                          accountNameInput: String, updateAccountId: String?) {
        val accountList = baseModel.getAccListByUserUidSync(mContext, userUid)
        val errorMessage = validation.accountNameValidation(mContext, accountNameInput,
                accountList, updateAccountId)

        if (errorMessage != null) {
            getView()!!.invalidAccountNameInput(errorMessage)
            getView()!!.hideFloatingButton()
        } else {
            getView()!!.validAccountNameInput()
        }
    }

    override fun validateAccountDescInput(mContext: Context, accountBalanceInput: String) {
        val errorMessage =
                validation.accountDescValidation(mContext, accountBalanceInput)

        if (errorMessage != null) {
            getView()!!.invalidAccountBalanceInput(errorMessage)
            getView()!!.hideFloatingButton()
        } else {
            getView()!!.validAccountBalanceInput()
        }
    }

    override fun checkAllInputError(errorAccountName: CharSequence?,
                                    errorAccountBalance: CharSequence?) {
        if (errorAccountName == null && errorAccountBalance == null) {
            getView()!!.showFloatingButton()
        }
    }

    override fun createAccount(mContext: Context, accountData: Account) {
        try {
            mCreateAccountModel.createAccountRealm(mContext, accountData)
            getView()!!.createAccountSuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}