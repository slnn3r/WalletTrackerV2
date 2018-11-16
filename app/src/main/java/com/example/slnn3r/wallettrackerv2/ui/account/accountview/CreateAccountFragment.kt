package com.example.slnn3r.wallettrackerv2.ui.account.accountview

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter.CreateAccountPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_create_account.*
import java.util.*

class CreateAccountFragment : Fragment(), AccountViewInterface.CreateAccountView {

    private val mCreateAccountViewPresenter: CreateAccountPresenter = CreateAccountPresenter()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_createAcc_title)
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialUi()
        setupCreateButton()
        setupAccNameEditView()
        setupAccDescEditView()
    }

    override fun onStart() {
        super.onStart()
        mCreateAccountViewPresenter.bindView(this)
        userData = mCreateAccountViewPresenter.getSignedInUser()!!
    }

    override fun onStop() {
        super.onStop()
        mCreateAccountViewPresenter.unbindView()
    }

    override fun validAccountNameInput() {
        tl_createAcc_accName.isErrorEnabled = false
        tl_createAcc_accName.error = null
    }

    override fun invalidAccountNameInput(errorMessage: String) {
        tl_createAcc_accName.error = errorMessage
    }

    override fun validAccountBalanceInput() {
        tl_createAcc_accDesc.isErrorEnabled = false
        tl_createAcc_accDesc.error = null
    }

    override fun invalidAccountBalanceInput(errorMessage: String) {
        tl_createAcc_accDesc.error = errorMessage
    }

    override fun showFloatingButton() {
        fb_createAcc.show()
    }

    override fun hideFloatingButton() {
        fb_createAcc.hide()
    }

    override fun createAccountSuccess() {
        Toast.makeText(context, getString(R.string.createAcc_created_message), Toast.LENGTH_LONG).show()
        (context as Activity).onBackPressed()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.CREATE_ACCOUNT_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        tl_createAcc_accName.error = getString(R.string.accName_empty_invalid_message)
        tl_createAcc_accDesc.error = getString(R.string.accDescription_empty_invalid_message)

        // Allow Multiline with ActionDone IME
        et_createAcc_accDesc.imeOptions = EditorInfo.IME_ACTION_DONE
        et_createAcc_accDesc.setRawInputType(InputType.TYPE_CLASS_TEXT)

        fb_createAcc.hide()
    }

    private fun setupCreateButton() {
        fb_createAcc.setOnClickListener {
            createButtonSubmit()
        }
    }

    private fun setupAccNameEditView() {
        et_createAcc_accName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCreateAccountViewPresenter.validateAccountNameInput(
                        context!!, userData.uid,
                        et_createAcc_accName.text.toString(), null)
            }

            override fun afterTextChanged(s: Editable?) {
                mCreateAccountViewPresenter.checkAllInputError(tl_createAcc_accName.error,
                        tl_createAcc_accDesc.error)
            }
        })
    }

    private fun setupAccDescEditView() {
        et_createAcc_accDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCreateAccountViewPresenter.validateAccountDescInput(context!!,
                        et_createAcc_accDesc.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                mCreateAccountViewPresenter
                        .checkAllInputError(tl_createAcc_accName.error, tl_createAcc_accDesc.error)
            }
        })
    }

    private fun createButtonSubmit() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_createAcc_createSubmit_title),
                getString(R.string.cd_createAcc_createSubmit_desc),
                resources.getDrawable(R.drawable.ic_info),
                DialogInterface.OnClickListener { _, _ ->
                    val uniqueID = UUID.randomUUID().toString()

                    val accountInput =
                            Account(uniqueID, et_createAcc_accName.text.toString(),
                                    et_createAcc_accDesc.text.toString(),
                                    userData.uid, Constant.ConditionalKeyword.NON_DEFAULT_STATUS)

                    mCreateAccountViewPresenter.createAccount(context!!, accountInput)
                }).show()
    }
}
