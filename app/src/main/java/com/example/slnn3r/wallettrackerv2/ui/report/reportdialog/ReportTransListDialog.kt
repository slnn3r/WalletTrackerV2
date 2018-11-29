package com.example.slnn3r.wallettrackerv2.ui.report.reportdialog

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.objectclass.TransactionSummary
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.ui.report.reportadapter.ReportTransListAdapter
import com.example.slnn3r.wallettrackerv2.ui.report.reportadapter.reportAdapterClickCount
import kotlinx.android.synthetic.main.dialog_report_transaction_list.*

class ReportTransListDialog : BottomSheetDialogFragment() {

    private lateinit var transactionData: ArrayList<Transaction>
    private lateinit var transactionSummaryData: TransactionSummary

    fun setTransactionData(passData: ArrayList<Transaction>) {
        transactionData = passData
    }

    fun setTransactionSummaryData(passData: TransactionSummary) {
        transactionSummaryData = passData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_report_transaction_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog
                    .findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
        }

        btn_reportTrans_close.setOnClickListener {
            (context as MenuActivity).setupNavigationMode()
            dialog.dismiss()
            reportAdapterClickCount = 0
        }

        tv_reportTrans_catTitle.text = getString(R.string.tv_reportDialog_title,
                transactionSummaryData.transactionSummaryCategory)
        tv_reportTrans_amountTitle.text = transactionSummaryData.transactionSummaryTotal
        tv_reportTrans_countTitle.text = transactionSummaryData.transactionSummaryCount

        rv_reportTrans_transList.isNestedScrollingEnabled = false
        rv_reportTrans_transList.layoutManager = LinearLayoutManager(context)
        rv_reportTrans_transList.adapter = ReportTransListAdapter(transactionData)
    }
}


