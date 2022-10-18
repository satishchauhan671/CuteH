package com.digipanther.cuteh.activity

import android.app.Activity
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.bumptech.glide.request.RequestOptions
import com.digipanther.cuteh.R
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.databinding.ActivityMainBinding
import com.digipanther.cuteh.dbHelper.UserDataHelper
import com.digipanther.cuteh.fragment.DashboardFragment
import com.digipanther.cuteh.model.UserInfoModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView


class DashboardActivity : MyActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var dashboardBinding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationView: NavigationView
    private var mActivity: Activity? = null
    private var actionBar: ActionBar? = null
    private lateinit var Userprofile_img: CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
       // this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
        mActivity = this@DashboardActivity
        setSupportActionBar(dashboardBinding.appBarHome.toolbar)
        bottomNavigationView = dashboardBinding.appBarHome.contentMain.bottomNavigation
        navigationView = dashboardBinding.navView
        val toggle = ActionBarDrawerToggle(
            this,
            dashboardBinding.drawerLayout,
            dashboardBinding.appBarHome.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        dashboardBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        actionBar = supportActionBar
        if (actionBar != null) {
            actionBar!!.setHomeButtonEnabled(true)
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        val header = navigationView.getHeaderView(0)
        Userprofile_img = header.findViewById(R.id.Image_Userprofile)
        val name = header.findViewById<TextView>(R.id.empname)
        val mobileNo = header.findViewById<TextView>(R.id.mobileNo)

        val model: UserInfoModel? = UserDataHelper.getLogin(this)
        if (model != null) {
            var userName: String? = null
            userName = if (Utility.isNullOrEmpty(model.userName)) "N/A" else model.userName
            name.text = userName
            mobileNo.text = "Mobile : " + model.mobile + ""
            if (model.img != null && !model.img.equals("")) {
                Userprofile_img?.setVisibility(
                    View.VISIBLE
                )
                Glide.with(this)
                    .applyDefaultRequestOptions(
                        RequestOptions()
                            .error(R.drawable.ic_user_image)
                    )
                    .load(Uri.parse(model.img))
                    .into(Userprofile_img)
            }
        }
       /* navigationView.footer_item.text =
            "Version: " + BuildConfig.VERSION_NAME + "   Android: v" + Build.VERSION.RELEASE*/
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null //====for color=========
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnItemSelectedListener { item ->

            item.isChecked = item.isChecked
            dashboardBinding.drawerLayout.closeDrawer(GravityCompat.START)
            var fragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_dashboard -> {
                    actionBar!!.title = "Dashboard"
                    actionBar!!.subtitle = ""
                    fragment = DashboardFragment(dashboardBinding)
                    navigationView.menu
                    navigationView.menu.findItem(R.id.navigation_dashboard).isChecked = true
                    Utility.replaceFragment(
                        fragment,
                        supportFragmentManager,
                        R.id.layout_fragment
                    )
                }

                R.id.nav_ci -> {
                    Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT)
                        .show()
                   /*  fragment = SearchCustomerCIFragment(dashboardBinding)
                     navigationView.menu.findItem(R.id.navigation_ci).isChecked = true
                     Utility.replaceFragment(
                         fragment,
                         supportFragmentManager,
                         R.id.layout_fragment
                     )*/
                }

                R.id.nav_mi -> {
                    actionBar!!.title = "Meter Indexing"
                    actionBar!!.subtitle = ""
                  /*  fragment = SearchCustomerMIFragment(dashboardBinding)
                    navigationView.menu.findItem(R.id.navigation_mi).isChecked = true
                    Utility.replaceFragment(
                        fragment,
                        supportFragmentManager,
                        R.id.layout_fragment
                    )*/
                }


                R.id.nav_profile -> {
                    actionBar!!.title = "My Profile"
                    actionBar!!.subtitle = ""
                   /* fragment = MyProfileFragment()
                    navigationView.menu.findItem(R.id.navigation_profile).isChecked = true
                    Utility.replaceFragment(
                        fragment,
                        supportFragmentManager,
                        R.id.layout_fragment
                    )*/
                }
            }
            true
        }


        val fragment = DashboardFragment(dashboardBinding)
        Utility.replaceFragment(fragment, supportFragmentManager, R.id.layout_fragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = item.isChecked
        dashboardBinding.drawerLayout.closeDrawer(GravityCompat.START)
        var fragment: Fragment? = null
        when (item.itemId) {

            R.id.navigation_dashboard -> {
                actionBar!!.title = "Dashboard"
                actionBar!!.subtitle = ""
                fragment = DashboardFragment(dashboardBinding)
                navigationView.menu.findItem(R.id.navigation_dashboard).isChecked = true
                Utility.replaceFragment(
                    fragment,
                    supportFragmentManager,
                    R.id.layout_fragment
                )
            }

            R.id.navigation_institude -> {

                Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT)
                    .show()
                /* fragment = SearchCustomerCIFragment(dashboardBinding)
                 navigationView.menu.findItem(R.id.navigation_ci).isChecked = true
                 Utility.replaceFragment(
                     fragment,
                     supportFragmentManager,
                     R.id.layout_fragment
                 )*/
            }

            R.id.navigation_hotel -> {
                actionBar!!.title = "Meter Indexing"
                actionBar!!.subtitle = ""
                /*fragment = SearchCustomerMIFragment(dashboardBinding)
                navigationView.menu.findItem(R.id.navigation_mi).isChecked = true
                Utility.replaceFragment(
                    fragment,
                    supportFragmentManager,
                    R.id.layout_fragment
                )*/
            }

            R.id.navigation_profile -> {
                actionBar!!.title = "My Profile"
                actionBar!!.subtitle = ""
              /*  fragment = MyProfileFragment()
                navigationView.menu.findItem(R.id.navigation_profile).isChecked = true
                Utility.replaceFragment(
                    fragment,
                    supportFragmentManager,
                    R.id.layout_fragment
                )*/
            }

            R.id.navigation_logout -> {
                if (!isFinishing) {
                    Utility.logout(mActivity!!, getString(R.string.logout_from_app))
                }
            }
        }
        return true
    }

    companion object {
        @JvmField
        var bottom_Navigation: BottomNavigationView? = null
    }


    override fun onBackPressed() {
        try {
            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                val fragments = supportFragmentManager.backStackEntryCount
                val size: Int = supportFragmentManager.fragments.size
                val fragment: Fragment?
                var otherfragment: Fragment? = null
                if (supportFragmentManager.fragments[size - 1] is SupportRequestManagerFragment) {
                    if (size > 1) {
                        otherfragment = supportFragmentManager.fragments[size - 2]
                    }
                } else {
                    otherfragment = supportFragmentManager.fragments[size - 1]
                }
                fragment = otherfragment
                if (fragment is DashboardFragment) {
                    finishAffinity()
                }   else {
                    finishAffinity()
                }
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return try {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val v = currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        v.clearFocus()
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm?.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
            super.dispatchTouchEvent(event)
        } catch (e: java.lang.Exception) {
            false
        }
    }


}