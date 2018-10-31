package com.example.slnn3r.wallettrackerv2.util

import com.example.slnn3r.wallettrackerv2.data.objectclass.Account

class InputValidation {

    fun accountNameValidation(accountNameInput: String, accountList: ArrayList<Account>, updateAccountId: String?) : String?{

        val errorMessage: String?

        val rex = "^[a-zA-Z]+(?: [a-zA-Z]+){0,3}\$".toRegex()

        if(accountNameInput.isEmpty()){
            errorMessage = "Please Enter Account Name"
        } else if (accountNameInput.length < 4) {
            errorMessage = "Min 4, Max 20 Words"

        } else if (!accountNameInput.matches(rex)) {
            errorMessage = "Invalid Input with Symbols"

        } else if (accountList.size > 0) {

            var detectMatched = 0

            if (updateAccountId == null) {
                accountList.forEach { data ->
                    if (data.accountName.equals(accountNameInput, ignoreCase = true)) {
                        detectMatched += 1
                    }
                }
            } else {
                accountList.forEach { data ->
                    if (data.accountName.equals(accountNameInput, ignoreCase = true) && data.accountId != updateAccountId) {
                        detectMatched += 1
                    }
                }
            }

            errorMessage = if (detectMatched > 0) {
                "Account Name already Used"
            } else {
                null
            }

        } else if (accountList.size == 0) { //when retrieve nothing database error

            errorMessage = "Database error occurred"

        } else {
            errorMessage = null
        }

        return errorMessage
    }

    fun amountValidation(accountBalanceInput: String): String?{

        return if (accountBalanceInput.isEmpty()) {
            "Please Enter Initial Balance"
        } else {
            null
        }
    }
}