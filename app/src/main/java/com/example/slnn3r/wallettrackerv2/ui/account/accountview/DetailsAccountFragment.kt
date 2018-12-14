package com.example.slnn3r.wallettrackerv2.ui.account.accountview

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter.DetailsAccountPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.fragment_details_account.*

class DetailsAccountFragment : Fragment(), AccountViewInterface.DetailsAccountView {

    private val mDetailsAccountViewPresenter: DetailsAccountPresenter = DetailsAccountPresenter()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private var userData: FirebaseUser? = null
    private lateinit var accountArgData: Account

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_detailsAcc_title)
        return inflater.inflate(R.layout.fragment_details_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialUi()
        setupAccNameEditText()
        setupAccDescEditText()
    }

    override fun onStart() {
        super.onStart()
        mDetailsAccountViewPresenter.bindView(this)
        userData = mDetailsAccountViewPresenter.getSignedInUser()
        mDetailsAccountViewPresenter.checkAccountStatus(accountArgData.accountStatus)
    }

    override fun onStop() {
        super.onStop()
        mDetailsAccountViewPresenter.unbindView()
    }

    override fun setupFloatingActionButton() {
        fb_detailsAcc.mainFabOpenedBackgroundColor =
                resources.getColor(R.color.colorPrimary)
        fb_detailsAcc.mainFabClosedBackgroundColor =
                resources.getColor(R.color.colorPrimaryDark)

        fb_detailsAcc.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_edit, R.drawable.ic_edit)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightGreen))
                        .setLabel(getString(R.string.fb_action_edit_title))
                        .setLabelColor(resources.getColor(R.color.colorLightGreen))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsAcc.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_delete, R.drawable.ic_delete)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightRed))
                        .setLabel(getString(R.string.fb_action_delete_title))
                        .setLabelColor(resources.getColor(R.color.colorLightRed))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsAcc.setOnActionSelectedListener { speedDialActionItem ->
            mDetailsAccountViewPresenter.actionCheck(speedDialActionItem)
            false
        }
    }

    override fun setupFloatingDefaultButton() {
        fb_detailsAcc.mainFabClosedBackgroundColor =
                resources.getColor(R.color.colorPrimaryDark)
        fb_detailsAcc.setMainFabClosedDrawable(resources.getDrawable(R.drawable.ic_edit))

        fb_detailsAcc.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {

                mCustomConfirmationDialog.confirmationDialog(context!!,
                        getString(R.string.cd_detailsAcc_editSubmit_title),
                        getString(R.string.cd_detailsAcc_editSubmit_desc),
                        resources.getDrawable(R.drawable.ic_warning),
                        DialogInterface.OnClickListener { _, _ ->

                            val accountInput =
                                    Account(accountArgData.accountId,
                                            et_detailsAcc_AccName.text.toString(),
                                            et_detailsAcc_AccDesc.text.toString(),
                                            accountArgData.userUid, accountArgData.accountStatus)

                            mDetailsAccountViewPresenter
                                    .editAccount(context!!, accountInput)
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
        tl_detailsAcc_AccDesc.isErrorEnabled = false
        tl_detailsAcc_AccDesc.error = null
    }

    override fun invalidAccountBalanceInput(errorMessage: String) {
        tl_detailsAcc_AccDesc.error = errorMessage
    }

    override fun showFloatingButton() {
        fb_detailsAcc.show()
    }

    override fun hideFloatingButton() {
        fb_detailsAcc.hide()
    }

    override fun editAccountPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsAcc_editSubmit_title),
                getString(R.string.cd_detailsAcc_editSubmit_desc),
                resources.getDrawable(R.drawable.ic_warning),
                DialogInterface.OnClickListener { _, _ ->
                    val accountInput =
                            Account(accountArgData.accountId,
                                    et_detailsAcc_AccName.text.toString(),
                                    et_detailsAcc_AccDesc.text.toString(),
                                    accountArgData.userUid, accountArgData.accountStatus)

                    mDetailsAccountViewPresenter.editAccount(context!!, accountInput)
                }
        ).show()
    }

    override fun deleteAccountPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsAcc_deleteSubmit_title),
                getString(R.string.cd_detailsAcc_deleteSubmit_desc),
                resources.getDrawable(R.drawable.ic_warning),
                DialogInterface.OnClickListener { _, _ ->
                    mDetailsAccountViewPresenter
                            .deleteAccount(context!!, accountArgData.accountId)
                }
        ).show()
    }

    override fun editAccountSuccess() {
        Snackbar.make(view!!,
                getString(R.string.detailsAcc_edited_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun deleteAccountSuccess() {
        Snackbar.make(view!!,
                getString(R.string.detailsAcc_deleted_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.DETAILS_ACCOUNT_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        // Receive Argument
        val accountDataJson = arguments?.getString(Constant.KeyId.ACCOUNT_DETAILS_ARG)

        // user GSON convert to object
        val gson = Gson()
        accountArgData = gson.fromJson<Account>(accountDataJson, Account::class.java)

        et_detailsAcc_AccName.setText(accountArgData.accountName)

        // Allow Multiline with ActionDone IME
        et_detailsAcc_AccDesc.imeOptions = EditorInfo.IME_ACTION_DONE
        et_detailsAcc_AccDesc.setRawInputType(InputType.TYPE_CLASS_TEXT)

        et_detailsAcc_AccDesc.setText(accountArgData.accountDesc)
    }

    private fun setupAccNameEditText() {
        et_detailsAcc_AccName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDetailsAccountViewPresenter.validateAccountNameInput(context!!, userData?.uid,
                        et_detailsAcc_AccName.text.toString(), accountArgData.accountId)
            }

            override fun afterTextChanged(s: Editable?) {
                mDetailsAccountViewPresenter.checkAllInputError(tl_detailsAcc_AccName.error,
                        tl_detailsAcc_AccDesc.error)
            }
        })
    }

    private fun setupAccDescEditText() {
        et_detailsAcc_AccDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDetailsAccountViewPresenter.validateAccountDescInput(context!!,
                        et_detailsAcc_AccDesc.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                mDetailsAccountViewPresenter.checkAllInputError(tl_detailsAcc_AccName.error,
                        tl_detailsAcc_AccDesc.error)
            }
        })
    }
}
