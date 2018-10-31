package com.example.slnn3r.wallettrackerv2.ui.account.accountview

import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account

interface AccountViewInterface {

    interface ViewAccountView : BaseView.Universal {
        fun populateAccountRecycleView(accountList: ArrayList<Account>)
    }

    interface CreateAccountView : BaseView.Universal {

        fun validAccountNameInput()
        fun invalidAccountNameInput(errorMessage: String)

        fun validAccountBalanceInput()
        fun invalidAccountBalanceInput(errorMessage: String)

        fun setTwoDecimalPlace()

        fun showFloatingButton()
        fun hideFloatingButton()

        fun createAccountSuccess()
    }

    interface DetailsAccountView : BaseView.Universal {

        fun setupFloatingActionButton()
        fun setupFloatingDefaultButton()

        fun validAccountNameInput()
        fun invalidAccountNameInput(errorMessage: String)

        fun validAccountBalanceInput()
        fun invalidAccountBalanceInput(errorMessage: String)

        fun setTwoDecimalPlace()

        fun showFloatingButton()
        fun hideFloatingButton()

        fun editAccountPrompt()
        fun deleteAccountPrompt()

        fun editAccountSuccess()
        fun deleteAccountSuccess()
    }
}