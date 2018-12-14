package com.example.slnn3r.wallettrackerv2.ui.history.historyview

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.FrameLayout
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.ui.history.historypresenter.HistoryFilterDialogPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_history_filter.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFilterDialog : BottomSheetDialogFragment(), HistoryViewInterface.HistoryFilterDialog {

    private val myCalendar = Calendar.getInstance()
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var simpleMonthFormat: SimpleDateFormat

    private lateinit var onFilterTriggerDialog: OnFilterTrigger

    private lateinit var tempYearAdapter: ArrayAdapter<String?>

    private val mHistoryFilterDialogPresenter: HistoryFilterDialogPresenter =
            HistoryFilterDialogPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private var userData: FirebaseUser? = null

    private lateinit var loadedCategoryList: ArrayList<Category>
    private lateinit var loadedAccountList: ArrayList<Account>

    private var havePreviousType = false
    private var havePreviousDay = false
    private var havePreviousDayCount = 0


    interface OnFilterTrigger {
        fun filterInputSubmit()
        fun filterInputCancel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        simpleDateFormat = SimpleDateFormat((Constant.Format.DATE_FORMAT), Locale.US)
        simpleMonthFormat = SimpleDateFormat((Constant.Format.MONTH_FORMAT), Locale.US)
        return inflater.inflate(R.layout.dialog_history_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogInitialUi()
        setupButton()
        setupStartDatePicker()
        setupEndDatePicker()
        setupSpecificDateSpinner()
        setupTransTypeSpinner()
        setupDateOptionSwitchButton()
    }

    override fun onPause() {
        super.onPause()
        onFilterTriggerDialog.filterInputCancel()
        dialog?.dismiss()
    }

    private fun setupDateOptionSwitchButton() {
        sb_historyFilter_dateOption.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            setupCurrentSpecificDate()
            setupDateInput()
        }
    }

    private fun setupTransTypeSpinner() {
        sp_historyFilter_transType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>,
                                        selectedItemView: View, position: Int, id: Long) {
                if (!havePreviousType) {
                    when {
                        sp_historyFilter_transType.selectedItem ==
                                Constant.ConditionalKeyword.EXPENSE_STATUS ->
                            mHistoryFilterDialogPresenter.getCategoryList(context!!, userData?.uid,
                                    sp_historyFilter_transType.selectedItem.toString())
                        sp_historyFilter_transType.selectedItem
                                == Constant.ConditionalKeyword.INCOME_STATUS ->
                            mHistoryFilterDialogPresenter.getCategoryList(context!!, userData?.uid,
                                    sp_historyFilter_transType.selectedItem.toString())
                        else -> {
                            val spinnerItem = ArrayList<String>()

                            spinnerItem.add(Constant.ConditionalKeyword.ALL_CATEGORY_STATUS)

                            val dataAdapter = ArrayAdapter(context!!,
                                    android.R.layout.simple_spinner_item, spinnerItem)
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                            sp_historyFilter_selectedCat.adapter = dataAdapter
                            sp_historyFilter_selectedCat.isEnabled = false
                        }
                    }
                } else {
                    havePreviousType = false
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun setupSpecificDateSpinner() {
        sp_historyFilter_specificYear.onItemSelectedListener = fixDays
        sp_historyFilter_specificMonth.onItemSelectedListener = fixDays
    }

    private fun setupDateInput() {
        if (sb_historyFilter_dateOption.isChecked) {
            tv_historyFilter_dateOption.text = Constant.ConditionalKeyword.SPECIFIC_DATE
            et_historyFilter_rangeFrom.isEnabled = false
            et_historyFilter_rangeTo.isEnabled = false
            sp_historyFilter_specificDay.isEnabled = true
            sp_historyFilter_specificMonth.isEnabled = true
            sp_historyFilter_specificYear.isEnabled = true
        } else {
            tv_historyFilter_dateOption.text = Constant.ConditionalKeyword.DATE_RANGE
            et_historyFilter_rangeFrom.isEnabled = true
            et_historyFilter_rangeTo.isEnabled = true
            sp_historyFilter_specificDay.isEnabled = false
            sp_historyFilter_specificMonth.isEnabled = false
            sp_historyFilter_specificYear.isEnabled = false
        }
    }

    private fun setupCurrentSpecificDate() {
        sp_historyFilter_specificYear.setSelection(6)
        sp_historyFilter_specificMonth.setSelection(myCalendar.get(Calendar.MONTH) + 1)

        // Initial Date
        et_historyFilter_rangeFrom
                .setText(SimpleDateFormat("yyyy/MM/1", Locale.US).format(myCalendar.time))
        et_historyFilter_rangeTo.setText(SimpleDateFormat("yyyy/MM/"
                + Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH).toString(),
                Locale.US).format(myCalendar.time))

    }

    override fun onStart() {
        super.onStart()
        mHistoryFilterDialogPresenter.bindView(this)

        userData = mHistoryFilterDialogPresenter.getSignedInUser()!!

        mHistoryFilterDialogPresenter.getAccountList(context!!, userData?.uid)

        populateYears(myCalendar.get(Calendar.YEAR) - 5, myCalendar.get(Calendar.YEAR))

        setupCurrentSpecificDate()

        mHistoryFilterDialogPresenter.getFilterInput(context!!)
    }


    override fun onStop() {
        super.onStop()
        mHistoryFilterDialogPresenter.unbindView()
    }

    override fun setupFilterInput(editor: SharedPreferences) {
        havePreviousType = true

        val filterAccount = editor.getString(Constant.KeyId.FILTER_INPUT_ACCOUNT, "")
        val filterCatType = editor.getString(Constant.KeyId.FILTER_INPUT_CATTYPE, "")
        val filterCategory = editor.getString(Constant.KeyId.FILTER_INPUT_CATEGORY, "")
        val filterRemark = editor.getString(Constant.KeyId.FILTER_INPUT_REMARK, "")
        val filterDateOption = editor.getString(Constant.KeyId.FILTER_INPUT_DATEOPTION, "")
        val filterDay = editor.getString(Constant.KeyId.FILTER_INPUT_DAY, "")
        val filterMonth = editor.getString(Constant.KeyId.FILTER_INPUT_MONTH, "")
        val filterYear = editor.getString(Constant.KeyId.FILTER_INPUT_YEAR, "")
        val filterStartDate = editor.getString(Constant.KeyId.FILTER_INPUT_STARTDATE, "")
        val filterEndDate = editor.getString(Constant.KeyId.FILTER_INPUT_ENDDATE, "")

        val accountNameList = ArrayList<String?>()
        loadedAccountList.forEach { data ->
            accountNameList.add(data.accountName)
        }

        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, accountNameList)
        sp_historyFilter_selectedAcc.setSelection(dataAdapter.getPosition(filterAccount))

        when (filterCatType) {
            Constant.ConditionalKeyword.EXPENSE_STATUS -> {
                mHistoryFilterDialogPresenter.getCategoryList(context!!, userData?.uid,
                        Constant.ConditionalKeyword.EXPENSE_STATUS)
                sp_historyFilter_transType.setSelection(1)
            }
            Constant.ConditionalKeyword.INCOME_STATUS -> {
                mHistoryFilterDialogPresenter.getCategoryList(context!!, userData?.uid,
                        Constant.ConditionalKeyword.INCOME_STATUS)
                sp_historyFilter_transType.setSelection(2)
            }
            else -> {
                val spinnerItem = ArrayList<String>()

                spinnerItem.add(Constant.ConditionalKeyword.ALL_CATEGORY_STATUS)

                val dataAdapterBo = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, spinnerItem)
                dataAdapterBo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                sp_historyFilter_selectedCat.adapter = dataAdapterBo
                sp_historyFilter_selectedCat.isEnabled = false

                sp_historyFilter_transType.setSelection(0)
            }
        }

        if (filterCategory != Constant.ConditionalKeyword.ALL_CATEGORY_STATUS) {
            val categoryNameList = ArrayList<String?>()
            loadedCategoryList.forEach { data ->
                categoryNameList.add(data.categoryName)
            }

            val dataAdapterCat = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, categoryNameList)
            sp_historyFilter_selectedCat.setSelection(dataAdapterCat.getPosition(filterCategory) + 1)
        }

        ac_historyFilter_remarks.setText(filterRemark)

        if (filterDateOption != Constant.ConditionalKeyword.SPECIFIC_DATE) {
            sb_historyFilter_dateOption.toggle()
        }
        setupDateInput()

        if (filterDateOption != Constant.ConditionalKeyword.SPECIFIC_DATE) {
            et_historyFilter_rangeFrom.setText(filterStartDate)
            et_historyFilter_rangeTo.setText(filterEndDate)
        } else {
            havePreviousDay = true

            if (filterYear != Constant.ConditionalKeyword.All_YEAR_STATUS) {
                sp_historyFilter_specificYear.setSelection(tempYearAdapter.getPosition(filterYear))
            } else {
                sp_historyFilter_specificYear.setSelection(0)
            }

            if (filterMonth != Constant.ConditionalKeyword.All_MONTH_STATUS) {
                val cal = Calendar.getInstance()
                cal.time = simpleMonthFormat.parse(filterMonth)
                sp_historyFilter_specificMonth.setSelection(cal.get(Calendar.MONTH) + 1)
            } else {
                sp_historyFilter_specificMonth.setSelection(0)
            }

            setDays()
            if (filterDay != Constant.ConditionalKeyword.All_DAY_STATUS) {
                sp_historyFilter_specificDay.setSelection(filterDay!!.toInt())
            } else {
                sp_historyFilter_specificDay.setSelection(0)
            }
        }
    }

    override fun populateAccountSpinner(accountList: ArrayList<Account>) {
        loadedAccountList = accountList // store to global for create usage later

        val accountNameList = ArrayList<String?>()

        accountList.forEach { data ->
            accountNameList.add(data.accountName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, accountNameList)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_historyFilter_selectedAcc.adapter = dataAdapter

        // Get SharedPreference saved Selection and Set to Spinner Selection
        val selectedAccountSharePref =
                mHistoryFilterDialogPresenter.getSelectedAccount(context!!, userData?.uid)
        val spinnerPosition = dataAdapter.getPosition(selectedAccountSharePref)
        sp_historyFilter_selectedAcc.setSelection(spinnerPosition)
    }

    override fun populateCategorySpinner(categoryList: ArrayList<Category>) {
        loadedCategoryList = categoryList // store to global for create usage later

        val categoryNameList = ArrayList<String?>()

        categoryNameList.add(Constant.ConditionalKeyword.ALL_CATEGORY_STATUS)

        categoryList.forEach { data ->
            categoryNameList.add(data.categoryName)
        }

        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, categoryNameList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sp_historyFilter_selectedCat.adapter = dataAdapter
        sp_historyFilter_selectedCat.isEnabled = true
    }

    private fun setupButton() {
        btn_historyFilter_confirm.setOnClickListener {

            mHistoryFilterDialogPresenter.saveFilterInput(context!!,
                    sp_historyFilter_selectedAcc.selectedItem.toString(),
                    sp_historyFilter_transType.selectedItem.toString(),
                    sp_historyFilter_selectedCat.selectedItem.toString(),
                    ac_historyFilter_remarks.text.toString(),
                    tv_historyFilter_dateOption.text.toString(),
                    sp_historyFilter_specificDay.selectedItem.toString(),
                    sp_historyFilter_specificMonth.selectedItem.toString(),
                    sp_historyFilter_specificYear.selectedItem.toString(),
                    et_historyFilter_rangeFrom.text.toString(),
                    et_historyFilter_rangeTo.text.toString())

            if (tv_historyFilter_dateOption.text == Constant.ConditionalKeyword.SPECIFIC_DATE) {
                onFilterTriggerDialog.filterInputSubmit()
                dialog?.dismiss()
            } else {
                onFilterTriggerDialog.filterInputSubmit()
                dialog?.dismiss()
            }
        }

        btn_historyFilter_cancel.setOnClickListener {
            onFilterTriggerDialog.filterInputCancel()
            dialog?.dismiss()
        }
    }

    private fun setupDialogInitialUi() {
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet)

            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
        }

        tv_historyFilter_dateOption.text = Constant.ConditionalKeyword.SPECIFIC_DATE

        val transTypeList = ArrayList<String>()
        transTypeList.add(Constant.ConditionalKeyword.All_TYPE_STATUS)
        transTypeList.add(Constant.ConditionalKeyword.EXPENSE_STATUS)
        transTypeList.add(Constant.ConditionalKeyword.INCOME_STATUS)

        val transTypeAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, transTypeList)
        transTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_historyFilter_transType.adapter = transTypeAdapter

        val monthList = ArrayList<String>()
        monthList.add(Constant.ConditionalKeyword.All_MONTH_STATUS)
        monthList.add(Constant.DefaultValue.JAN)
        monthList.add(Constant.DefaultValue.FEB)
        monthList.add(Constant.DefaultValue.MAR)
        monthList.add(Constant.DefaultValue.APR)
        monthList.add(Constant.DefaultValue.MAY)
        monthList.add(Constant.DefaultValue.JUN)
        monthList.add(Constant.DefaultValue.JUL)
        monthList.add(Constant.DefaultValue.AUG)
        monthList.add(Constant.DefaultValue.SEP)
        monthList.add(Constant.DefaultValue.OCT)
        monthList.add(Constant.DefaultValue.NOV)
        monthList.add(Constant.DefaultValue.DEC)

        // Creating adapter for month spinner
        val monthAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, monthList)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_historyFilter_specificMonth.adapter = monthAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onFilterTriggerDialog = this.targetFragment as OnFilterTrigger
        } catch (e: ClassCastException) {
            Log.e("", e.toString())
        }
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.HISTORY_DIALOG_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private var fixDays: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            if (!havePreviousDay) {
                setDays()
            } else {
                if (havePreviousDayCount > 0) {
                    havePreviousDay = false
                }
                havePreviousDayCount += 1
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    private fun setDays() {
        if (sp_historyFilter_specificYear.selectedItem.toString() ==
                Constant.ConditionalKeyword.All_YEAR_STATUS ||
                sp_historyFilter_specificMonth.selectedItem.toString() ==
                Constant.ConditionalKeyword.All_MONTH_STATUS) {

            sp_historyFilter_specificDay.isEnabled = false
            sp_historyFilter_specificMonth.isEnabled = false

            val daysArray = arrayOfNulls<String>(1)
            daysArray[0] = Constant.ConditionalKeyword.All_DAY_STATUS

            val spinnerArrayAdapter = ArrayAdapter(context!!,
                    android.R.layout.simple_spinner_dropdown_item, daysArray)
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            sp_historyFilter_specificDay.adapter = spinnerArrayAdapter

            sp_historyFilter_specificMonth.setSelection(0)
        }

        if (sp_historyFilter_specificYear.selectedItem.toString() !=
                Constant.ConditionalKeyword.All_YEAR_STATUS &&
                sp_historyFilter_specificMonth.selectedItem.toString() ==
                Constant.ConditionalKeyword.All_MONTH_STATUS) {
            sp_historyFilter_specificMonth.isEnabled = true
        }

        if (sp_historyFilter_specificMonth.selectedItem.toString() !=
                Constant.ConditionalKeyword.All_MONTH_STATUS &&
                sp_historyFilter_specificYear.selectedItem.toString() !=
                Constant.ConditionalKeyword.All_YEAR_STATUS) {
            sp_historyFilter_specificDay.isEnabled = sb_historyFilter_dateOption.isChecked

            val year = Integer.parseInt(sp_historyFilter_specificYear.selectedItem.toString())
            val month = sp_historyFilter_specificMonth.selectedItemPosition
            val mycal = GregorianCalendar(year, month - 1, 1)

            // Get the number of days in that month
            val daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)

            val daysArray = arrayOfNulls<String>(daysInMonth + 1)

            daysArray[0] = Constant.ConditionalKeyword.All_DAY_STATUS

            for (k in 1 until daysInMonth + 1)
                daysArray[k] = "" + (k)

            val spinnerArrayAdapter = ArrayAdapter(context!!,
                    android.R.layout.simple_spinner_dropdown_item, daysArray)
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            sp_historyFilter_specificDay.adapter = spinnerArrayAdapter
        }
    }


    private fun populateYears(minYear: Int, maxYear: Int) {
        val yearsArray = arrayOfNulls<String>(maxYear - minYear + 2)

        yearsArray[0] = Constant.ConditionalKeyword.All_YEAR_STATUS

        var count = 1
        for (i in minYear..maxYear) {
            yearsArray[count] = "" + i
            count++
        }

        val spinnerArrayAdapter = ArrayAdapter(context!!,
                android.R.layout.simple_spinner_dropdown_item, yearsArray)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_historyFilter_specificYear.adapter = spinnerArrayAdapter

        tempYearAdapter = spinnerArrayAdapter
    }

    private fun setupStartDatePicker() {
        val dateDialog =
                DatePickerDialog(context!!,
                        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                            enableAllUiComponent()
                            myCalendar.set(Calendar.YEAR, year)
                            myCalendar.set(Calendar.MONTH, monthOfYear)
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            et_historyFilter_rangeFrom.setText(simpleDateFormat.format(myCalendar.time))
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))

        dateDialog.setOnCancelListener {
            enableAllUiComponent()
        }

        et_historyFilter_rangeFrom.setOnClickListener {
            disableAllUiComponent()
            dateDialog.show()
        }
        et_historyFilter_rangeFrom.isEnabled = false
    }

    private fun setupEndDatePicker() {
        val dateDialog =
                DatePickerDialog(context!!,
                        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                            enableAllUiComponent()
                            myCalendar.set(Calendar.YEAR, year)
                            myCalendar.set(Calendar.MONTH, monthOfYear)
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            et_historyFilter_rangeTo.setText(simpleDateFormat.format(myCalendar.time))
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))

        dateDialog.setOnCancelListener {
            enableAllUiComponent()
        }

        et_historyFilter_rangeTo.setOnClickListener {
            disableAllUiComponent()
            dateDialog.show()
        }
        et_historyFilter_rangeTo.isEnabled = false
    }

    private fun disableAllUiComponent() {
        sp_historyFilter_selectedAcc.isEnabled = false
        sp_historyFilter_transType.isEnabled = false
        sp_historyFilter_selectedCat.isEnabled = false
        ac_historyFilter_remarks.isEnabled = false
        tv_historyFilter_dateOption.isEnabled = false
        sb_historyFilter_dateOption.isEnabled = false
        et_historyFilter_rangeFrom.isEnabled = false
        et_historyFilter_rangeTo.isEnabled = false
    }

    private fun enableAllUiComponent() {
        sp_historyFilter_selectedAcc.isEnabled = true
        sp_historyFilter_transType.isEnabled = true
        sp_historyFilter_selectedCat.isEnabled = true
        ac_historyFilter_remarks.isEnabled = true
        tv_historyFilter_dateOption.isEnabled = true
        sb_historyFilter_dateOption.isEnabled = true
        et_historyFilter_rangeFrom.isEnabled = true
        et_historyFilter_rangeTo.isEnabled = true
    }
}