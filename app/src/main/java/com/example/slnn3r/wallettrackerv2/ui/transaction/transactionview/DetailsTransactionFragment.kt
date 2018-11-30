package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactiondialog.CalculatorDialog
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionpresenter.DetailsTransactionPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.leinardi.android.speeddial.SpeedDialActionItem
import kotlinx.android.synthetic.main.fragment_details_transaction.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailsTransactionFragment : Fragment(), TransactionViewInterface.DetailsTransactionView,
        CalculatorDialog.OnInputSelected {

    private val myCalendar = Calendar.getInstance()
    private lateinit var simpleDateFormat: SimpleDateFormat

    private val mCurrentTime = Calendar.getInstance()
    private val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
    private val minute = mCurrentTime.get(Calendar.MINUTE)
    private lateinit var simpleTimeFormat: SimpleDateFormat

    private val mDetailsTransactionViewPresenter: DetailsTransactionPresenter =
            DetailsTransactionPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser
    private lateinit var transactionArgData: Transaction

    private var initialLaunch = true

    private lateinit var loadedCategoryList: ArrayList<Category>
    private lateinit var loadedAccountList: ArrayList<Account>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        simpleDateFormat = SimpleDateFormat((Constant.Format.DATE_FORMAT), Locale.US)
        simpleTimeFormat = SimpleDateFormat((Constant.Format.TIME_12HOURS_FORMAT), Locale.US)

        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_detailsTrans_title)
        return inflater.inflate(R.layout.fragment_details_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context as MenuActivity).setupNavigationMode()

        setupInitialUi()
        setupSwitchButton()
        setupAmountEditText()
        setupRemarkAutoComplete()
        setupDatePicker()
        setupTimePicker()
        setupFloatingActionButton()
    }

    override fun onStart() {
        super.onStart()
        mDetailsTransactionViewPresenter.bindView(this)

        if(initialLaunch){
            userData = mDetailsTransactionViewPresenter.getSignedInUser()!!

            mDetailsTransactionViewPresenter.getAccountList(context!!, userData.uid)
            mDetailsTransactionViewPresenter.checkSelectedCategoryType(transactionArgData.category.categoryType)
        }
    }

    override fun onStop() {
        super.onStop()
        mDetailsTransactionViewPresenter.unbindView()
    }

    override fun switchButtonToggle() {
        // will trigger getCategoryList at ToggleListener to populate Category Spinner
        sb_detailsTrans_catType.toggle()
    }

    override fun initialExpenseMode() {
        // initialExpenseMode need to trigger getCategoryList to populate Category Spinner
        mDetailsTransactionViewPresenter.getCategoryList(context!!, userData.uid,
                tv_detailsTrans_catType_selection.text.toString())
    }

    override fun switchButtonExpenseMode() {
        tv_detailsTrans_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
        sb_detailsTrans_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
        et_detailsTrans_amount
                .setTextColor(ColorStateList.valueOf(resources.getColor(R.color.colorLightRed)))
    }

    override fun switchButtonIncomeMode() {
        tv_detailsTrans_catType_selection.text = Constant.ConditionalKeyword.INCOME_STATUS
        sb_detailsTrans_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
        et_detailsTrans_amount
                .setTextColor(ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen)))
    }

    override fun selectCategorySpinnerData(spinnerPosition: Int) {
        sp_detailsTrans_selectedCat_selection.setSelection(spinnerPosition)
    }

    override fun selectDeletedCategorySpinnerData(spinnerPosition: Int) {
        sp_detailsTrans_selectedCat_selection.setSelection(spinnerPosition)
    }

    override fun populateAccountSpinner(accountList: ArrayList<Account>) {
        loadedAccountList = accountList // store to global for edit/delete usage later

        val accountNameList = ArrayList<String>()

        accountList.forEach { data ->
            accountNameList.add(data.accountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, accountNameList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_detailsTrans_selectedAcc_selection.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val selectedAccountSharePref =
                mDetailsTransactionViewPresenter.getSelectedAccount(context!!, userData.uid)
        val spinnerPosition = dataAdapter.getPosition(selectedAccountSharePref)
        sp_detailsTrans_selectedAcc_selection.setSelection(spinnerPosition)
    }

    override fun populateCategorySpinner(categoryList: ArrayList<Category>) {
        loadedCategoryList = categoryList // store to global for edit/delete usage later

        val categoryNameList = ArrayList<String>()

        categoryList.forEach { data ->
            categoryNameList.add(data.categoryName)
        }
        // Add (Deleted Item) selection Initially, will filtered out by checkCategoryData if not necessary to appear
        categoryNameList.add(context!!.getString(R.string.deletedItem_spinner_label))

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, categoryNameList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_detailsTrans_selectedCat_selection.adapter = dataAdapter

        // Set Selected Category Spinner Selection
        mDetailsTransactionViewPresenter.checkCategoryData(categoryList, transactionArgData,
                dataAdapter, categoryNameList, initialLaunch)

        initialLaunch = false
    }

    override fun editTransactionPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsTrans_editSubmit_title),
                getString(R.string.cd_detailsTrans_editSubmit_desc),
                resources.getDrawable(R.drawable.ic_warning),
                DialogInterface.OnClickListener { _, _ ->
                    // format date and time input into Long Type
                    val dateTimeInput = Date.parse(getString(R.string.dateTime_format_string,
                            et_detailsTrans_date.text.toString(),
                            et_detailsTrans_time.text.toString()))

                    // input empty Category and Account Data initially
                    val transactionInput =
                            Transaction(transactionArgData.transactionId,
                                    dateTimeInput,
                                    et_detailsTrans_amount.text.toString().toDouble(),
                                    ac_detailsTrans_remarks.text.toString(),
                                    Category("", "", "", "", ""),
                                    Account("", "", "", "", ""))

                    // save remark to Realm
                    mDetailsTransactionViewPresenter.saveRemark(context!!,
                            ac_detailsTrans_remarks.text.toString())

                    mDetailsTransactionViewPresenter
                            .editTransaction(context!!, transactionInput, userData.uid,
                                    sp_detailsTrans_selectedAcc_selection.selectedItem.toString(),
                                    sp_detailsTrans_selectedCat_selection.selectedItem.toString(),
                                    loadedAccountList, loadedCategoryList, transactionArgData)
                }
        ).show()
    }

    override fun deleteTransactionPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsTrans_deleteSubmit_title),
                getString(R.string.cd_detailsTrans_deleteSubmit_desc),
                resources.getDrawable(R.drawable.ic_warning),
                DialogInterface.OnClickListener { _, _ ->
                    mDetailsTransactionViewPresenter
                            .deleteTransaction(context!!, transactionArgData.transactionId)
                }
        ).show()
    }

    override fun editTransactionSuccess() {
        Snackbar.make(view!!,
                getString(R.string.detailsTrans_edited_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun deleteTransactionSuccess() {
        Snackbar.make(view!!,
                getString(R.string.detailsTrans_deleted_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun calculatorInput(input: String) {
        enableAllUiComponent()
        (context as MenuActivity).setupNavigationMode()

        fb_detailsTrans.show()
        tl_detailsTrans_amount.error = null
        tl_detailsTrans_amount.hint = getString(R.string.amount_title)
        et_detailsTrans_amount.setText(input)
    }

    override fun calculatorNoInput() {
        enableAllUiComponent()
        (context as MenuActivity).setupNavigationMode()

        fb_detailsTrans.hide()
        tl_detailsTrans_amount.error = getString(R.string.amount_error_label)
        tl_detailsTrans_amount.hint = getString(R.string.amount_required_title)
        et_detailsTrans_amount.setText(getString(R.string.enter_amount_title))
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.DETAILS_TRANSACTION_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        // Receive Argument
        val transactionDataJson = arguments?.getString(Constant.KeyId.TRANSACTION_DETAILS_ARG)

        // user GSON convert to object
        val gson = Gson()
        transactionArgData = gson.fromJson<Transaction>(transactionDataJson, Transaction::class.java)

        ac_detailsTrans_remarks.setText(transactionArgData.transactionRemark)
        et_detailsTrans_amount.setText(transactionArgData.transactionAmount.toString())

        val dateOnly = simpleDateFormat.format(transactionArgData.transactionDateTime)
        val timeOnly = simpleTimeFormat.format(transactionArgData.transactionDateTime)
        et_detailsTrans_date.setText(dateOnly)
        et_detailsTrans_time.setText(timeOnly)
    }

    private fun setupSwitchButton() {
        sb_detailsTrans_catType.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            mDetailsTransactionViewPresenter.checkSwitchButton(sb_detailsTrans_catType.isChecked)
            mDetailsTransactionViewPresenter.getCategoryList(context!!, userData.uid,
                    tv_detailsTrans_catType_selection.text.toString())
        }
    }

    private fun setupAmountEditText() {
        et_detailsTrans_amount.setOnClickListener {
            disableAllUiComponent()
            (context as MenuActivity).setupToDisable()

            val args = Bundle()
            args.putString(Constant.KeyId.CALCULATE_DIALOG_ARG, et_detailsTrans_amount.text.toString())

            et_detailsTrans_amount.setText(getString(R.string.amount_loading_label))

            val calCustomDialog = CalculatorDialog()
            calCustomDialog.arguments = args
            calCustomDialog.isCancelable = false
            calCustomDialog.setTargetFragment(this, 1)
            calCustomDialog.show(this.fragmentManager, "")
        }
    }

    private fun setupRemarkAutoComplete() {
        val remarkList = mDetailsTransactionViewPresenter.getRemark(context!!)
        val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_list_item_1, remarkList)
        ac_detailsTrans_remarks.setAdapter<ArrayAdapter<String>>(adapter)

        // on click hint selection, will close keyboard
        ac_detailsTrans_remarks.onItemClickListener =
                AdapterView.OnItemClickListener { _: AdapterView<*>, _: View, _: Int, _: Long ->
                    mDetailsTransactionViewPresenter.hideKeyboard(activity!!)
                }
    }

    private fun setupDatePicker() {
        val dateDialog =
                DatePickerDialog(context!!,
                        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                            enableAllUiComponent()
                            (context as MenuActivity).setupNavigationMode()
                            myCalendar.set(Calendar.YEAR, year)
                            myCalendar.set(Calendar.MONTH, monthOfYear)
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            et_detailsTrans_date.setText(simpleDateFormat.format(myCalendar.time))
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))

        dateDialog.setOnCancelListener {
            enableAllUiComponent()
            (context as MenuActivity).setupNavigationMode()
        }

        et_detailsTrans_date.setOnClickListener {
            disableAllUiComponent()
            (context as MenuActivity).setupToDisable()
            dateDialog.show()
        }
    }

    private fun setupTimePicker() {
        val timeDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    enableAllUiComponent()
                    (context as MenuActivity).setupNavigationMode()
                    val time = Time(selectedHour, selectedMinute, 0)
                    val formattedTime = simpleTimeFormat.format(time)
                    et_detailsTrans_time.setText(formattedTime)
                }, hour, minute, false)

        timeDialog.setOnCancelListener {
            enableAllUiComponent()
            (context as MenuActivity).setupNavigationMode()
        }

        et_detailsTrans_time.setOnClickListener {
            disableAllUiComponent()
            (context as MenuActivity).setupToDisable()
            timeDialog.show()
        }
    }

    private fun setupFloatingActionButton() {
        fb_detailsTrans.mainFabOpenedBackgroundColor =
                resources.getColor(R.color.colorPrimary)
        fb_detailsTrans.mainFabClosedBackgroundColor =
                resources.getColor(R.color.colorPrimaryDark)

        fb_detailsTrans.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_edit, R.drawable.ic_edit)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightGreen))
                        .setLabel(getString(R.string.fb_action_edit_title))
                        .setLabelColor(resources.getColor(R.color.colorLightGreen))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsTrans.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_delete, R.drawable.ic_delete)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightRed))
                        .setLabel(getString(R.string.fb_action_delete_title))
                        .setLabelColor(resources.getColor(R.color.colorLightRed))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsTrans.setOnActionSelectedListener { speedDialActionItem ->
            mDetailsTransactionViewPresenter.actionCheck(speedDialActionItem)
            false
        }
    }

    private fun enableAllUiComponent() {
        sp_detailsTrans_selectedAcc_selection.isEnabled = true
        sb_detailsTrans_catType.isEnabled = true
        sp_detailsTrans_selectedCat_selection.isEnabled = true
        ac_detailsTrans_remarks.isEnabled = true
        et_detailsTrans_amount.isEnabled = true
        et_detailsTrans_date.isEnabled = true
        et_detailsTrans_time.isEnabled = true
        fb_detailsTrans.isEnabled = true
    }

    private fun disableAllUiComponent() {
        sp_detailsTrans_selectedAcc_selection.isEnabled = false
        sb_detailsTrans_catType.isEnabled = false
        sp_detailsTrans_selectedCat_selection.isEnabled = false
        ac_detailsTrans_remarks.isEnabled = false
        et_detailsTrans_amount.isEnabled = false
        et_detailsTrans_date.isEnabled = false
        et_detailsTrans_time.isEnabled = false
        fb_detailsTrans.isEnabled = false
    }
}
