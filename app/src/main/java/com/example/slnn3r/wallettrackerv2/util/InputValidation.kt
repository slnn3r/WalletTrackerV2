package com.example.slnn3r.wallettrackerv2.util

import android.content.Context
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category

class InputValidation {

    fun accountNameValidation(mContext: Context, accountNameInput: String,
                              accountList: ArrayList<Account>, updateAccountId: String?): String? {
        val errorMessage: String?
        val rex = Constant.RegularExpression.REX_NAME.toRegex()

        if (accountNameInput.isEmpty()) {
            errorMessage = mContext.getString(R.string.accName_empty_invalid_message)
        } else if (accountNameInput.length < Constant.ConditionalFigure.MIN_ACCOUNT_NAME) {
            errorMessage = mContext.getString(R.string.accName_minWord_invalid_message)
        } else if (!accountNameInput.matches(rex)) {
            errorMessage = mContext.getString(R.string.accName_format_invalid_messagae)
        } else if (accountList.size > 0) {
            var detectMatched = 0

            if (updateAccountId == null) {
                accountList.forEach { accountData ->
                    if (accountData.accountName.equals(accountNameInput, ignoreCase = true)) {
                        detectMatched += 1
                    }
                }
            } else {
                accountList.forEach { accountData ->
                    if (accountData.accountName.equals(accountNameInput, ignoreCase = true)
                            && accountData.accountId != updateAccountId) {
                        detectMatched += 1
                    }
                }
            }

            errorMessage = if (detectMatched > 0) {
                mContext.getString(R.string.accName_used_invalid_message)
            } else {
                null
            }

        } else if (accountList.size == 0) { // when retrieve nothing database error
            errorMessage = mContext.getString(R.string.accName_databaseError_invalid_message)
        } else {
            errorMessage = null
        }

        return errorMessage
    }

    fun accountDescValidation(mContext: Context, accountDescInput: String): String? {
        return if (accountDescInput.isEmpty()) {
            mContext.getString(R.string.accDescription_empty_invalid_message)
        } else {
            null
        }
    }

    fun categoryNameValidation(mContext: Context, categoryNameInput: String,
                               categoryList: ArrayList<Category>,
                               updateCategoryId: String?): String? {
        val errorMessage: String?
        val rex = Constant.RegularExpression.REX_NAME.toRegex()

        if (categoryNameInput.isEmpty()) {
            errorMessage = mContext.getString(R.string.catName_empty_invalid_message)
        } else if (categoryNameInput.length < Constant.ConditionalFigure.MIN_ACCOUNT_NAME) {
            errorMessage = mContext.getString(R.string.catName_minWord_invalid_message)
        } else if (!categoryNameInput.matches(rex)) {
            errorMessage = mContext.getString(R.string.catName_format_invalid_messagae)
        } else if (categoryList.size > 0) {
            var detectMatched = 0

            if (updateCategoryId == null) {
                categoryList.forEach { categoryData ->
                    if (categoryData.categoryName.equals(categoryNameInput, ignoreCase = true)) {
                        detectMatched += 1
                    }
                }
            } else {
                categoryList.forEach { categoryData ->
                    if (categoryData.categoryName.equals(categoryNameInput, ignoreCase = true)
                            && categoryData.categoryId != updateCategoryId) {
                        detectMatched += 1
                    }
                }
            }

            errorMessage = if (detectMatched > 0) {
                mContext.getString(R.string.catName_used_invalid_message)
            } else {
                null
            }

        } else if (categoryList.size == 0) { // when retrieve nothing database error
            errorMessage = mContext.getString(R.string.catName_databaseError_invalid_message)
        } else {
            errorMessage = null
        }

        return errorMessage
    }
}