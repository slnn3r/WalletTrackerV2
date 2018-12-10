package com.example.slnn3r.wallettrackerv2.ui.report.reportadapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import kotlinx.android.synthetic.main.list_row_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*

class ReportTransListAdapter(private val transactionList: ArrayList<Transaction>) :
        RecyclerView.Adapter<ReportDialogViewHolder>() {

    override fun getItemCount(): Int {
        return transactionList.count() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportDialogViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_row_transaction, parent, false)

        return ReportDialogViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ReportDialogViewHolder, position: Int) {
        val viewContext = holder.view.context

        holder.setIsRecyclable(false)

        if (position == transactionList.count() && transactionList.count() != 0) { // detect last index and use it create an empty list when transaction data is present, else show default No Transaction Title
            holder.view.cv_transListRow.background = null
            holder.view.cl_transListRow.isClickable = false
            holder.view.tv_transList_datetime_label.visibility = View.INVISIBLE
            holder.view.tv_transList_category_label.visibility = View.INVISIBLE
            holder.view.tv_transList_amount_label.visibility = View.INVISIBLE
        } else if (transactionList.count() > 0) {
            val transactionData = transactionList[position]

            val dateFormat = SimpleDateFormat(Constant.Format.DATE_FORMAT, Locale.US)
            val date12Format = SimpleDateFormat(Constant.Format.TIME_12HOURS_FORMAT, Locale.US)

            val timeOnly = date12Format.format(transactionData.transactionDateTime)
            val dateOnly = dateFormat.format(transactionData.transactionDateTime)

            val sdf = SimpleDateFormat(Constant.Format.DAY_FORMAT, Locale.US)
            val d = Date.parse(dateOnly)
            val dayOfTheWeek = sdf.format(d)

            if (transactionData.category.categoryType.equals(
                            Constant.ConditionalKeyword.EXPENSE_STATUS, ignoreCase = true)) {
                holder.view.tv_transList_amount_label.text =
                        viewContext.getString(R.string.tv_transList_amount_labelFormat,
                                viewContext.getString(R.string.negative_figure_symbol),
                                transactionData.transactionAmount)
                holder.view.tv_transList_amount_label.setTextColor(
                        ColorStateList.valueOf(viewContext.getColor(R.color.colorLightRed)))
            } else {
                holder.view.tv_transList_amount_label.text =
                        viewContext.getString(R.string.tv_transList_amount_labelFormat,
                                viewContext.getString(R.string.positive_figure_symbol),
                                transactionData.transactionAmount)
                holder.view.tv_transList_amount_label.setTextColor(
                        ColorStateList.valueOf(viewContext.getColor(R.color.colorLightGreen)))
            }

            holder.view.tv_transList_datetime_label.text =
                    viewContext.getString(R.string.tv_transList_datetime_labelFormat,
                            dayOfTheWeek, dateOnly, timeOnly)

            if (transactionData.transactionRemark!!.isEmpty()) {
                holder.view.tv_transList_category_label.text =
                        viewContext.getString(R.string.tv_transList_categoryOnly_labelFormat,
                                transactionData.category.categoryName)
            } else {
                holder.view.tv_transList_category_label.text =
                        viewContext.getString(R.string.tv_transList_categoryRemark_labelFormat,
                                transactionData.category.categoryName,
                                transactionData.transactionRemark)
            }
        }
    }
}

class ReportDialogViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)