package com.example.slnn3r.wallettrackerv2.ui.category.categorypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.ui.category.categorymodel.DetailsCategoryViewModel
import com.example.slnn3r.wallettrackerv2.ui.category.categoryview.CategoryViewInterface
import com.example.slnn3r.wallettrackerv2.util.InputValidation
import com.leinardi.android.speeddial.SpeedDialActionItem

class DetailsCategoryPresenter : CategoryPresenterInterface.DetailsCategoryPresenter,
        BasePresenter<CategoryViewInterface.DetailsCategoryView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mDetailsAccountModel: DetailsCategoryViewModel = DetailsCategoryViewModel()
    private val validation = InputValidation()

    override fun checkCategoryStatus(categoryStatus: String) {
        if (categoryStatus == Constant.ConditionalKeyword.DEFAULT_STATUS) {
            getView()!!.setupFloatingDefaultButton()
        } else {
            getView()!!.setupFloatingActionButton()
        }
    }

    override fun checkSelectedCategoryType(filterType: String) {
        if (filterType.equals(Constant.ConditionalKeyword.EXPENSE_STATUS, ignoreCase = true)) {
            getView()!!.switchButtonExpenseMode()
        } else {
            getView()!!.switchButtonToggle()
            getView()!!.switchButtonIncomeMode()
        }
    }

    override fun checkSwitchButton(isChecked: Boolean) {
        if (isChecked) {
            getView()!!.switchButtonExpenseMode()
        } else {
            getView()!!.switchButtonIncomeMode()
        }
    }

    override fun validateCategoryNameInput(mContext: Context, userUid: String,
                                           categoryNameInput: String, updateCategoryId: String?,
                                           filterType: String) {
        val categoryList = baseModel.getCategoryListByUserUidWithFilterSync(mContext, userUid, filterType)
        val errorMessage = validation.categoryNameValidation(mContext, categoryNameInput,
                categoryList, updateCategoryId)

        if (errorMessage != null) {
            getView()!!.invalidCategoryNameInput(errorMessage)
            getView()!!.hideFloatingButton()
        } else {
            getView()!!.validCategoryNameInput()
            getView()!!.showFloatingButton()
        }
    }

    override fun actionCheck(menuItem: SpeedDialActionItem) {
        when (menuItem.id) {
            R.id.fb_action_edit -> {
                getView()!!.editCategoryPrompt()
            }
            R.id.fb_action_delete -> {
                getView()!!.deleteCategoryPrompt()
            }
        }
    }

    override fun editAccount(mContext: Context, categoryData: Category) {
        try {
            mDetailsAccountModel.editCategoryRealm(mContext, categoryData)
            getView()!!.editCategorySuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }

    override fun deleteAccount(mContext: Context, categoryData: Category) {
        try {
            mDetailsAccountModel.deleteCategoryRealm(mContext, categoryData.categoryId)
            getView()!!.deleteCategorySuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}