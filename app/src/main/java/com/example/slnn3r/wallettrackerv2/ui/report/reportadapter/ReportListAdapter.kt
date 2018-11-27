package com.example.slnn3r.wallettrackerv2.ui.report.reportadapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import kotlinx.android.synthetic.main.list_row_transaction.view.*

// maybe could make it pass in ArrayList<ReportList> ?
class ReportListAdapter(private val transactionList: ArrayList<Transaction>) :
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




            holder.passData = transactionData
        }
    }
}

class TransactionViewHolder(val view: View, var passData: Transaction? = null) :
        RecyclerView.ViewHolder(view)