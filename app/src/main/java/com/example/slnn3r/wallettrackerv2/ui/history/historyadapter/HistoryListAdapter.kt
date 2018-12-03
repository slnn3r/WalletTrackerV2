package com.example.slnn3r.wallettrackerv2.ui.history.historyadapter

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.google.gson.Gson
import kotlinx.android.synthetic.main.list_row_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryListAdapter(private val transactionList: ArrayList<Transaction>) :
        RecyclerView.Adapter<TransactionViewHolder>() {

    override fun getItemCount(): Int {
        return transactionList.count() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_row_transaction, parent, false)

        return TransactionViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
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

            holder.passData = transactionData
        }
    }
}

class TransactionViewHolder(val view: View, var passData: Transaction? = null) :
        RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            if (passData != null) {
                val gson = Gson()

                val transactionData = Transaction(passData!!.transactionId,
                        passData!!.transactionDateTime,
                        passData!!.transactionAmount, passData!!.transactionRemark,
                        passData!!.category, passData!!.account)

                val json = gson.toJson(transactionData)

                val navController = view.findNavController()

                val bundle = Bundle()
                bundle.putString(Constant.KeyId.TRANSACTION_DETAILS_ARG, json)
                navController
                        .navigate(R.id.action_historyFragment_to_detailsTransactionFragment, bundle)
            }
        }
    }
}