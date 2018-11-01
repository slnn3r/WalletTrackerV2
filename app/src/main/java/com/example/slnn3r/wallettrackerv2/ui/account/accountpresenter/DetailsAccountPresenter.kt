package com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.account.accountmodel.DetailsAccountViewModel
import com.example.slnn3r.wallettrackerv2.ui.account.accountview.AccountViewInterface
import com.example.slnn3r.wallettrackerv2.util.InputValidation
import com.leinardi.android.speeddial.SpeedDialActionItem

class DetailsAccountPresenter : AccountPresenterInterface.DetailsAccountPresenter,
        BasePresenter<AccountViewInterface.DetailsAccountView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mDetailsAccountModel: DetailsAccountViewModel = DetailsAccountViewModel()
    private val validation = InputValidation()

    override fun checkAccountStatus(accountStatus: String) {
        if (accountStatus == Constant.ConditionalKeyword.DEFAULT_STATUS) {
            getView()!!.setupFloatingDefaultButton()
        } else {
            getView()!!.setupFloatingActionButton()
        }
    }

    override fun validateAccountNameInput(mContext: Context, userUid: String,
                                          accountNameInput: String, updateAccountId: String?) {

        val accountList = baseModel.getAccountListByUserUidSync(mContext, userUid)
        val errorMessage = validation.accountNameValidation(mContext, accountNameInput,
                accountList, updateAccountId)

        if (errorMessage != null) {
            getView()!!.invalidAccountNameInput(errorMessage)
            getView()!!.hideFloatingButton()
        } else {
            getView()!!.validAccountNameInput()
        }
    }

    override fun validateAccountBalanceInput(mContext: Context, accountBalanceInput: String) {
        val errorMessage =
                validation.amountValidation(mContext, accountBalanceInput)

        if (errorMessage != null) {
            getView()!!.invalidAccountBalanceInput(errorMessage)
            getView()!!.hideFloatingButton()
        } else {
            getView()!!.validAccountBalanceInput()
        }
    }

    override fun decimalInputCheck(accountBalanceInput: String) {
        if (accountBalanceInput.contains(".") &&
                accountBalanceInput.substring(
                        accountBalanceInput.indexOf(".") + 1).length > 2) {
            getView()!!.setTwoDecimalPlace()
        }
    }

    override fun actionCheck(menuItem: SpeedDialActionItem) {

        when (menuItem.id) {
            R.id.fb_action_edit -> {
                getView()!!.editAccountPrompt()
            }
            R.id.fb_action_delete -> {
                getView()!!.deleteAccountPrompt()
            }
        }
    }

    override fun editAccount(mContext: Context, accountData: Account) {
        try {
            mDetailsAccountModel.editAccountRealm(mContext, accountData)
            getView()!!.editAccountSuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun deleteAccount(mContext: Context, accountData: Account) {
        try {
            mDetailsAccountModel.deleteAccountRealm(mContext, accountData.accountId)
            getView()!!.deleteAccountSuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun checkAllInputError(errorAccountName: CharSequence?,
                                    errorAccountBalance: CharSequence?) {
        if (errorAccountName == null && errorAccountBalance == null) {
            getView()!!.showFloatingButton()
        }
    }
}