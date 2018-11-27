package com.example.slnn3r.wallettrackerv2.ui.report.reportview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.report.reportadapter.ReportListAdapter
import com.example.slnn3r.wallettrackerv2.ui.report.reportpresenter.ReportViewPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
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
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>,
                                        selectedItemView: View, position: Int, id: Long) {
                loadReportData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun setupMonthSpinner() {
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        userData = mReportViewPresenter.getSignedInUser()!!

        mReportViewPresenter.getAccountList(context!!, userData.uid)

        mReportViewPresenter.getReportData(context!!, userData.uid,
                spinner.selectedItem.toString(), loadedAccountList,
                spinner2.selectedItem.toString(), spinner3.selectedItem.toString())

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

        spinner.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val selectedAccountSharePref =
                mReportViewPresenter.getSelectedAccount(context!!, userData.uid)
        val spinnerPosition = dataAdapter.getPosition(selectedAccountSharePref)
        spinner.setSelection(spinnerPosition)
    }

    override fun enableMonthSelection() {
        spinner2.isEnabled = true
    }

    override fun disableMonthSelection() {
        spinner2.isEnabled = false
        spinner2.setSelection(0)
    }

    override fun populateReportRecycleView(transactionList: ArrayList<Transaction>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ReportListAdapter(transactionList)
    }

    override fun populateSummaryGraph() {

    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.REPORT_VIEW_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        setInitialMonth()
        spinner3.setSelection(6)
        spinner2.setSelection(myCalendar.get(Calendar.MONTH) + 1)
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
        spinner2.adapter = monthAdapter
    }

    private fun setupYearSpinner() {
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>,
                                        selectedItemView: View, position: Int, id: Long) {
                mReportViewPresenter.checkDateFilter(spinner3.selectedItem.toString(),
                        spinner2.selectedItem.toString())
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
        spinner3.adapter = spinnerArrayAdapter
    }

    private fun loadReportData() {

        if (!initialLaunch) {
            mReportViewPresenter.getReportData(context!!, userData.uid,
                    spinner.selectedItem.toString(), loadedAccountList,
                    spinner2.selectedItem.toString(), spinner3.selectedItem.toString())
        } else {
            if (initialListenerCount > 1) {
                initialLaunch = false
            }
            initialListenerCount += 1
        }
    }
}
