package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardview

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardpresenter.DashboardViewPresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(), DashboardViewInterface.DashboardView {

    //private var doubleBackToExitPressedOnce = false

    private val mDashboardViewPresenter: DashboardViewPresenter = DashboardViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Dashboard"
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton.setOnClickListener{

            (activity as MenuActivity).setupNavigationMode()

            /*
            if (doubleBackToExitPressedOnce) {*/
            val navController = view.findNavController()
            navController.navigate(R.id.action_dashboardFragment_to_createTransactionFragment)

                //this.doubleBackToExitPressedOnce = false
            /*
            }else{
                val navController = view.findNavController()
                navController.navigate(R.id.action_dashboardFragment_to_detailsTransactionFragment)
                this.doubleBackToExitPressedOnce = true
            }*/
        }
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
        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, accountNameList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = (context as Activity).findViewById(R.id.spinner) as Spinner
        spinner.adapter = dataAdapter


        // Get SharedPreference saved Selection and Set to Spinner Selection
        //val go = presenter.getSelectedAccount(mainContext)

        //val spinnerPosition = dataAdapter.getPosition(go)
        //spinner.setSelection(spinnerPosition)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                //presenter.setSelectedAccount(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountName) //Save Select Account in SharedPreference for future use

                //presenter.checkTransaction(mainContext, walletAccountList[spinner.selectedItemPosition].WalletAccountID, userProfile.UserUID)

            }
        }

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
}
