package com.example.slnn3r.wallettrackerv2.ui.account.accountview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.account.accountadapter.AccountListAdapter
import com.example.slnn3r.wallettrackerv2.ui.account.accountadapter.accAdapterClickCount
import com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter.ViewAccountPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_view_account.*

class ViewAccountFragment : Fragment(), AccountViewInterface.ViewAccountView {

    private val mViewAccountViewPresenter: ViewAccountPresenter = ViewAccountPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_viewAcc_title)
        return inflater.inflate(R.layout.fragment_view_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCreateButton()
    }

    override fun onStart() {
        super.onStart()
        mViewAccountViewPresenter.bindView(this)
        userData = mViewAccountViewPresenter.getSignedInUser()!!

        mViewAccountViewPresenter.getAccountList(context!!, userData.uid)
    }

    override fun onStop() {
        super.onStop()
        mViewAccountViewPresenter.unbindView()
    }

    override fun populateAccountRecycleView(accountList: ArrayList<Account>) {
        rv_viewAcc_accList.layoutManager = LinearLayoutManager(context)
        rv_viewAcc_accList.adapter = AccountListAdapter(accountList)
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.VIEW_ACCOUNT_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupCreateButton() {
        fb_viewAcc_createAcc.setOnClickListener {
            if (accAdapterClickCount > 0) {
                return@setOnClickListener
            }

            val navController = view!!.findNavController()
            navController.navigate(R.id.action_viewAccountFragment_to_createAccountFragment)

            fb_viewAcc_createAcc.isEnabled = false
            accAdapterClickCount += 1
        }
    }
}
