package com.philbrick.ui.other

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.philbrick.data.local.PreferenceManager
import com.philbrick.databinding.ActivitySplashScreenBinding
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.ui.screen.account.AccountViewModel
import com.philbrick.ui.screen.account.LoginActivity
import com.philbrick.ui.screen.account.ProfileActivity
import com.philbrick.ui.screen.product.SideMenuActivity
import com.philbrick.util.constant.Constant
import com.philbrick.util.constant.Extra

class SplashScreen : BaseActivity() {
    private lateinit var activitySplashBinding: ActivitySplashScreenBinding
    private val splashTime = 2000L
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var pref : PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activitySplashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activitySplashBinding.root)
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        pref = PreferenceManager(this)

        runnable = Runnable {
            if (accountViewModel.isUserLoggedIn()){
                val intent = Intent(this@SplashScreen, SideMenuActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
               if (PreferenceManager(this).accessToken.isNotEmpty() && PreferenceManager(this).userId.isNotEmpty()){
//                   val intent = Intent(this@SplashScreen, ProfileActivity::class.java)
//                   intent.putExtra(Extra.IS_FROM_OTP,true)
//                   startActivity(intent)
//                   finish()
                   val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                   startActivity(intent)
                   finish()
               }
               else{
                   val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                   startActivity(intent)
                   finish()
               }
            }

        }
        init()
    }
    private fun init()
    {
        activitySplashBinding.tvVersion.text = "V${Constant.EXTERNAL_VERSION}"
        baseHandler.postDelayed(runnable, splashTime)
    }
    //on activity destroy
    override fun onDestroy() {
        super.onDestroy()
        baseHandler.removeCallbacks(runnable)
    }


}