package com.example.slnn3r.wallettrackerv2.ui.category.categoryadapter

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.google.gson.Gson
import kotlinx.android.synthetic.main.category_list_row.view.*

class CategoryListAdapter(private val categoryList: ArrayList<Category>) :
        RecyclerView.Adapter<CategoryViewHolder>() {

    override fun getItemCount(): Int {
        return categoryList.count() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.category_list_row, parent, false)

        return CategoryViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val viewContext = holder.view.context

        holder.setIsRecyclable(false)

        if (position == categoryList.count()) { // detect last index and use it create an empty list
            holder.view.cv_catListRow.background = null
            holder.view.cl_catListRow.isClickable = false
        } else {

            val categoryData = categoryList[position]

            if (categoryData.categoryType.equals(
                            Constant.ConditionalKeyword.EXPENSE_STATUS, ignoreCase = true)) {
                holder.view.cv_catListRow.backgroundTintList =
                        ColorStateList.valueOf(viewContext.getColor(R.color.colorLightRed))
            } else {
                holder.view.cv_catListRow.backgroundTintList =
                        ColorStateList.valueOf(viewContext.getColor(R.color.colorLightGreen))
            }

            if (categoryData.categoryStatus.equals(
                            Constant.ConditionalKeyword.DEFAULT_STATUS, ignoreCase = true)) {

                holder.view.tv_catListRow_catName.text = categoryData.categoryName
                holder.view.tv_catListRow_remark.text = viewContext.getString(R.string.non_deletable_title)
            } else {
                holder.view.tv_catListRow_catName.text = categoryData.categoryName
            }

            holder.passData = categoryData
        }
    }

}

class CategoryViewHolder(val view: View, var passData: Category? = null) :
        RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {

            if (passData != null) {

                val gson = Gson()
                val categoryData = Category(passData!!.categoryId,
                        passData!!.categoryName, passData!!.categoryType,
                        passData!!.categoryStatus, passData!!.userUid)

                val json = gson.toJson(categoryData)

                val navController = view.findNavController()

                val bundle = Bundle()
                bundle.putString(Constant.KeyId.CATEGORY_DETAILS_ARG, json)
                navController
                        .navigate(R.id.action_viewCategoryFragment_to_detailsCategoryFragment, bundle)
            }
        }
    }
}