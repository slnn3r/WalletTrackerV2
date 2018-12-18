package com.example.slnn3r.wallettrackerv2.ui.menu.menuview

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardadapter.dashboardAdapterClickCount
import com.example.slnn3r.wallettrackerv2.ui.login.loginview.LoginActivity
import com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter.MenuViewPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
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
    private var userData: FirebaseUser? = null

    private val initialScreen: Int = R.id.dashboardFragment
    private var isNavigated: String = Constant.NavigationKey.NAV_DRAWER // Set Initial Navigation Status to false

    private var doubleBackToExitPressedOnce = false

    private lateinit var toggle: ActionBarDrawerToggle

    private var initialLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        setupNavigation()
    }

    override fun onPause() {
        super.onPause()
        // show blank screen when User go to Recent Apps Screen, to avoid sensitive transaction data being reveal
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun onResume() {
        super.onResume()
        // when resume clear the blank screen setting as it may disable screenshot feature
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    override fun onStart() {
        super.onStart()
        mMenuPresenter.bindView(this)

        if (initialLaunch) {
            mGoogleSignInClient = mMenuPresenter.getGoogleSignInClient(this)
            userData = mMenuPresenter.getSignedInUser()

            navigationView = findViewById<View>(R.id.nav_view) as NavigationView

            displayUserDataToNavDrawer()
            displayWelcomeMessage()
            mMenuPresenter.checkBackupSetting(this, userData?.uid)
        }
        initialLaunch = false
    }

    override fun onStop() {
        super.onStop()
        mMenuPresenter.unbindView() // Unbind the view when it stopped
    }

    override fun onPause() {
        super.onPause();
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
    }

    override fun onResume() {
        super.onResume()
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }

    override fun onBackPressed() {
        val currentScreen = findNavController(R.id.navMenu).currentDestination?.id
        mMenuPresenter.checkNavigationStatus(isNavigated, true, currentScreen,
                drawer_layout.isDrawerOpen(GravityCompat.START), doubleBackToExitPressedOnce)
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

        val currentScreen = findNavController(R.id.navMenu).currentDestination?.id

        if (currentScreen == initialScreen) { // if Current screen is initial screen (Dashboard), switch Navigation Up button back to Drawer function
            setupDrawerMode()
        }
    }

    override fun openDrawer() {
        drawer_layout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    override fun proceedToAccountScreen() {
        setupNavigationMode()
        Navigation.findNavController(this, R.id.navMenu)
                .navigate(R.id.action_dashboardFragment_to_viewAccountFragment)
    }

    override fun proceedToCategoryScreen() {
        setupNavigationMode()
        Navigation.findNavController(this, R.id.navMenu)
                .navigate(R.id.action_dashboardFragment_to_viewCategoryFragment)
    }

    override fun proceedToHistoryScreen() {
        setupNavigationMode()
        Navigation.findNavController(this, R.id.navMenu)
                .navigate(R.id.action_dashboardFragment_to_historyFragment)
    }

    override fun proceedToReportScreen() {
        setupNavigationMode()
        Navigation.findNavController(this, R.id.navMenu)
                .navigate(R.id.action_dashboardFragment_to_reportFragment)
    }

    override fun executeBackupOnBackground() {
        mMenuPresenter.backupDataManually(this, userData?.uid)
    }

    override fun proceedToSettingScreen() {
        setupNavigationMode()
        Navigation.findNavController(this, R.id.navMenu)
                .navigate(R.id.action_dashboardFragment_to_settingFragment)
    }

    override fun proceedToSignOut() {
        mCustomConfirmationDialog.confirmationDialog(this,
                getString(R.string.sign_out_dialog_title),
                getString(R.string.sign_out_dialog_message),
                resources.getDrawable(android.R.drawable.ic_dialog_alert),
                DialogInterface.OnClickListener { _, _ ->
                    dashboardAdapterClickCount += 1 // just for prevent crash during click sign out then spam the Recent Transaction List
                    mMenuPresenter.stopBackupDataPeriodically(this)
                    mMenuPresenter.executeGoogleSignOut(mGoogleSignInClient)
                }).show()
    }

    override fun signOutSuccess() {
        mMenuPresenter.clearSelectedAccountSharePreference(this, userData?.uid) // remove sharePreference

        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayDoubleTabExitMessage() {
        Snackbar.make(findViewById<View>(android.R.id.content),
                getString(R.string.doubleTabExit_label), Snackbar.LENGTH_SHORT)
                .show()

        doubleBackToExitPressedOnce = true
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun updateDrawerBackupDateTime(backupDateTime: String?) {
        val menu = navigationView.menu
        val navSyncData = menu.findItem(R.id.navDrawer_section_2)

        navSyncData.title = getString(R.string.backup_drawerDateTime, backupDateTime)
    }


    override fun executePeriodicalBackup() {
        mMenuPresenter.backupDataPeriodically(this, userData?.uid)
    }

    override fun backupOnBackgroundStart() {
        Snackbar.make(findViewById<View>(android.R.id.content),
                getString(R.string.manualBackup_start_message), Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun backupPeriodicallyStart() {
        Handler().postDelayed({
            Snackbar.make(findViewById<View>(android.R.id.content),
                    getString(R.string.autoBackup_start_message), Snackbar.LENGTH_SHORT)
                    .show()
        }, 2000)
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

        drawer_layout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                mMenuPresenter.checkBackupDateTime(applicationContext, userData?.uid)
            }
        })

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        // Setup Custom Navigation Drawer Button Listener
        toolbar.setNavigationOnClickListener {
            mMenuPresenter.checkNavigationStatus(isNavigated, false,
                    null, false, doubleBackToExitPressedOnce)
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun displayUserDataToNavDrawer() {
        val headerView = navigationView.getHeaderView(0)

        val navHeaderUserName = headerView.findViewById(R.id.tv_navHeader_username) as TextView
        val navHeaderUserEmail = headerView.findViewById(R.id.tv_navHeader_user_email) as TextView
        val navHeaderUserPicture = headerView.findViewById(R.id.iv_navHeader_user_image) as ImageView

        navHeaderUserName.text = userData?.displayName
        navHeaderUserEmail.text = userData?.email
        Picasso.get().load(userData?.photoUrl).into(navHeaderUserPicture)
    }

    private fun displayWelcomeMessage() {
        Snackbar.make(findViewById<View>(android.R.id.content),
                getString(R.string.welcome_message, userData?.displayName), Snackbar.LENGTH_SHORT)
                .show()
    }

    private fun setupDrawerMode() {
        isNavigated = Constant.NavigationKey.NAV_DRAWER
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        animateIcon(1, 0, 800) // with Animation no need to deal with any Icon Change stuff
    }

    fun setupToDisable() {
        isNavigated = Constant.NavigationKey.NAV_DISABLE
    }

    fun setupNavigationMode() {
        isNavigated = Constant.NavigationKey.NAV_MENU

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
