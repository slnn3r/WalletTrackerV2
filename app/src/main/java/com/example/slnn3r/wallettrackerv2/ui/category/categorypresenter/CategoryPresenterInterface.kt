package com.example.slnn3r.wallettrackerv2.ui.category.categorypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.leinardi.android.speeddial.SpeedDialActionItem

interface CategoryPresenterInterface {

    interface ViewCategoryPresenter {
        fun checkToggle(isToggleOn: Boolean)
        fun checkSwitchButton(isChecked: Boolean)
        fun getCategoryList(mContext: Context, userUid: String, filterType: String)
    }

    interface CreateCategoryPresenter {
        fun checkSelectedCategoryType(filterType: String)
        fun checkSwitchButton(isChecked: Boolean)
        fun validateCategoryNameInput(mContext: Context, userUid: String,
                                      categoryNameInput: String, updateCategoryId: String?,
                                      filterType: String)

        fun createCategory(mContext: Context, categoryData: Category)
    }

    interface DetailsCategoryPresenter {

        fun checkCategoryStatus(categoryStatus: String)
        fun checkSelectedCategoryType(filterType: String)

        fun checkSwitchButton(isChecked: Boolean)

        fun validateCategoryNameInput(mContext: Context, userUid: String,
                                      categoryNameInput: String, updateCategoryId: String?,
                                      filterType: String)

        fun actionCheck(menuItem: SpeedDialActionItem)

        fun editAccount(mContext: Context, categoryData: Category)
        fun deleteAccount(mContext: Context, categoryData: Category)
    }
}