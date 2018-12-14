package com.example.slnn3r.wallettrackerv2.ui.category.categorymodel

import android.content.Context
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category

interface CategoryModelInterface {

    interface CreateCategoryViewModel {
        fun createCategoryRealm(mContext: Context, categoryData: Category)
    }

    interface DetailsCategoryViewModel {
        fun editCategoryRealm(mContext: Context, categoryData: Category)
        fun deleteCategoryRealm(mContext: Context, categoryId: String?)
    }
}