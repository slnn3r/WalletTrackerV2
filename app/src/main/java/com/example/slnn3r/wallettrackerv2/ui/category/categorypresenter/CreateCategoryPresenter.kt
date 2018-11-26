package com.example.slnn3r.wallettrackerv2.ui.category.categorypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.ui.category.categorymodel.CreateCategoryViewModel
import com.example.slnn3r.wallettrackerv2.ui.category.categoryview.CategoryViewInterface
import com.example.slnn3r.wallettrackerv2.util.InputValidation

class CreateCategoryPresenter : CategoryPresenterInterface.CreateCategoryPresenter,
        BasePresenter<CategoryViewInterface.CreateCategoryView>() {

    private val baseModel: BaseModel = BaseModel()
    private val mCreateCategoryModel: CreateCategoryViewModel = CreateCategoryViewModel()
    private val validation = InputValidation()

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
        val categoryList =
                baseModel.getCatListByUserUidWithFilterSync(mContext, userUid, filterType)
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

    override fun createCategory(mContext: Context, categoryData: Category) {
        try {
            mCreateCategoryModel.createCategoryRealm(mContext, categoryData)
            getView()!!.createCategorySuccess()
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}