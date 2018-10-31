package com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.leinardi.android.speeddial.SpeedDialActionItem

interface AccountPresenterInterface {

    interface ViewAccountPresenter {
        fun getAllAccountData(mContext: Context, userUid: String)
    }

    interface CreateAccountPresenter {
        fun validateAccountNameInput(mContext: Context, userUid: String,
                                     accountNameInput: String, updateAccountId: String?)

        fun validateAccountBalanceInput(accountBalanceInput: String)
        fun decimalInputCheck(accountBalanceInput: String)
        fun checkAllInputError(errorAccountName: CharSequence?, errorAccountBalance: CharSequence?)
        fun createAccount(mContext: Context, accountData: Account)
    }

    interface DetailsAccountPresenter {
        fun checkAccountStatus(accountStatus: String)
        fun validateAccountNameInput(mContext: Context, userUid: String,
                                     accountNameInput: String, updateAccountId: String?)

        fun validateAccountBalanceInput(accountBalanceInput: String)
        fun decimalInputCheck(accountBalanceInput: String)

        fun actionCheck(menuItem: SpeedDialActionItem)
        fun checkAllInputError(errorAccountName: CharSequence?, errorAccountBalance: CharSequence?)

        fun editAccount(mContext: Context, accountData: Account,
                        errorAccountName: CharSequence?, errorAccountBalance: CharSequence?)

        fun deleteAccount(mContext: Context, accountData: Account)
    }
}