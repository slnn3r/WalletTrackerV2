package com.example.slnn3r.wallettrackerv2.ui.category.categoryview

import android.app.Activity
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.ui.category.categorypresenter.CreateCategoryPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_create_category.*
import java.util.*

class CreateCategoryFragment : Fragment(), CategoryViewInterface.CreateCategoryView {

    private val mCreateCategoryViewPresenter: CreateCategoryPresenter = CreateCategoryPresenter()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_createCat_title)
        return inflater.inflate(R.layout.fragment_create_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialUi()
        setupCreateButton()
        setupSwitchButton()
        setupCatNameEditText()
    }

    override fun onStart() {
        super.onStart()
        mCreateCategoryViewPresenter.bindView(this)
        userData = mCreateCategoryViewPresenter.getSignedInUser()!!

        // Receive Selected Category Type Argument from View screen
        val selectedCategoryType = arguments?.getString(Constant.KeyId.CATEGORY_CREATE_ARG)
        mCreateCategoryViewPresenter.checkSelectedCategoryType(selectedCategoryType!!)
    }

    override fun onStop() {
        super.onStop()
        mCreateCategoryViewPresenter.unbindView()
    }

    override fun switchButtonExpenseMode() {
        tv_createCat_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
        sb_createCat_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
    }

    override fun switchButtonIncomeMode() {
        tv_createCat_catType_selection.text = Constant.ConditionalKeyword.INCOME_STATUS
        sb_createCat_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
    }

    override fun switchButtonToggle() {
        sb_createCat_catType.toggle()
    }

    override fun validCategoryNameInput() {
        tl_createCat_catName.isErrorEnabled = false
        tl_createCat_catName.error = null
    }

    override fun invalidCategoryNameInput(errorMessage: String) {
        tl_createCat_catName.error = errorMessage
    }

    override fun showFloatingButton() {
        fb_createCat.show()
    }

    override fun hideFloatingButton() {
        fb_createCat.hide()
    }

    override fun createCategorySuccess() {
        Snackbar.make(view!!,
                getString(R.string.createCat_created_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.CREATE_CATEGORY_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        tl_createCat_catName.error = getString(R.string.catName_empty_invalid_message)
        fb_createCat.hide()
    }

    private fun setupCreateButton() {
        fb_createCat.setOnClickListener {
            createButtonSubmit()
        }
    }

    private fun setupSwitchButton() {
        sb_createCat_catType.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            mCreateCategoryViewPresenter.checkSwitchButton(sb_createCat_catType.isChecked)
            mCreateCategoryViewPresenter.validateCategoryNameInput(
                    context!!, userData.uid,
                    et_createCat_catName.text.toString(), null,
                    tv_createCat_catType_selection.text.toString())
        }
    }

    private fun setupCatNameEditText() {
        et_createCat_catName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCreateCategoryViewPresenter.validateCategoryNameInput(
                        context!!, userData.uid,
                        et_createCat_catName.text.toString(), null,
                        tv_createCat_catType_selection.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("", "")
            }
        })
    }

    private fun createButtonSubmit() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_createCat_createSubmit_title),
                getString(R.string.cd_createCat_createSubmit_desc),
                resources.getDrawable(R.drawable.ic_info),
                DialogInterface.OnClickListener { _, _ ->
                    val uniqueID = UUID.randomUUID().toString()

                    val categoryInput =
                            Category(uniqueID, et_createCat_catName.text.toString(),
                                    tv_createCat_catType_selection.text.toString(),
                                    Constant.ConditionalKeyword.NON_DEFAULT_STATUS, userData.uid)

                    mCreateCategoryViewPresenter.createCategory(context!!, categoryInput)
                }).show()
    }
}
