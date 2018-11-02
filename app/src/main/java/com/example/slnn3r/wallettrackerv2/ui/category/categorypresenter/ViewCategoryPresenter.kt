package com.example.slnn3r.wallettrackerv2.ui.category.categorypresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.category.categoryview.CategoryViewInterface

class ViewCategoryPresenter : CategoryPresenterInterface.ViewCategoryPresenter,
        BasePresenter<CategoryViewInterface.ViewCategoryView>() {

    private val baseModel: BaseModel = BaseModel()

    override fun checkToggle(isToggleOn: Boolean) {
        if (!isToggleOn) {
            getView()!!.switchButtonToggle()
        }
    }

    override fun checkSwitchButton(isChecked: Boolean) {
        if (isChecked) {
            getView()!!.switchButtonExpenseMode()
        } else {
            getView()!!.switchButtonIncomeMode()
        }
    }

    override fun getCategoryList(mContext: Context, userUid: String,
                                 filterType: String) {

        try {
            val categoryList = baseModel.getCategoryListByUserUidWithFilterSync(
                    mContext, userUid, filterType)

            getView()!!.populateCategoryRecycleView(categoryList)
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}