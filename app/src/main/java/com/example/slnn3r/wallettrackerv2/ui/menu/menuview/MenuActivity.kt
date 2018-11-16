package com.example.slnn3r.wallettrackerv2.ui.menu.menuview

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginActivity
import com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter.MenuViewPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        MenuViewInterface.MenuView {

    private val mMenuPresenter: MenuViewPresenter = MenuViewPresenter()
    private val mCustomConfirmationDialog: CustomAlertDialog = CustomAlertDialog()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var navigationView: NavigationView

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var userData: FirebaseUser

    private val initialScreen: Int = R.id.dashboardFragment
    private var isNavigated: String = "NavDrawer" // Set Initial Navigation Status to false
    private var selectedHistoryScreen = ""

    private var doubleBackToExitPressedOnce = false

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        setupNavigation()
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
        val currentScreen = findNavController(R.id.navMenu).currentDestination!!.id
        mMenuPresenter.checkNavigationStatus(isNavigated, selectedHistoryScreen,
                true, currentScreen, drawer_layout.isDrawerOpen(GravityCompat.START),
                doubleBackToExitPressedOnce)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        mMenuPresenter.navigationDrawerSelection(item)
        return true
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navMenu).navigateUp()

    override fun setupNavigationFlow() {
        mMenuPresenter.hideKeyboard(this) // hide any open keyboard to ensure navigated screen free from previous opened keyboard

        onSupportNavigateUp() // call the back function (auto navigate to previous screen) of the navigation graph

        val currentScreen = findNavController(R.id.navMenu).currentDestination!!.id

        if (currentScreen == initialScreen) { // if Current screen is initial screen (Dashboard), switch Navigation Up button back to Drawer function
            setupDrawerMode()
        }
    }

    override fun displayDrawerDropDown() {
        val menu = navigationView.menu

        val b = !menu.findItem(R.id.nav_sub_line_graph).isVisible
        //setting submenus visible state
        menu.findItem(R.id.nav_sub_line_graph).isVisible = b
        menu.findItem(R.id.nav_sub_bar_graph).isVisible = b
    }

    override fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun proceedToAccountScreen() {
        Handler().postDelayed({
            setupNavigationMode()
            Navigation.findNavController(this, R.id.navMenu)
                    .navigate(R.id.action_dashboardFragment_to_viewAccountFragment)
        }, 300)
    }

    override fun proceedToCategoryScreen() {
        Handler().postDelayed({
            setupNavigationMode()
            Navigation.findNavController(this, R.id.navMenu)
                    .navigate(R.id.action_dashboardFragment_to_viewCategoryFragment)
        }, 300)
    }

    override fun proceedToHistoryScreen() {
        Handler().postDelayed({
            setupNavigationMode()
            Navigation.findNavController(this, R.id.navMenu)
                    .navigate(R.id.action_dashboardFragment_to_historyFragment)
        }, 300)
    }

    override fun proceedToLineReportScreen() {
    }

    override fun proceedToBarReportScreen() {
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

    override fun signOutSuccess() {
        Toast.makeText(this, "Sign Out Successful", Toast.LENGTH_SHORT).show()

        mMenuPresenter.clearSharePreferenceData(this, userData.uid) // remove sharePreference

        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayDoubleTabExitMessage() {
        Toast.makeText(this, "Click One More to Exit", Toast.LENGTH_SHORT).show()
        doubleBackToExitPressedOnce = true
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun superOnPressBack() {
        super.onBackPressed()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.MENU_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(this, message).show()
        return
    }

    private fun setupNavigation() {
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // Setup Custom Navigation Drawer Button Listener
        toolbar.setNavigationOnClickListener {
            mMenuPresenter.checkNavigationStatus(isNavigated, selectedHistoryScreen,
                    false, null, false, doubleBackToExitPressedOnce)
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun displayUserDataToNavDrawer() {
        val headerView = navigationView.getHeaderView(0)

        val navHeaderUserName = headerView.findViewById(R.id.tv_navHeader_username) as TextView
        val navHeaderUserEmail = headerView.findViewById(R.id.tv_navHeader_user_email) as TextView
        val navHeaderUserPicture = headerView.findViewById(R.id.iv_navHeader_user_image) as ImageView

        navHeaderUserName.text = userData.displayName
        navHeaderUserEmail.text = userData.email
        Picasso.get().load(userData.photoUrl).into(navHeaderUserPicture)
    }

    private fun setupDrawerMode() {
        isNavigated = "NavDrawer"
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        animateIcon(1, 0, 800) // with Animation no need to deal with any Icon Change stuff
    }

    // If Use Loading indication then no need this implementation dy (NOT REALLY)
    fun setupToDisable() {
        if (isNavigated != "HistoryNavGraph") { // If didnt use BottomNavigaton then no need everything relate to this
            isNavigated = "NavDisable"
        }
    }

    fun setupNavigationMode() {
        if (isNavigated != "HistoryNavGraph") {
            isNavigated = "MenuNavGraph"
        }

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        animateIcon(0, 1, 800) // with Animation no need to deal with any Icon Change stuff
    }

    private fun animateIcon(start: Int, end: Int, duration: Int) {
        val anim = ValueAnimator.ofFloat(start.toFloat(), end.toFloat())

        anim.addUpdateListener { animation ->
            val slideOffset = animation?.animatedValue as Float
            toggle.onDrawerSlide(drawer_layout, slideOffset)
        }

        anim.interpolator = DecelerateInterpolator()
        anim.duration = duration.toLong()
        anim.start()
    }
}
