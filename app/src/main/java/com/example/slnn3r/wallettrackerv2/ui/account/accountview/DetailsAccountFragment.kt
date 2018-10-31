package com.example.slnn3r.wallettrackerv2.ui.account.accountview

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter.DetailsAccountPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.fragment_details_account.*

class DetailsAccountFragment : Fragment(), AccountViewInterface.DetailsAccountView {

    private val mDetailsAccountViewPresenter: DetailsAccountPresenter = DetailsAccountPresenter()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser
    private lateinit var accountArgData: Account

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_detailsAcc_title)
        return inflater.inflate(R.layout.fragment_details_account, container, false)
    }

    override fun onStart() {
        super.onStart()
        mDetailsAccountViewPresenter.bindView(this)
        userData = mDetailsAccountViewPresenter.getSignedInUser()!!
        mDetailsAccountViewPresenter.checkAccountStatus(accountArgData.accountStatus)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Receive Argumemt
        val accountDataJson = arguments?.getString(Constant.KeyId.ACCOUNT_DETAILS_ARG)

        // user GSON convert to object
        val gson = Gson()
        accountArgData = gson.fromJson<Account>(accountDataJson, Account::class.java)

        et_detailsAcc_AccName.setText(accountArgData.accountName)
        et_detailsAcc_AccInitialBal.setText(accountArgData.accountInitialBalance.toString())

        et_detailsAcc_AccName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDetailsAccountViewPresenter.validateAccountNameInput(context!!, userData.uid,
                        et_detailsAcc_AccName.text.toString(), accountArgData.accountId)
            }

            override fun afterTextChanged(s: Editable?) {
                mDetailsAccountViewPresenter.checkAllInputError(tl_detailsAcc_AccName.error,
                        tl_detailsAcc_AccInitialBal.error)
            }
        })

        et_detailsAcc_AccInitialBal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDetailsAccountViewPresenter
                        .decimalInputCheck(et_detailsAcc_AccInitialBal.text.toString())
                mDetailsAccountViewPresenter
                        .validateAccountBalanceInput(et_detailsAcc_AccInitialBal.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                mDetailsAccountViewPresenter.checkAllInputError(tl_detailsAcc_AccName.error,
                        tl_detailsAcc_AccInitialBal.error)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        mDetailsAccountViewPresenter.unbindView()
    }

    override fun setupFloatingActionButton() {

        fb_detailsAcc_editDelete.mainFabOpenedBackgroundColor =
                resources.getColor(R.color.colorPrimary)
        fb_detailsAcc_editDelete.mainFabClosedBackgroundColor =
                resources.getColor(R.color.colorPrimaryDark)

        fb_detailsAcc_editDelete.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_edit, R.drawable.ic_edit)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightGreen))
                        .setLabel(getString(R.string.fb_action_edit_title))
                        .setLabelColor(resources.getColor(R.color.colorLightGreen))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsAcc_editDelete.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_delete, R.drawable.ic_delete)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightRed))
                        .setLabel(getString(R.string.fb_action_delete_title))
                        .setLabelColor(resources.getColor(R.color.colorLightRed))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsAcc_editDelete.setOnActionSelectedListener { speedDialActionItem ->
            mDetailsAccountViewPresenter.actionCheck(speedDialActionItem)
            false
        }
    }

    override fun setupFloatingDefaultButton() {

        fb_detailsAcc_editDelete.mainFabClosedBackgroundColor =
                resources.getColor(R.color.colorPrimaryDark)
        fb_detailsAcc_editDelete.setMainFabClosedDrawable(resources.getDrawable(R.drawable.ic_edit))

        fb_detailsAcc_editDelete.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {

                mCustomConfirmationDialog.confirmationDialog(context!!,
                        getString(R.string.cd_detailsAcc_editSubmit_title),
                        getString(R.string.cd_detailsAcc_editSubmit_desc),
                        resources.getDrawable(android.R.drawable.ic_dialog_alert),
                        DialogInterface.OnClickListener { _, _ ->

                            val accountInput =
                                    Account(accountArgData.accountId,
                                            et_detailsAcc_AccName.text.toString(),
                                            et_detailsAcc_AccInitialBal.text.toString().toDouble(),
                                            accountArgData.userUid, accountArgData.accountStatus)

                            mDetailsAccountViewPresenter
                                    .editAccount(context!!, accountInput,
                                            tl_detailsAcc_AccName.error,
                                            tl_detailsAcc_AccInitialBal.error)
                        }
                ).show()

                return false
            }

            override fun onToggleChanged(isOpen: Boolean) {
            }
        })
    }

    override fun validAccountNameInput() {
        tl_detailsAcc_AccName.isErrorEnabled = false
        tl_detailsAcc_AccName.error = null
    }

    override fun invalidAccountNameInput(errorMessage: String) {
        tl_detailsAcc_AccName.error = errorMessage
    }

    override fun validAccountBalanceInput() {
        tl_detailsAcc_AccInitialBal.isErrorEnabled = false
        tl_detailsAcc_AccInitialBal.error = null
    }

    override fun invalidAccountBalanceInput(errorMessage: String) {
        tl_detailsAcc_AccInitialBal.error = errorMessage
    }

    override fun setTwoDecimalPlace() {
        val text = et_detailsAcc_AccInitialBal.text.toString()
        et_detailsAcc_AccInitialBal.setText(text.substring(0, text.length - 1))
        et_detailsAcc_AccInitialBal.setSelection(et_detailsAcc_AccInitialBal.text.length)
    }

    override fun showFloatingButton() {
        fb_detailsAcc_editDelete.show()
    }

    override fun hideFloatingButton() {
        fb_detailsAcc_editDelete.hide()
    }

    override fun editAccountPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsAcc_editSubmit_title),
                getString(R.string.cd_detailsAcc_editSubmit_desc),
                resources.getDrawable(R.drawable.ic_info),
                DialogInterface.OnClickListener { _, _ ->

                    val accountInput =
                            Account(accountArgData.accountId,
                                    et_detailsAcc_AccName.text.toString(),
                                    et_detailsAcc_AccInitialBal.text.toString().toDouble(),
                                    accountArgData.userUid, accountArgData.accountStatus)

                    mDetailsAccountViewPresenter
                            .editAccount(context!!, accountInput,
                                    tl_detailsAcc_AccName.error, tl_detailsAcc_AccInitialBal.error)
                }
        ).show()
    }

    override fun deleteAccountPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsAcc_deleteSubmit_title),
                getString(R.string.cd_detailsAcc_deleteSubmit_desc),
                resources.getDrawable(R.drawable.ic_warning),
                DialogInterface.OnClickListener { _, _ ->

                    val accountInput =
                            Account(accountArgData.accountId,
                                    et_detailsAcc_AccName.text.toString(),
                                    et_detailsAcc_AccInitialBal.text.toString().toDouble(),
                                    accountArgData.userUid, accountArgData.accountStatus)

                    mDetailsAccountViewPresenter
                            .deleteAccount(context!!, accountInput)
                }
        ).show()
    }

    override fun editAccountSuccess() {
        Toast.makeText(context, getString(R.string.detailsAcc_edited_message),
                Toast.LENGTH_LONG).show()
        (context as Activity).onBackPressed()
    }

    override fun deleteAccountSuccess() {
        Toast.makeText(context, getString(R.string.detailsAcc_deleted_message),
                Toast.LENGTH_LONG).show()
        (context as Activity).onBackPressed()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.DETAILS_ACCOUNT_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }
}
