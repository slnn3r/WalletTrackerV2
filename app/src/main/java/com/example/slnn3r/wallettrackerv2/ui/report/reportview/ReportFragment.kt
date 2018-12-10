package com.example.slnn3r.wallettrackerv2.ui.report.reportview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.data.objectclass.TransactionSummary
import com.example.slnn3r.wallettrackerv2.ui.report.reportadapter.ReportListAdapter
import com.example.slnn3r.wallettrackerv2.ui.report.reportpresenter.ReportViewPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.example.slnn3r.wallettrackerv2.util.CustomMarkerAdapter
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_report.*
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : Fragment(), ReportViewInterface.ReportView {

    private val mReportViewPresenter: ReportViewPresenter =
            ReportViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser
    private val myCalendar = Calendar.getInstance()

    private lateinit var loadedAccountList: ArrayList<Account>

    private var initialLaunch = true
    private var initialListenerCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_report_title)
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateYears(myCalendar.get(Calendar.YEAR) - 5, myCalendar.get(Calendar.YEAR))
        setupInitialUi()
        setupYearSpinner()
        setupMonthSpinner()
        setupAccountSpinner()
    }

    private fun setupAccountSpinner() {
        sp_report_selectedAcc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>,
                                        selectedItemView: View, position: Int, id: Long) {
                loadReportData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun setupMonthSpinner() {
        sp_report_monthSelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>,
                                        selectedItemView: View, position: Int, id: Long) {
                loadReportData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    override fun onStart() {
        super.onStart()

        mReportViewPresenter.bindView(this)

        if (initialLaunch) {
            userData = mReportViewPresenter.getSignedInUser()!!

            mReportViewPresenter.getAccountList(context!!, userData.uid)

            mReportViewPresenter.getReportData(context!!, userData.uid,
                    sp_report_selectedAcc.selectedItem.toString(), loadedAccountList,
                    sp_report_monthSelection.selectedItem.toString(), sp_report_yearSelection.selectedItem.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        mReportViewPresenter.unbindView()
    }

    override fun populateAccountSpinner(accountList: ArrayList<Account>) {
        loadedAccountList = accountList // store to global for create usage later

        val accountNameList = ArrayList<String>()

        accountList.forEach { data ->
            accountNameList.add(data.accountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, accountNameList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_report_selectedAcc.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val selectedAccountSharePref =
                mReportViewPresenter.getSelectedAccount(context!!, userData.uid)
        val spinnerPosition = dataAdapter.getPosition(selectedAccountSharePref)
        sp_report_selectedAcc.setSelection(spinnerPosition)
    }

    override fun enableMonthSelection() {
        sp_report_monthSelection.isEnabled = true
    }

    override fun disableMonthSelection() {
        sp_report_monthSelection.isEnabled = false
        sp_report_monthSelection.setSelection(0)
    }

    override fun populateReportRecycleView(transactionSummaryList: ArrayList<TransactionSummary>,
                                           transactionList: ArrayList<Transaction>) {
        rv_report_summaryTrans_list.layoutManager = LinearLayoutManager(context)
        rv_report_summaryTrans_list.adapter = ReportListAdapter(transactionSummaryList, transactionList, this)
    }

    override fun populateSummaryGraph(entryList: ArrayList<BarEntry>, yAxisList: ArrayList<String>) {
        mp_report_summaryChart.setScaleEnabled(false)
        mp_report_summaryChart.description = null
        mp_report_summaryChart.axisLeft.setDrawLabels(false)

        val dataSet = BarDataSet(entryList, "")
        dataSet.setDrawValues(true)
        dataSet.valueFormatter = MyValueFormatter()
        dataSet.setColors()

        dataSet.setColors(
                ContextCompat.getColor(context!!, R.color.colorGraphBlue),
                ContextCompat.getColor(context!!, R.color.colorGraphRed),
                ContextCompat.getColor(context!!, R.color.colorGraphGreen))

        val xAxis = mp_report_summaryChart.xAxis
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1f
        xAxis.valueFormatter = MyXAxisValueFormatter(yAxisList)
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.setDrawGridLines(false)

        val data = BarData(dataSet)
        data.barWidth = 0.9f // set custom bar width
        mp_report_summaryChart.data = data
        mp_report_summaryChart.setFitBars(true) // make the x-axis fit exactly all bars
        mp_report_summaryChart.notifyDataSetChanged() // this line solve weird auto resize when refresh graph(becuz of being call from spinner listener change)
        mp_report_summaryChart.invalidate() // refresh

        mp_report_summaryChart.legend.isEnabled = false

        val mv = CustomMarkerAdapter(context!!, R.layout.marker_barchart_view)
        mp_report_summaryChart.markerView = mv
    }

    override fun setTotalTransactionLabel(transactionCount: Int) {
        tv_report_transaction_title.text =
                getString(R.string.tv_report_transactionTitle, transactionCount)
    }

    // Format the X Axis Value to Display Desired String Value
    inner class MyXAxisValueFormatter(private val mValues: ArrayList<String>) : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[value.toInt()]
        }
    }


    // Format the Y Axis Value to 2Decimal value + add Dollar Sign
    inner class MyValueFormatter : IValueFormatter {
        override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int,
                                       viewPortHandler: ViewPortHandler): String {
            return ""// Override the Implementation Display Nothing
        }
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.REPORT_VIEW_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        setInitialMonth()
        sp_report_yearSelection.setSelection(6)
        sp_report_monthSelection.setSelection(myCalendar.get(Calendar.MONTH) + 1)
    }

    private fun setInitialMonth() {
        val monthList = ArrayList<String>()
        monthList.add(Constant.ConditionalKeyword.All_MONTH_STATUS)
        monthList.add(Constant.DefaultValue.JAN)
        monthList.add(Constant.DefaultValue.FEB)
        monthList.add(Constant.DefaultValue.MAR)
        monthList.add(Constant.DefaultValue.APR)
        monthList.add(Constant.DefaultValue.MAY)
        monthList.add(Constant.DefaultValue.JUN)
        monthList.add(Constant.DefaultValue.JUL)
        monthList.add(Constant.DefaultValue.AUG)
        monthList.add(Constant.DefaultValue.SEP)
        monthList.add(Constant.DefaultValue.OCT)
        monthList.add(Constant.DefaultValue.NOV)
        monthList.add(Constant.DefaultValue.DEC)

        // Creating adapter for month spinner
        val monthAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, monthList)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_report_monthSelection.adapter = monthAdapter
    }

    private fun setupYearSpinner() {
        sp_report_yearSelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>,
                                        selectedItemView: View, position: Int, id: Long) {
                mReportViewPresenter.checkDateFilter(sp_report_yearSelection.selectedItem.toString(),
                        sp_report_monthSelection.selectedItem.toString())
                loadReportData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun populateYears(minYear: Int, maxYear: Int) {
        val yearsArray = arrayOfNulls<String>(maxYear - minYear + 2)

        yearsArray[0] = Constant.ConditionalKeyword.All_YEAR_STATUS

        var count = 1
        for (i in minYear..maxYear) {
            yearsArray[count] = "" + i
            count++
        }

        val spinnerArrayAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_dropdown_item, yearsArray)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_report_yearSelection.adapter = spinnerArrayAdapter
    }

    private fun loadReportData() {
        if (!initialLaunch) {
            mReportViewPresenter.getReportData(context!!, userData.uid,
                    sp_report_selectedAcc.selectedItem.toString(), loadedAccountList,
                    sp_report_monthSelection.selectedItem.toString(), sp_report_yearSelection.selectedItem.toString())
        } else {
            if (initialListenerCount > 1) {
                initialLaunch = false
            }
            initialListenerCount += 1
        }
    }
}
