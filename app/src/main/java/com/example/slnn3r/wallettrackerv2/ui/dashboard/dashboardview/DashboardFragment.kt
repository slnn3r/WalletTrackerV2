package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardview

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
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardadapter.TransactionListAdapter
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardpresenter.DashboardViewPresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
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
                        mDashboardViewPresenter.saveSelectedAccount(context!!,
                                accountList[sp_dashboard_selectedAcc_selection
                                        .selectedItemPosition].accountName,
                                userData.uid) //Save Select Account in SharedPreference for future use
                        mDashboardViewPresenter.getTransactionData(context!!,
                                userData.uid,
                                accountList[sp_dashboard_selectedAcc_selection
                                        .selectedItemPosition].accountId)
                    }
                }
    }

    override fun populateTransactionRecycleView(transactionList: ArrayList<Transaction>) {
        rv_dashboard_transList.layoutManager = LinearLayoutManager(context)
        rv_dashboard_transList.adapter = TransactionListAdapter(transactionList)
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

    private fun setupCreateButton() {
        fb_dashboard_createTrans.setOnClickListener {
            (activity as MenuActivity).setupNavigationMode()

            val navController = view!!.findNavController()
            navController.navigate(R.id.action_dashboardFragment_to_createTransactionFragment)
        }
    }
}
