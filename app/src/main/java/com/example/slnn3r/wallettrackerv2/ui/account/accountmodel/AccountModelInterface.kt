package com.example.slnn3r.wallettrackerv2.ui.account.accountmodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account

interface AccountModelInterface {

    interface CreateAccountViewModel {
        fun createAccountRealm(mContext: Context, accountData: Account)
    }

    interface DetailsAccountViewModel {
        fun editAccountRealm(mContext: Context, accountData: Account)
        fun deleteAccountRealm(mContext: Context, accountId: String)
    }
}