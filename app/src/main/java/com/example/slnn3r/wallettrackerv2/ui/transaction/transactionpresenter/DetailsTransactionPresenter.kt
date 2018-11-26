package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionpresenter

import android.content.Context
import android.widget.ArrayAdapter
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionmodel.DetailsTransactionViewModel
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview.TransactionViewInterface
import com.leinardi.android.speeddial.SpeedDialActionItem
import java.util.*

class DetailsTransactionPresenter : TransactionPresenterInterface.DetailsTransactionPresenter,
        BasePresenter<TransactionViewInterface.DetailsTransactionView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mDetailsTransactionModel: DetailsTransactionViewModel = DetailsTransactionViewModel()

    override fun checkSwitchButton(isChecked: Boolean) {
        if (isChecked) {
            getView()!!.switchButtonExpenseMode()
        } else {
            getView()!!.switchButtonIncomeMode()
        }
    }

    override fun checkSelectedCategoryType(filterType: String) {
        if (filterType.equals(Constant.ConditionalKeyword.EXPENSE_STATUS, ignoreCase = true)) {
            getView()!!.switchButtonExpenseMode()
            getView()!!.initialExpenseMode()
        } else {
            getView()!!.switchButtonToggle()
            getView()!!.switchButtonIncomeMode()
        }
    }

    override fun checkCategoryData(categoryList: ArrayList<Category>,
                                   transactionArgData: Transaction,
                                   dataAdapter: ArrayAdapter<String>,
                                   categoryNameList: ArrayList<String>, initialLaunch: Boolean) {
        // Detect Category Name if Deleted or not, then make spinner select the Category Name
        // Check if the ID exist or not, If Exist spinner item will refer to it, else, add this selection indicated Deleted
        var tempCategoryRef = ""
        val categoryNameSelection = transactionArgData.category

        // if category object is exist overwrite data to Category Name
        categoryList.forEach { data ->
            if (categoryNameSelection.categoryId == data.categoryId) {
                tempCategoryRef = data.categoryName
            }
        }

        // return -1 if detect category object is not existed as it is ""
        val spinnerPosition = dataAdapter.getPosition(tempCategoryRef)
        val categoryItemSize = categoryNameList.size

        if (spinnerPosition < 0 && !initialLaunch) {
            categoryNameList[categoryItemSize - 1] =
                    categoryNameSelection.categoryName + categoryNameList[categoryItemSize - 1]
            getView()!!.selectDeletedCategorySpinnerData(categoryItemSize - 1)
        } else {
            categoryNameList.removeAt(categoryItemSize - 1) // filter out (Deleted Item) selection as category object is existed
            getView()!!.selectCategorySpinnerData(spinnerPosition)
        }
    }

    override fun actionCheck(menuItem: SpeedDialActionItem) {
        when (menuItem.id) {
            R.id.fb_action_edit -> {
                getView()!!.editTransactionPrompt()
            }
            R.id.fb_action_delete -> {
                getView()!!.deleteTransactionPrompt()
            }
        }
    }

    override fun getAccountList(mContext: Context, userUid: String) {
        try {
            val dataList = baseModel.getAccListByUserUidSync(mContext, userUid)
            getView()!!.populateAccountSpinner(dataList)
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun getCategoryList(mContext: Context, userUid: String, filterType: String) {
        try {
            val categoryList = baseModel.getCatListByUserUidWithFilterSync(
                    mContext, userUid, filterType)
            getView()!!.populateCategorySpinner(categoryList)
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun editTransaction(mContext: Context, transactionData: Transaction, userUid: String,
                                 selectedAccount: String, selectedCategory: String,
                                 accountList: ArrayList<Account>, categoryList: ArrayList<Category>,
                                 initialTransactionData: Transaction) {
        try {
            var accountData: Account = initialTransactionData.account
            var categoryData: Category = initialTransactionData.category

            // Detect the newly Selected Account/Category and Overwrite the initial data to the updated(newly selected) one
            accountList.forEach { data ->
                // 100% will detect and overwrite even though detect same Account Data (no affect)
                if (data.accountName.equals(selectedAccount, ignoreCase = true)) {
                    accountData = data
                }
            }

            categoryList.forEach { data ->
                // Possible wont detect and overwrite because there might have Deleted Category Object data so will use back initialized data
                if (data.categoryName.equals(selectedCategory, ignoreCase = true)) {
                    categoryData = data
                }
            }

            val finalizedTransactionData =
                    Transaction(transactionData.transactionId,
                            transactionData.transactionDateTime,
                            transactionData.transactionAmount,
                            transactionData.transactionRemark,
                            categoryData,
                            accountData
                    )

            mDetailsTransactionModel.editCategoryRealm(mContext, finalizedTransactionData)
            getView()!!.editTransactionSuccess()

        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun deleteTransaction(mContext: Context, transactionId: String) {
        try {
            mDetailsTransactionModel.deleteCategoryRealm(mContext, transactionId)
            getView()!!.deleteTransactionSuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}