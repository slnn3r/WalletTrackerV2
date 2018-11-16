package com.example.slnn3r.wallettrackerv2.constant.defaultdata

import com.example.slnn3r.wallettrackerv2.data.objectclass.Category

class CategoryDefaultData {

    fun getListItem(): ArrayList<Category> {

        val spinnerItem = ArrayList<Category>()

        spinnerItem.add(Category(
                "", "Meals",
                "Expense", "New", ""))
        spinnerItem.add(Category(
                "", "Food and Drink",
                "Expense", "New", ""))
        spinnerItem.add(Category(
                "", "Transport",
                "Expense", "New", ""))
        spinnerItem.add(Category(
                "", "Bills",
                "Expense", "New", ""))
        spinnerItem.add(Category(
                "", "Other",
                "Expense", "Default", ""))
        spinnerItem.add(Category(
                "", "Salary",
                "Income", "New", ""))
        spinnerItem.add(Category(
                "", "Other",
                "Income", "New", ""))
        spinnerItem.add(Category(
                "", "Initial Balance",
                "Income", "Default", ""))

        return spinnerItem
    }
}