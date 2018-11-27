package com.example.slnn3r.wallettrackerv2.initialdatabase

import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category

class CategoryData {

    fun getListItem(): ArrayList<Category> {
        val spinnerItem = ArrayList<Category>()
        val expense = Constant.ConditionalKeyword.EXPENSE_STATUS
        val income = Constant.ConditionalKeyword.INCOME_STATUS
        val nonDefault = Constant.ConditionalKeyword.NON_DEFAULT_STATUS
        val default = Constant.ConditionalKeyword.DEFAULT_STATUS

        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT1,
                expense, nonDefault, ""))
        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT2,
                expense, nonDefault, ""))
        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT3,
                expense, nonDefault, ""))
        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT4,
                expense, nonDefault, ""))
        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT5,
                expense, default, ""))
        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT6,
                income, nonDefault, ""))
        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT5,
                income, nonDefault, ""))
        spinnerItem.add(Category(
                "", Constant.DefaultValue.CATEGORY_DATA_DEFAULT7,
                income, default, ""))

        return spinnerItem
    }
}