package com.digipanther.cuteh.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.digipanther.cuteh.R
import com.digipanther.cuteh.common.Utility
import com.digipanther.cuteh.fragment.LoginFragment

class LoginActivity : AppCompatActivity() {
    var loginFragment: LoginFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginFragment = LoginFragment()
        Utility.addFragment(LoginFragment(), supportFragmentManager, R.id.layout_fragment)
    }
}