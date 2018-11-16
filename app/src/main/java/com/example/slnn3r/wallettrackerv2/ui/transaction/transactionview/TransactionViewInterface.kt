package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview

import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category

interface TransactionViewInterface {

    interface CreateTransactionView : BaseView.Universal {
        fun switchButtonExpenseMode()
        fun switchButtonIncomeMode()

        fun populateAccountSpinner(accountList: ArrayList<Account>)
        fun populateCategorySpinner(categoryList: ArrayList<Category>)

        fun createTransactionSuccess()
    }

    interface DetailsTransactionView : BaseView.Universal {

        fun switchButtonToggle()
        fun initialExpenseMode()

        fun switchButtonExpenseMode()
        fun switchButtonIncomeMode()

        fun selectCategorySpinnerData(spinnerPosition: Int)
        fun selectDeletedCategorySpinnerData(spinnerPosition: Int)

        fun populateAccountSpinner(accountList: ArrayList<Account>)
        fun populateCategorySpinner(categoryList: ArrayList<Category>)

        fun editTransactionPrompt()
        fun deleteTransactionPrompt()

        fun editTransactionSuccess()
        fun deleteTransactionSuccess()
    }
}