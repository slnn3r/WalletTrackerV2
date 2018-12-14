package com.example.slnn3r.wallettrackerv2.ui.report.reportadapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.objectclass.TransactionSummary
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.ui.report.reportdialog.ReportTransListDialog
import kotlinx.android.synthetic.main.list_row_transaction.view.*

var reportAdapterClickCount = 0

class ReportListAdapter(private val transactionSummaryList: ArrayList<TransactionSummary>,
                        private val transactionList: ArrayList<Transaction>,
                        private val fragment: Fragment) :
        RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        reportAdapterClickCount = 0
    }

    override fun getItemCount(): Int {
        return transactionSummaryList.count() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_row_transaction, parent, false)

        return TransactionViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val viewContext = holder.view.context

        holder.setIsRecyclable(false)

        if (position == transactionSummaryList.count() && transactionSummaryList.count() != 0) { // detect last index and use it create an empty list when transaction data is present, else show default No Transaction Title
            holder.view.cv_transListRow.background = null
            holder.view.cl_transListRow.isClickable = false
            holder.view.tv_transList_datetime_label.visibility = View.INVISIBLE
            holder.view.tv_transList_category_label.visibility = View.INVISIBLE
            holder.view.tv_transList_amount_label.visibility = View.INVISIBLE
        } else if (transactionSummaryList.count() > 0) {
            val transactionSummaryData = transactionSummaryList[position]

            holder.view.tv_transList_datetime_label.text = transactionSummaryData.transactionSummaryCategory
            holder.view.tv_transList_category_label.text = transactionSummaryData.transactionSummaryCount
            holder.view.tv_transList_amount_label.text = transactionSummaryData.transactionSummaryTotal

            if (transactionSummaryData.transactionSummaryType == Constant.ConditionalKeyword.EXPENSE_STATUS) {
                holder.view.tv_transList_amount_label.setTextColor(
                        ColorStateList.valueOf(viewContext.getColor(R.color.colorLightRed)))
            } else {
                holder.view.tv_transList_amount_label.setTextColor(
                        ColorStateList.valueOf(viewContext.getColor(R.color.colorLightGreen)))
            }

            holder.passTransactionSummaryData = transactionSummaryData
            holder.passTransactionData = transactionList
            holder.passFragment = fragment
        }
    }
}

class TransactionViewHolder(val view: View, var passTransactionSummaryData: TransactionSummary? = null,
                            var passTransactionData: ArrayList<Transaction>? = null,
                            var passFragment: Fragment? = null) :
        RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            (view.context as MenuActivity).setupToDisable()

            if (passTransactionSummaryData != null && reportAdapterClickCount < 1) {
                val filterList = ArrayList<Transaction>()

                passTransactionData?.forEach { data ->
                    if (data.category?.categoryName ==
                            passTransactionSummaryData?.transactionSummaryCategory &&
                            data.category?.categoryType ==
                            passTransactionSummaryData?.transactionSummaryType) {
                        filterList.add(data)
                    }
                }

                val calCustomDialog = ReportTransListDialog()
                calCustomDialog.isCancelable = false
                calCustomDialog.setTransactionData(filterList)
                calCustomDialog.setTransactionSummaryData(passTransactionSummaryData)
                calCustomDialog.setTargetFragment(passFragment!!, 1)
                calCustomDialog.show(passFragment!!.fragmentManager!!, "")

                reportAdapterClickCount += 1
            }
        }
    }
}