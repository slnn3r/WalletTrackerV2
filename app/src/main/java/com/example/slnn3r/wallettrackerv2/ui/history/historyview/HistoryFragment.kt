package com.example.slnn3r.wallettrackerv2.ui.history.historyview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.history.historyadapter.HistoryListAdapter
import com.example.slnn3r.wallettrackerv2.ui.history.historyadapter.historyAdapterClickCount
import com.example.slnn3r.wallettrackerv2.ui.history.historypresenter.HistoryViewPresenter
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment(), HistoryViewInterface.HistoryView,
        HistoryFilterDialog.OnFilterTrigger {

    private val mHistoryViewPresenter: HistoryViewPresenter =
            HistoryViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    override fun filterInputSubmit() {
        enableAllUiComponent()
        mHistoryViewPresenter.getHistoryData(context!!, userData.uid)
    }

    override fun filterInputCancel() {
        enableAllUiComponent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_historyTrans_title)
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupFloatingButton()
    }

    private fun setupFloatingButton() {
        fb_historyTrans_filterOption.setOnClickListener {
            if (historyAdapterClickCount > 0) { // needed as FilterDialog delay pop out will cause crash
                return@setOnClickListener
            }

            disableAllUiComponent()

            val calCustomDialog = HistoryFilterDialog()
            calCustomDialog.isCancelable = false
            calCustomDialog.setTargetFragment(this, 1)
            calCustomDialog.show(this.fragmentManager!!, "")
        }
    }

    private fun enableAllUiComponent() {
        (context as MenuActivity).setupNavigationMode()
        fb_historyTrans_filterOption.show()
        historyAdapterClickCount = 0
    }

    private fun disableAllUiComponent() {
        (context as MenuActivity).setupToDisable()
        fb_historyTrans_filterOption.hide()
        historyAdapterClickCount += 1
    }

    override fun onStart() {
        super.onStart()
        mHistoryViewPresenter.bindView(this)

        userData = mHistoryViewPresenter.getSignedInUser()!!

        mHistoryViewPresenter.getHistoryData(context!!, userData.uid)

    }

    override fun onStop() {
        super.onStop()
        mHistoryViewPresenter.unbindView()
    }

    override fun populateHistoryData(transactionList: ArrayList<Transaction>) {
        mHistoryViewPresenter.calculateHistoryData(transactionList)
        rv_history_transList.layoutManager = LinearLayoutManager(context)
        rv_history_transList.adapter = HistoryListAdapter(transactionList)
    }

    override fun populateSummaryHistoryData(transCount: Int, expCount: Int, expTotal: Double,
                                            incCount: Int, incTotal: Double, totalBalance: Double) {
        tv_history_transCount.text = transCount.toString()
        tv_history_expCount.text =
                getString(R.string.history_exp_labelFormat, expCount.toString(), expTotal)
        tv_history_incCount.text =
                getString(R.string.history_inc_labelFormat, incCount.toString(), incTotal)
        tv_history_totalBal.text =
                getString(R.string.history_totalBal_labelFormat, totalBalance)
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.HISTORY_VIEW_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }
}
