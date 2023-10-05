package com.philbrick.ui.screen.product

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.philbrick.data.APIResult
import com.philbrick.databinding.FragmentHomeBinding
import com.philbrick.ui.adapter.VPAdapter
import com.philbrick.ui.customview.BaseFragment
import com.philbrick.util.constant.Code
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground
import com.philbrick.util.helper.Helper
import java.util.*


class HomeFragment : BaseFragment(),View.OnClickListener {

    private var parentActivity: SideMenuActivity? = null
    private lateinit var homeFragmentBinding: FragmentHomeBinding
    private lateinit var vpAdapter: VPAdapter

    private var currentPage: Int = 0
    var timer: Timer? = null
    private val DELAY_MS: Long = 500
    private val PERIOD_MS: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        parentActivity = if (activity is SideMenuActivity) activity as SideMenuActivity else null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeFragmentBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return homeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        addClickListener()
    }

    private fun init(){
        homeFragmentBinding.vpBanner.apply {
            vpAdapter = VPAdapter(arrayListOf(),requireContext())
            adapter = vpAdapter
            homeFragmentBinding.bannerDots.setViewPager(this)
        }
        homeFragmentBinding.clScanner.setOnClickListener {
            startActivity(Intent(requireContext(),ScannerActivity::class.java))
        }

        baseHandler.postDelayed(Runnable {
            if (parentActivity?.productViewModel?.bannerList?.isEmpty() == true){
                getProductBannerList()
            }else{
                bannerImageList()
                vpAdapter.update(parentActivity?.productViewModel?.bannerList?: arrayListOf())
            }
        },500)
    }

    private fun bannerImageList() {
        val update = Runnable {
            if (currentPage == parentActivity?.productViewModel?.bannerList?.size) {
                currentPage = 0
            }
            homeFragmentBinding.vpBanner.setCurrentItem(currentPage++, true)
        }


        timer?.cancel()
        timer = Timer() // This will create a new Thread

        timer?.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                baseHandler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    private fun addClickListener(){
        with(homeFragmentBinding){
            btnScanner.setOnClickListener(this@HomeFragment)
        }
    }

    override fun onClick(view: View?) {
       with(homeFragmentBinding){
           when(view){
                btnScanner -> {
                    if (checkPermission()){
                        startActivity(Intent(requireContext(),ScannerActivity::class.java))
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(arrayOf(Manifest.permission.CAMERA), Code.RequestCode.REQ_CAMERA)
                        }
                    }
                }
           }
       }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        when (requestCode) {
            Code.RequestCode.REQ_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(requireContext(),ScannerActivity::class.java))
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getProductBannerList(){
       parentActivity?.productViewModel?.getProductBannerList {
            when (it) {
                is APIResult.Success -> {
                    bannerImageList()
                    vpAdapter.update(parentActivity?.productViewModel?.bannerList?: arrayListOf())
                    dismissDialog()
                }
                is APIResult.Failure -> {
                    showError(it.message?:"")
                    dismissDialog()
                }
                APIResult.InProgress -> {
                    showDialog()
                }
            }
        }
    }

    private fun showError(message: String, isSuccess: Boolean = false) {
        if (message.isNotEmpty()) {
            Helper.customSnackBar(homeFragmentBinding.clParent, message, requireContext(), isSuccess)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}