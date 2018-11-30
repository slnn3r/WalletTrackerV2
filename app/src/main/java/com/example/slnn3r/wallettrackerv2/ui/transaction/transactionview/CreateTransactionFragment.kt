package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.example.slnn3r.wallettrackerv2.ui.transaction.transactionpresenter.CreateTransactionPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_create_transaction.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateTransactionFragment : Fragment(), TransactionViewInterface.CreateTransactionView,
        CalculatorDialog.OnInputSelected {

    private val myCalendar = Calendar.getInstance()
    private lateinit var simpleDateFormat: SimpleDateFormat

    private val mCurrentTime = Calendar.getInstance()
    private val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
    private val minute = mCurrentTime.get(Calendar.MINUTE)
    private lateinit var simpleTimeFormat: SimpleDateFormat

    private val mCreateTransactionViewPresenter: CreateTransactionPresenter =
            CreateTransactionPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    private lateinit var loadedCategoryList: ArrayList<Category>
    private lateinit var loadedAccountList: ArrayList<Account>

    private var initialLaunch = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        simpleDateFormat = SimpleDateFormat((Constant.Format.DATE_FORMAT), Locale.US)
        simpleTimeFormat = SimpleDateFormat((Constant.Format.TIME_12HOURS_FORMAT), Locale.US)

        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_createTrans_title)
        return inflater.inflate(R.layout.fragment_create_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialUi()
        setupSwitchButton()
        setupAmountEditText()
        setupRemarkAutoComplete()
        setupDatePicker()
        setupTimePicker()
        setupCreateButton()
    }

    override fun onStart() {
        super.onStart()
        mCreateTransactionViewPresenter.bindView(this)

        if(initialLaunch){
            userData = mCreateTransactionViewPresenter.getSignedInUser()!!

            mCreateTransactionViewPresenter.getAccountList(context!!, userData.uid)
            mCreateTransactionViewPresenter.getCategoryList(context!!, userData.uid,
                    tv_createTrans_catType_selection.text.toString())
        }
        initialLaunch= false
    }

    override fun onStop() {
        super.onStop()
        mCreateTransactionViewPresenter.unbindView()
    }

    override fun switchButtonExpenseMode() {
        tv_createTrans_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
        sb_createTrans_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
        et_createTrans_amount
                .setTextColor(ColorStateList.valueOf(resources.getColor(R.color.colorLightRed)))
    }

    override fun switchButtonIncomeMode() {
        tv_createTrans_catType_selection.text = Constant.ConditionalKeyword.INCOME_STATUS
        sb_createTrans_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
        et_createTrans_amount
                .setTextColor(ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen)))
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

        sp_createTrans_selectedAcc_selection.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val selectedAccountSharePref =
                mCreateTransactionViewPresenter.getSelectedAccount(context!!, userData.uid)
        val spinnerPosition = dataAdapter.getPosition(selectedAccountSharePref)
        sp_createTrans_selectedAcc_selection.setSelection(spinnerPosition)
    }

    override fun populateCategorySpinner(categoryList: ArrayList<Category>) {
        loadedCategoryList = categoryList // store to global for create usage later

        val categoryNameList = ArrayList<String>()

        categoryList.forEach { data ->
            categoryNameList.add(data.categoryName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, categoryNameList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_createTrans_selectedCat_selection.adapter = dataAdapter
    }

    override fun createTransactionSuccess() {
        Snackbar.make(view!!,
                getString(R.string.createTrans_created_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun calculatorInput(input: String) {
        enableAllUiComponent()
        (context as MenuActivity).setupNavigationMode()

        fb_createTrans.show()
        tl_createTrans_amount.error = null
        tl_createTrans_amount.hint = getString(R.string.amount_title)
        et_createTrans_amount.setText(input)
    }

    override fun calculatorNoInput() {
        enableAllUiComponent()
        (context as MenuActivity).setupNavigationMode()

        fb_createTrans.hide()
        tl_createTrans_amount.error = getString(R.string.amount_error_label)
        tl_createTrans_amount.hint = getString(R.string.amount_required_title)
        et_createTrans_amount.setText(getString(R.string.enter_amount_title))
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.CREATE_TRANSACTION_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        fb_createTrans.hide()
        tl_createTrans_amount.error = getString(R.string.amount_error_label)

        tv_createTrans_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
    }

    private fun setupSwitchButton() {
        sb_createTrans_catType.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            mCreateTransactionViewPresenter.checkSwitchButton(sb_createTrans_catType.isChecked)
            mCreateTransactionViewPresenter.getCategoryList(context!!, userData.uid,
                    tv_createTrans_catType_selection.text.toString())
        }
    }

    private fun setupAmountEditText() {
        et_createTrans_amount.setOnClickListener {
            disableAllUiComponent()
            (context as MenuActivity).setupToDisable()

            val args = Bundle()
            args.putString(Constant.KeyId.CALCULATE_DIALOG_ARG, et_createTrans_amount.text.toString())

            et_createTrans_amount.setText(getString(R.string.amount_loading_label))

            val calCustomDialog = CalculatorDialog()
            calCustomDialog.arguments = args
            calCustomDialog.isCancelable = false
            calCustomDialog.setTargetFragment(this, 1)
            calCustomDialog.show(this.fragmentManager, "")
        }
    }

    private fun setupRemarkAutoComplete() {
        val remarkList = mCreateTransactionViewPresenter.getRemark(context!!)
        val adapter = ArrayAdapter(context!!,
                android.R.layout.simple_list_item_1, remarkList)
        ac_createTrans_remarks.setAdapter<ArrayAdapter<String>>(adapter)

        // on click hint selection, will close keyboard
        ac_createTrans_remarks.onItemClickListener =
                AdapterView.OnItemClickListener { _: AdapterView<*>, _: View, _: Int, _: Long ->
                    mCreateTransactionViewPresenter.hideKeyboard(activity!!)
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
                            et_createTrans_date.setText(simpleDateFormat.format(myCalendar.time))
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))

        dateDialog.setOnCancelListener {
            enableAllUiComponent()
            (context as MenuActivity).setupNavigationMode()
        }

        et_createTrans_date.setOnClickListener {
            disableAllUiComponent()
            (context as MenuActivity).setupToDisable()
            dateDialog.show()
        }

        // Initial Date
        et_createTrans_date.setText(simpleDateFormat.format(myCalendar.time))
    }

    private fun setupTimePicker() {
        val timeDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    enableAllUiComponent()
                    (context as MenuActivity).setupNavigationMode()
                    val time = Time(selectedHour, selectedMinute, 0)
                    val formattedTime = simpleTimeFormat.format(time)
                    et_createTrans_time.setText(formattedTime)
                }, hour, minute, false)

        timeDialog.setOnCancelListener {
            enableAllUiComponent()
            (context as MenuActivity).setupNavigationMode()
        }

        et_createTrans_time.setOnClickListener {
            disableAllUiComponent()
            (context as MenuActivity).setupToDisable()
            timeDialog.show()
        }

        // Initial Time
        val second = mCurrentTime.get(Calendar.SECOND)
        val time = Time(hour, minute, second)

        //format takes in a Date, and Time is a subclass of Date
        val formattedTime = simpleTimeFormat.format(time)
        et_createTrans_time.setText(formattedTime)
    }

    private fun setupCreateButton() {
        fb_createTrans.setOnClickListener {
            val uniqueID = UUID.randomUUID().toString()

            // format date and time input into Long Type
            val dateTimeInput = Date.parse(getString(R.string.dateTime_format_string,
                    et_createTrans_date.text.toString(),
                    et_createTrans_time.text.toString()))

            // use empty Category and Account Data, process correct data when pass to presenter
            val transactionInput =
                    Transaction(
                            uniqueID,
                            dateTimeInput,
                            et_createTrans_amount.text.toString().toDouble(),
                            ac_createTrans_remarks.text.toString(),
                            Category("", "", "", "", ""),
                            Account("", "", "", "", "")
                    )

            // save remark to Realm
            mCreateTransactionViewPresenter.saveRemark(context!!,
                    ac_createTrans_remarks.text.toString())

            mCreateTransactionViewPresenter.createTransaction(context!!, transactionInput,
                    userData.uid, sp_createTrans_selectedAcc_selection.selectedItem.toString(),
                    sp_createTrans_selectedCat_selection.selectedItem.toString(),
                    loadedAccountList, loadedCategoryList)
        }
    }

    private fun enableAllUiComponent() {
        sp_createTrans_selectedAcc_selection.isEnabled = true
        sb_createTrans_catType.isEnabled = true
        sp_createTrans_selectedCat_selection.isEnabled = true
        ac_createTrans_remarks.isEnabled = true
        et_createTrans_amount.isEnabled = true
        et_createTrans_date.isEnabled = true
        et_createTrans_time.isEnabled = true
        fb_createTrans.isEnabled = true
    }

    private fun disableAllUiComponent() {
        sp_createTrans_selectedAcc_selection.isEnabled = false
        sb_createTrans_catType.isEnabled = false
        sp_createTrans_selectedCat_selection.isEnabled = false
        ac_createTrans_remarks.isEnabled = false
        et_createTrans_amount.isEnabled = false
        et_createTrans_date.isEnabled = false
        et_createTrans_time.isEnabled = false
        fb_createTrans.isEnabled = false
    }
}
