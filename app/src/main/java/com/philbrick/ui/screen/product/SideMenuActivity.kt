package com.philbrick.ui.screen.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.philbrick.R
import com.philbrick.data.APIResult
import com.philbrick.data.local.PreferenceManager
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.databinding.ActivitySideMenuBinding
import com.philbrick.ui.screen.account.LoginActivity
import com.philbrick.ui.screen.account.ProfileActivity
import com.philbrick.ui.screen.web.WebViewActivity
import com.philbrick.util.constant.Constant
import com.philbrick.util.constant.Extra
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground

class SideMenuActivity : BaseActivity(), View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySideMenuBinding
    var navView: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    var navController: NavController? = null
    private var ivMenu: ImageView? = null
    var clLayout: ConstraintLayout? = null
    lateinit var productViewModel : ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        binding = ActivitySideMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
        init()
    }

    private fun setUpNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
     /*   appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home,
            R.id.nav_profile, R.id.nav_about, R.id.nav_contact_us,
            R.id.nav_privacy,
            R.id.nav_terms,R.id.nav_logout), drawerLayout)*/
        navView?.setupWithNavController(navController!!)
        navView?.itemIconTintList = null
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
    }



    private fun init(){
        drawerLayout = findViewById(R.id.navDrawer)
        navView = findViewById(R.id.navView)
        binding.version.text = "V${Constant.EXTERNAL_VERSION}"
        setToolbar()
        addListener()

    }

    private fun setToolbar() {
        clLayout = findViewById(R.id.constraintLayout)
        ivMenu = clLayout?.findViewById(R.id.ivMenu)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun addListener() {
        ivMenu?.setOnClickListener(this)

        navView?.menu?.findItem(R.id.nav_profile)?.setOnMenuItemClickListener {
           startActivity(Intent(this,ProfileActivity::class.java))
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }

        navView?.menu?.findItem(R.id.nav_home)?.setOnMenuItemClickListener {
           navController?.navigate(R.id.nav_home)
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }

        navView?.menu?.findItem(R.id.nav_privacy)?.setOnMenuItemClickListener {
            val intent = Intent(
                this@SideMenuActivity,
                WebViewActivity::class.java
            ).apply {
                putExtra(Extra.LINK, Constant.LINK_PRIVACY_AND_POLICY)
                putExtra(Extra.TITLE, resources.getString(R.string.title_privacy_policy_))
            }
            startActivity(intent)
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }

        navView?.menu?.findItem(R.id.nav_terms)?.setOnMenuItemClickListener {
            val intent = Intent(this@SideMenuActivity, WebViewActivity::class.java).apply {
                putExtra(Extra.LINK, Constant.LINK_TERMS_AND_USE)
                putExtra(Extra.TITLE, resources.getString(R.string.title_terms_of_use_))
            }
            startActivity(intent)
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }
        navView?.menu?.findItem(R.id.nav_delete)?.setOnMenuItemClickListener {
            showDeleteAccountDialog()
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }

        navView?.menu?.findItem(R.id.nav_logout)?.setOnMenuItemClickListener {
            showLogoutDialog()
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }

        navView?.menu?.findItem(R.id.nav_contact_us)?.setOnMenuItemClickListener {
            val intent = Intent(this@SideMenuActivity, WebViewActivity::class.java).apply {
                putExtra(Extra.LINK, Constant.LINK_CONTACT_US)
                putExtra(Extra.TITLE, resources.getString(R.string.menu_contact_us))
            }
            startActivity(intent)
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }

        navView?.menu?.findItem(R.id.nav_about)?.setOnMenuItemClickListener {
            val intent = Intent(this@SideMenuActivity, WebViewActivity::class.java).apply {
                putExtra(Extra.LINK, Constant.LINK_ABOUTS_US)
                putExtra(Extra.TITLE, resources.getString(R.string.menu_about))
            }
            startActivity(intent)
            drawerLayout?.closeDrawer(GravityCompat.START)
            true
        }

    }

    override fun onClick(view: View?) {
        when(view){
            ivMenu -> {
                if (!drawerLayout?.isDrawerOpen(GravityCompat.START)!!)
                    drawerLayout?.openDrawer(GravityCompat.START)
                else
                    drawerLayout?.closeDrawer(GravityCompat.END)
            }
        }
    }

    private fun showLogoutDialog(){
        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.Components_Temper_DialogThemeOverlay
        )

        dialog.setTitle(resources.getString(R.string.logout))
            .setMessage(resources.getString(R.string.msg_logout))
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                dialog.dismiss()
                PreferenceManager(this).logOut()
                val intent = Intent(this@SideMenuActivity, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            .show()
    }

    private fun showDeleteAccountDialog(){
        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.Components_Temper_DialogThemeOverlay
        )

        dialog.setTitle(resources.getString(R.string.deleteaccount))
            .setMessage(resources.getString(R.string.msg_deleteaccount))
            .setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                dialog.dismiss()
                PreferenceManager(this).logOut()
                val intent = Intent(this@SideMenuActivity, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            .show()
    }


}