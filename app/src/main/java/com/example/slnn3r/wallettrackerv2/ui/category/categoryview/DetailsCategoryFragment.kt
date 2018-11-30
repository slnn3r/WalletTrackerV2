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
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.ui.category.categorypresenter.DetailsCategoryPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.fragment_details_category.*

class DetailsCategoryFragment : Fragment(), CategoryViewInterface.DetailsCategoryView {

    private val mDetailsCategoryViewPresenter: DetailsCategoryPresenter = DetailsCategoryPresenter()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser
    private lateinit var categoryArgData: Category

    private var initialLaunch = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_detailsCat_title)
        return inflater.inflate(R.layout.fragment_details_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialUi()
        setupSwitchButton()
        setupCatNameEditText()
    }

    override fun onStart() {
        super.onStart()
        mDetailsCategoryViewPresenter.bindView(this)

        if (initialLaunch) {
            userData = mDetailsCategoryViewPresenter.getSignedInUser()!!

            mDetailsCategoryViewPresenter.checkSelectedCategoryType(categoryArgData.categoryType)
            mDetailsCategoryViewPresenter.checkCategoryStatus(categoryArgData.categoryStatus)
        }
        initialLaunch = false
    }

    override fun onStop() {
        super.onStop()
        mDetailsCategoryViewPresenter.unbindView()
    }

    override fun setupFloatingActionButton() {
        fb_detailsCat.mainFabOpenedBackgroundColor =
                resources.getColor(R.color.colorPrimary)
        fb_detailsCat.mainFabClosedBackgroundColor =
                resources.getColor(R.color.colorPrimaryDark)

        fb_detailsCat.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_edit, R.drawable.ic_edit)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightGreen))
                        .setLabel(getString(R.string.fb_action_edit_title))
                        .setLabelColor(resources.getColor(R.color.colorLightGreen))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsCat.addActionItem(
                SpeedDialActionItem.Builder(R.id.fb_action_delete, R.drawable.ic_delete)
                        .setFabBackgroundColor(resources.getColor(R.color.colorLightRed))
                        .setLabel(getString(R.string.fb_action_delete_title))
                        .setLabelColor(resources.getColor(R.color.colorLightRed))
                        .setLabelBackgroundColor(resources.getColor(R.color.colorPrimaryLight))
                        .create()
        )

        fb_detailsCat.setOnActionSelectedListener { speedDialActionItem ->
            mDetailsCategoryViewPresenter.actionCheck(speedDialActionItem)
            false
        }
    }

    override fun setupFloatingDefaultButton() {
        fb_detailsCat.mainFabClosedBackgroundColor =
                resources.getColor(R.color.colorPrimaryDark)
        fb_detailsCat.setMainFabClosedDrawable(resources.getDrawable(R.drawable.ic_edit))

        fb_detailsCat.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {

                mCustomConfirmationDialog.confirmationDialog(context!!,
                        getString(R.string.cd_detailsCat_editSubmit_title),
                        getString(R.string.cd_detailsCat_editSubmit_desc),
                        resources.getDrawable(R.drawable.ic_warning),
                        DialogInterface.OnClickListener { _, _ ->

                            val categoryInput =
                                    Category(categoryArgData.categoryId,
                                            et_detailsCat_catName.text.toString(),
                                            tv_detailsCat_catType_selection.text.toString(),
                                            categoryArgData.categoryStatus, categoryArgData.userUid)

                            mDetailsCategoryViewPresenter
                                    .editAccount(context!!, categoryInput)
                        }
                ).show()
                return false
            }

            override fun onToggleChanged(isOpen: Boolean) {
            }
        })
    }

    override fun switchButtonExpenseMode() {
        tv_detailsCat_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
        sb_detailsCat_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
    }

    override fun switchButtonIncomeMode() {
        tv_detailsCat_catType_selection.text = Constant.ConditionalKeyword.INCOME_STATUS
        sb_detailsCat_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
    }

    override fun switchButtonToggle() {
        sb_detailsCat_catType.toggle()
    }

    override fun validCategoryNameInput() {
        tl_detailsCat_catName.isErrorEnabled = false
        tl_detailsCat_catName.error = null
    }

    override fun invalidCategoryNameInput(errorMessage: String) {
        tl_detailsCat_catName.error = errorMessage
    }

    override fun showFloatingButton() {
        fb_detailsCat.show()
    }

    override fun hideFloatingButton() {
        fb_detailsCat.hide()
    }

    override fun editCategoryPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsCat_editSubmit_title),
                getString(R.string.cd_detailsCat_editSubmit_desc),
                resources.getDrawable(R.drawable.ic_warning),
                DialogInterface.OnClickListener { _, _ ->
                    val categoryInput =
                            Category(categoryArgData.categoryId,
                                    et_detailsCat_catName.text.toString(),
                                    tv_detailsCat_catType_selection.text.toString(),
                                    categoryArgData.categoryStatus, categoryArgData.userUid)

                    mDetailsCategoryViewPresenter
                            .editAccount(context!!, categoryInput)
                }
        ).show()
    }

    override fun deleteCategoryPrompt() {
        mCustomConfirmationDialog.confirmationDialog(context!!,
                getString(R.string.cd_detailsCat_deleteSubmit_title),
                getString(R.string.cd_detailsCat_deleteSubmit_desc),
                resources.getDrawable(R.drawable.ic_warning),
                DialogInterface.OnClickListener { _, _ ->
                    mDetailsCategoryViewPresenter
                            .deleteAccount(context!!, categoryArgData.categoryId)
                }
        ).show()
    }

    override fun editCategorySuccess() {
        Snackbar.make(view!!,
                getString(R.string.detailsCat_edited_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun deleteCategorySuccess() {
        Snackbar.make(view!!,
                getString(R.string.detailsCat_deleted_message), Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.DETAILS_CATEGORY_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        // Receive Argument
        val categoryDataJson = arguments?.getString(Constant.KeyId.CATEGORY_DETAILS_ARG)

        // user GSON convert to object
        val gson = Gson()
        categoryArgData = gson.fromJson<Category>(categoryDataJson, Category::class.java)

        et_detailsCat_catName.setText(categoryArgData.categoryName)

        tv_detailsCat_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
    }

    private fun setupSwitchButton() {
        sb_detailsCat_catType.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            mDetailsCategoryViewPresenter.checkSwitchButton(sb_detailsCat_catType.isChecked)
            mDetailsCategoryViewPresenter.validateCategoryNameInput(
                    context!!, userData.uid,
                    et_detailsCat_catName.text.toString(), categoryArgData.categoryId,
                    tv_detailsCat_catType_selection.text.toString())
        }
    }

    private fun setupCatNameEditText() {
        et_detailsCat_catName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDetailsCategoryViewPresenter.validateCategoryNameInput(
                        context!!, userData.uid,
                        et_detailsCat_catName.text.toString(), categoryArgData.categoryId,
                        tv_detailsCat_catType_selection.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("", "")
            }
        })
    }
}
