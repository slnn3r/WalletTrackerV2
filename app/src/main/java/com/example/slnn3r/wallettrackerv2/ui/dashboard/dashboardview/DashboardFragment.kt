package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardview

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardadapter.TransactionListAdapter
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardpresenter.DashboardViewPresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.example.slnn3r.wallettrackerv2.util.CustomMarkerAdapter
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(), DashboardViewInterface.DashboardView {

    private val mDashboardViewPresenter: DashboardViewPresenter = DashboardViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_dashboard_title)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // clear Filter Input store from History Module
        mDashboardViewPresenter.clearFilterInputSharePreference(context!!)

        setupInitialUi()
        setupCreateButton()
    }

    override fun onStart() {
        super.onStart()
        mDashboardViewPresenter.bindView(this)
        userData = mDashboardViewPresenter.getSignedInUser()!!

        mDashboardViewPresenter.getAllAccountData(context!!, userData.uid)
    }

    override fun onStop() {
        super.onStop()
        mDashboardViewPresenter.unbindView()
    }

    override fun populateAccountSpinner(accountList: ArrayList<Account>) {
        val accountNameList = ArrayList<String>()

        accountList.forEach { data ->
            accountNameList.add(data.accountName)
        }

        // Creating adapter for spinner
        val dataAdapter =
                ArrayAdapter(context!!, android.R.layout.simple_spinner_item, accountNameList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_dashboard_selectedAcc_selection.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        // If initial setup or just login, will return empty string, spinner position will return -1 yet spinner will select 0 position
        val selectedAccountSharePref =
                mDashboardViewPresenter.getSelectedAccount(context!!, userData.uid)
        val spinnerPosition = dataAdapter.getPosition(selectedAccountSharePref)
        sp_dashboard_selectedAcc_selection.setSelection(spinnerPosition)

        // Listener here because require keep update the accountId for get account list
        sp_dashboard_selectedAcc_selection.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                                position: Int, id: Long) {

                        cv_transListLoading.visibility = View.VISIBLE // display Loading cardview
                        rv_dashboard_transList.adapter = null // remove adapter every reloading

                        mDashboardViewPresenter.saveSelectedAccount(context!!,
                                accountList[sp_dashboard_selectedAcc_selection
                                        .selectedItemPosition].accountName,
                                userData.uid) //Save Select Account in SharedPreference for future use

                        Handler().postDelayed({
                            if (getView() != null) {
                                mDashboardViewPresenter.getTransactionData(context!!, // get RecycleView Data
                                        userData.uid,
                                        accountList[sp_dashboard_selectedAcc_selection
                                                .selectedItemPosition].accountId)
                            }
                        }, 1000)

                        mDashboardViewPresenter.getRecentExpenseTransaction(context!!, // get graph Data
                                userData.uid,
                                accountList[sp_dashboard_selectedAcc_selection
                                        .selectedItemPosition].accountId)
                    }
                }
    }

    override fun populateTransactionRecycleView(transactionList: ArrayList<Transaction>) {
        rv_dashboard_transList.layoutManager = LinearLayoutManager(context)
        rv_dashboard_transList.adapter = TransactionListAdapter(transactionList)
        cv_transListLoading.visibility = View.INVISIBLE // remove loading cardview
    }

    override fun populateExpenseGraph(entryList: ArrayList<Entry>, xAxisList: ArrayList<String>) {
        val dataSet = LineDataSet(entryList, null)

        dataSet.setDrawFilled(true)
        dataSet.valueFormatter = MyValueFormatter()
        dataSet.setColors(ContextCompat.getColor(context!!, R.color.colorLightRed))

        val data = LineData(dataSet)

        val xAxis = mp_dashboard_transFlow.xAxis
        xAxis.isGranularityEnabled = true
        xAxis.valueFormatter = MyXAxisValueFormatter(xAxisList)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        mp_dashboard_transFlow.description = null
        mp_dashboard_transFlow.data = data
        mp_dashboard_transFlow.legend.isEnabled = false

        val mv = CustomMarkerAdapter(context!!, R.layout.marker_linechart_view)
        mp_dashboard_transFlow.markerView = mv

        mp_dashboard_transFlow.notifyDataSetChanged() // this line solve weird auto resize when refresh graph(becuz of being call from spinner listener change)
        mp_dashboard_transFlow.invalidate() // refresh
    }

    override fun proceedToFirstTimeSetup() {
        mDashboardViewPresenter.firstTimeSetup(context!!, userData.uid)
    }

    override fun firstTimeSetupSuccess() {
        mDashboardViewPresenter.getAllAccountData(context!!, userData.uid)
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.DASHBOARD_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        mp_dashboard_transFlow.setNoDataText(getString(R.string.mp_loading_label))
    }

    private fun setupCreateButton() {
        fb_dashboard_createTrans.setOnClickListener {
            (activity as MenuActivity).setupNavigationMode()

            val navController = view!!.findNavController()
            navController.navigate(R.id.action_dashboardFragment_to_createTransactionFragment)

            // Used to Prevent navController unknown destination error (trigger when receive multiple time same request in other screen)
            fb_dashboard_createTrans.isEnabled = false
        }
    }

    // Format the Y Axis Value to 2Decimal value + add Dollar Sign
    inner class MyValueFormatter : IValueFormatter {
        override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int,
                                       viewPortHandler: ViewPortHandler): String {
            return "" // Override the Implementation Display Nothing
        }
    }

    // Format the X Axis Value to Display Desired String Value
    inner class MyXAxisValueFormatter(private val mValues: ArrayList<String>) : IAxisValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[value.toInt()]
        }
    }
}
