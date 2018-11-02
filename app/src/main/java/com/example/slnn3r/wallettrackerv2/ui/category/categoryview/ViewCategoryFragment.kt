package com.example.slnn3r.wallettrackerv2.ui.category.categoryview

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.ui.category.categoryadapter.CategoryListAdapter
import com.example.slnn3r.wallettrackerv2.ui.category.categorypresenter.ViewCategoryPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_view_category.*

class ViewCategoryFragment : Fragment(), CategoryViewInterface.ViewCategoryView {

    private val mViewCategoryViewPresenter: ViewCategoryPresenter = ViewCategoryPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_viewCat_title)
        return inflater.inflate(R.layout.fragment_view_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialUi() // Setup Category type before call getCategoryFunction

        fb_viewCat_createCat.setOnClickListener {

            val navController = view.findNavController()
            val bundle = Bundle()
            bundle.putString(Constant.KeyId.CATEGORY_CREATE_ARG,
                    tv_viewCat_catType_selection.text.toString())
            navController
                    .navigate(R.id.action_viewCategoryFragment_to_createCategoryFragment, bundle)
        }

        sb_viewCat_catType.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            mViewCategoryViewPresenter.checkSwitchButton(sb_viewCat_catType.isChecked)
            mViewCategoryViewPresenter.getCategoryList(context!!, userData.uid,
                    tv_viewCat_catType_selection.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        mViewCategoryViewPresenter.bindView(this)
        userData = mViewCategoryViewPresenter.getSignedInUser()!!

        mViewCategoryViewPresenter.checkToggle(sb_viewCat_catType.isChecked)

        mViewCategoryViewPresenter.getCategoryList(context!!, userData.uid,
                tv_viewCat_catType_selection.text.toString())
    }

    override fun onStop() {
        super.onStop()
        mViewCategoryViewPresenter.unbindView()
    }

    override fun switchButtonExpenseMode() {
        tv_viewCat_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
        sb_viewCat_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
    }

    override fun switchButtonIncomeMode() {
        tv_viewCat_catType_selection.text = Constant.ConditionalKeyword.INCOME_STATUS
        sb_viewCat_catType.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
    }

    override fun switchButtonToggle() {
        sb_viewCat_catType.toggle()
    }

    override fun populateCategoryRecycleView(categoryList: ArrayList<Category>) {
        val catListRecyclerView =
                (context as Activity).findViewById(R.id.rv_viewCat_catList) as RecyclerView

        catListRecyclerView.layoutManager = LinearLayoutManager(context)
        catListRecyclerView.adapter = CategoryListAdapter(categoryList)
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.VIEW_CATEGORY_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupInitialUi() {
        tv_viewCat_catType_selection.text = Constant.ConditionalKeyword.EXPENSE_STATUS
    }
}
