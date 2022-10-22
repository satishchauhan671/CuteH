package com.digipanther.cuteh.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.digipanther.cuteh.R
import com.digipanther.cuteh.dbHelper.UserDataHelper
import com.digipanther.cuteh.model.UserInfoModel

class SplashActivity : MyActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.statusBarColor = ContextCompat.getColor(this@SplashActivity, R.color.black)
        }
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            accessPage()

        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    private fun accessPage() {
        val userInfoModel = UserDataHelper.getLogin(this@SplashActivity)
       if (userInfoModel != null) {
            intent = Intent(this@SplashActivity, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}