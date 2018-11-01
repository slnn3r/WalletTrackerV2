package com.example.slnn3r.wallettrackerv2.ui.category.categoryview

import com.example.slnn3r.wallettrackerv2.base.BaseView
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category

interface CategoryViewInterface {

    interface ViewCategoryView : BaseView.Universal {
        fun switchButtonExpenseMode()
        fun switchButtonIncomeMode()

        fun switchButtonToggle()

        fun populateCategoryRecycleView(categoryList: ArrayList<Category>)
    }

    interface CreateCategoryView : BaseView.Universal {
        fun switchButtonExpenseMode()
        fun switchButtonIncomeMode()

        fun switchButtonToggle()

        fun validCategoryNameInput()
        fun invalidCategoryNameInput(errorMessage: String)

        fun showFloatingButton()
        fun hideFloatingButton()

        fun createCategorySuccess()
    }

    interface DetailsCategoryView : BaseView.Universal {
        fun setupFloatingActionButton()
        fun setupFloatingDefaultButton()

        fun switchButtonExpenseMode()
        fun switchButtonIncomeMode()

        fun switchButtonToggle()

        fun validCategoryNameInput()
        fun invalidCategoryNameInput(errorMessage: String)

        fun showFloatingButton()
        fun hideFloatingButton()

        fun editCategoryPrompt()
        fun deleteCategoryPrompt()

        fun editCategorySuccess()
        fun deleteCategorySuccess()
    }
}