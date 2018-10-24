package com.example.slnn3r.wallettrackerv2.ui.menu.menuview

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginActivity
import com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter.MenuPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        MenuViewInterface.MenuView {

    private val mMenuPresenter: MenuPresenter = MenuPresenter()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var navigationView:NavigationView

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var userData: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        mMenuPresenter.bindView(this)
        mGoogleSignInClient = mMenuPresenter.getGoogleSignInClient(this)
        userData = mMenuPresenter.getSignedInUser()!!

        navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        displayUserDataToNavDrawer()
    }

    override fun onStop() {
        super.onStop()
        mMenuPresenter.unbindView() // Unbind the view when it stopped
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        mMenuPresenter.navigationDrawerSelection(item)
        return true
    }

    override fun signOutSuccess() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.MENU_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(this, message).show()
        return
    }

    private fun displayUserDataToNavDrawer() {
        val headerView = navigationView.getHeaderView(0)

        val navHeaderUserName
                = headerView.findViewById(R.id.tv_navHeader_username) as TextView
        val navHeaderUserEmail
                = headerView.findViewById(R.id.tv_navHeader_user_email) as TextView
        val navHeaderUserPicture
                = headerView.findViewById(R.id.iv_navHeader_user_image) as ImageView

        navHeaderUserName.text = userData.displayName
        navHeaderUserEmail.text = userData.email
        Picasso.get().load(userData.photoUrl).into(navHeaderUserPicture)
    }

    override fun proceedToSignOut() {
        mCustomConfirmationDialog.confirmationDialog(this,
                getString(R.string.sign_out_dialog_title),
                getString(R.string.sign_out_dialog_message),
                resources.getDrawable(android.R.drawable.ic_dialog_alert),
                DialogInterface.OnClickListener { _, _ ->
                    mMenuPresenter.executeGoogleSignOut(mGoogleSignInClient)
                }).show()
    }

    override fun displayDrawerDropDown() {

        val menu = navigationView.menu

        val b = !menu.findItem(R.id.nav_sub_line_graph).isVisible
        //setting submenus visible state
        menu.findItem(R.id.nav_sub_line_graph).isVisible = b
        menu.findItem(R.id.nav_sub_bar_graph).isVisible = b
    }

    override fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun proceedToAccountScreen() {
        Navigation.findNavController(this, R.id.navMenu).navigate(R.id.action_dashboardFragment_to_viewAccountFragment)
    }

    override fun proceedToCategoryScreen() {
        Navigation.findNavController(this, R.id.navMenu).navigate(R.id.action_dashboardFragment_to_viewCategoryFragment)
    }

    override fun proceedToHistoryScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun proceedToReportScreen() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
