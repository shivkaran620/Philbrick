package com.philbrick.ui.screen.product

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.request.ImageRequest
import com.philbrick.R
import com.philbrick.data.APIResult
import com.philbrick.databinding.ActivityProductDetailsBinding
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.util.constant.Extra
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground
import com.philbrick.util.helper.Helper

class ProductDetailsActivity : BaseActivity() {
    lateinit var productBinding: ActivityProductDetailsBinding
    private var productId: String = ""
    lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        setContentView(R.layout.activity_product_details)
        productBinding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(productBinding.root)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        init()
    }

    private fun init() {
        intent.let {
            if (intent != null) {
                if (intent.hasExtra(Extra.PRODUCT_ID)) {
                    productId = intent.getStringExtra(Extra.PRODUCT_ID) ?: ""
                }
            }
        }
        productBinding.apply {
            tbProfile.tvTitle.text = resources.getString(R.string.product_details)
            tbProfile.imgBack.setOnClickListener {
                onBackPressed()
            }
        }
        getProductDetail()
    }

    private fun setPreData() {


        /*      when (productViewModel.product.productCategory) {
                  CategoryType.CAR_DOOR.name -> {
                      productBinding.apply {
                          Log.e("@@@one", "One")
                          clCategory1.visibility = View.GONE
                          clCategory2.visibility = View.VISIBLE
                          clCategory3.visibility = View.GONE
                          clCategory4.visibility = View.GONE

                      }
                  }
                  CategoryType.LIFE_MASTER.id -> {
                      productBinding.apply {
                          Log.e("@@@two", "One")
                          clCategory1.visibility = View.GONE
                          clCategory2.visibility = View.GONE
                          clCategory3.visibility = View.GONE
                          clCategory4.visibility = View.VISIBLE

                      }
                  }
                  CategoryType.LANDING_DOOR.id -> {
                      productBinding.apply {
                          Log.e("@@@three", "One")
                          clCategory1.visibility = View.GONE
                          clCategory2.visibility = View.GONE
                          clCategory3.visibility = View.VISIBLE
                          clCategory4.visibility = View.GONE

                      }
                  }
                  CategoryType.CONTROL_PANEL.id -> {
                      productBinding.apply {
                          Log.e("@@@Four", "One")
                          clCategory1.visibility = View.VISIBLE
                          clCategory2.visibility = View.GONE
                          clCategory3.visibility = View.GONE
                          clCategory4.visibility = View.GONE

                      }
                  }
                  else -> {
                      Log.e("@@@Five", "One")
                      productBinding.apply {
                          clCategory1.visibility = View.GONE
                          clCategory2.visibility = View.GONE
                          clCategory3.visibility = View.GONE
                          clCategory4.visibility = View.VISIBLE

                      }
                  }
              }*/

        if (productViewModel.product.cat_name.equals("Ultima")) {

            productBinding.clCategory1.visibility = View.VISIBLE
            productBinding.clCategory2.visibility = View.GONE
            productBinding.clCategory3.visibility = View.GONE
            productBinding.clCategory4.visibility = View.GONE
            if (productViewModel.product.proname_name?.equals("LANDING HEADER") == true) {
                productBinding.incOne.txtSkateCode.visibility = View.GONE
                productBinding.incOne.txtSkateDetail.visibility = View.GONE
                productBinding.incOne.txtCarheaderfittingclampCode.visibility = View.GONE
                productBinding.incOne.txtCarheaderfittingclampDetail.visibility = View.GONE
                productBinding.incOne.txtMotordriveandpowersupplyCode.visibility = View.GONE
                productBinding.incOne.txtMotordriveandpowersupplyDetail.visibility = View.GONE
            }
        } else if (productViewModel.product.cat_name.equals("Lift Master")) {
            productBinding.clCategory1.visibility = View.GONE
            productBinding.clCategory2.visibility = View.GONE
            productBinding.clCategory3.visibility = View.GONE
            productBinding.clCategory4.visibility = View.VISIBLE

        } else if (productViewModel.product.cat_name.equals("Control Panel")) {
            productBinding.clCategory1.visibility = View.GONE
            productBinding.clCategory2.visibility = View.VISIBLE
            productBinding.clCategory3.visibility = View.GONE
            productBinding.clCategory4.visibility = View.GONE
        } else {
            productBinding.clCategory1.visibility = View.GONE
            productBinding.clCategory2.visibility = View.GONE
            productBinding.clCategory3.visibility = View.VISIBLE
            productBinding.clCategory4.visibility = View.GONE
            if (productViewModel.product.proname_name?.equals("LANDING HEADER") == true) {
                productBinding.incThree.txtSkateCode.visibility = View.GONE
                productBinding.incThree.txtSkateDetail.visibility = View.GONE
                productBinding.incThree.txtCarheaderfittingclampCode.visibility = View.GONE
                productBinding.incThree.txtCarheaderfittingclampDetail.visibility = View.GONE
                productBinding.incThree.txtMotordriveandpowersupplyCode.visibility = View.GONE
                productBinding.incThree.txtMotordriveandpowersupplyDetail.visibility = View.GONE

            }
        }

        productBinding.apply {
            incOne.apply {
                Log.e("@@@one", "One")
                txtCategoryDetail.text = productViewModel.product.cat_name
                txtSubCategoryDetail.text = productViewModel.product.subcat_name
                txtCategoryDetail.visibility = View.GONE
                txtSubCategoryDetail.visibility = View.GONE
                txtProductNameDetail.text = productViewModel.product.proname_name
                txtPartNameDetail.text = productViewModel.product.partname_name
                txtPartCodeDetail.text = productViewModel.product.partcode_name
                txtSerialNumberDetail.text = productViewModel.product.product_serialno
                txtDomrgDetail.text = productViewModel.product.productDateMfg
                txtHeaderassemblyDetail.text = productViewModel.product.productHasseMBLY
                txtSillDetail.text = productViewModel.product.productSill
                txtSkateDetail.text = productViewModel.product.productSkate
                txtsillRollerassemblysetDetail.text = productViewModel.product.productSillSet
                txtCarheaderfittingclampDetail.text =
                    productViewModel.product.productCarHedFClamp
                txtMotordriveandpowersupplyDetail.text = productViewModel.product.productMdpSupply
                txtHardwarekitsetDetail.text = productViewModel.product.productHardKit
                txtdrawingmanualDetail.text = productViewModel.product.productDrwMnl
            }
            incTwo.apply {
                Log.e("@@@Two", "One")
                txtCategoryDetail.text = productViewModel.product.cat_name
                tvSubCategoryDetails.text = productViewModel.product.subcat_name
                tvProductNameValue.text = productViewModel.product.proname_name
                txtCategoryDetail.visibility = View.GONE
                tvSubCategoryDetails.visibility = View.GONE
                tvModelValue.text = productViewModel.product.product_model
                tvHPValue.text = productViewModel.product.product_hp
                tvFloorValue.text = productViewModel.product.productFloor
                tvSupplyValue.text = productViewModel.product.product_supply
                tvSrialNumberValue.text = productViewModel.product.product_serialno
                tvdateofmfgValue.text = productViewModel.product.productDateMfg
                tvCustomerNameValue.text = productViewModel.product.productClient
                tvDriveNoValue.text = productViewModel.product.productDriveNo

            }
            incThree.apply {
                txtCategoryDetail.text = productViewModel.product.cat_name
                txtSubCategoryDetail.text = productViewModel.product.subcat_name
                txtCategoryDetail.visibility = View.GONE
                txtSubCategoryDetail.visibility = View.GONE
                txtProductNameDetail.text = productViewModel.product.proname_name
                txtPartNameDetail.text = productViewModel.product.partname_name
                txtPartCodeDetail.text = productViewModel.product.partcode_name
                txtSerialNumberDetail.text = productViewModel.product.product_serialno
                txtDomrgDetail.text = productViewModel.product.productDateMfg
                txtHeaderassemblyDetail.text = productViewModel.product.productHasseMBLY
                txtSillDetail.text = productViewModel.product.productSill
                txtSkateDetail.text = productViewModel.product.productSkate
                txtsillRollerassemblysetDetail.text = productViewModel.product.productSillSet
                txtCarheaderfittingclampDetail.text =
                    productViewModel.product.productCarHedFClamp
                txtMotordriveandpowersupplyDetail.text = productViewModel.product.productMdpSupply
                txtHardwarekitsetDetail.text = productViewModel.product.productHardKit
                txtdrawingmanualDetail.text = productViewModel.product.productDrwMnl

            }
            incFour.apply {
                Log.e("@@@Four", "One")
                txtCategoryDetail.text = productViewModel.product.cat_name
                txtSubCategoryDetail.text = productViewModel.product.subcat_name
                txtCategoryDetail.visibility = View.GONE
                txtSubCategoryDetail.visibility = View.GONE
                txtProductNameDetail.text = productViewModel.product.proname_name
                txtPartNameDetail.text = productViewModel.product.partname_name
                txtVersionDetail.text = productViewModel.product.product_version
                txtSerialNumberDetail.text = productViewModel.product.product_serialno
                txtDomrgDetail.text = productViewModel.product.productDateMfg
                txtMoterDetail.text = productViewModel.product.productMotor
                txtDriveDetail.text = productViewModel.product.productDrive
                txtPowerSupplyDetail.text = productViewModel.product.product_powersupply
                txtHardwarekitsetDetail.text = productViewModel.product.productHardKit
            }
            imgLogo.visibility = View.VISIBLE
            imgLogo.load(productViewModel.product.catLogo) {
                listener(
                    onSuccess = { imageRequest: ImageRequest, result ->
                        //   imgLogo.visibility = View.VISIBLE
                        pb.visibility = View.GONE
                    },
                    onError = { imageRequest: ImageRequest, error ->
                        placeholder(R.drawable.ic_user_icon)
                        //imgLogo.visibility = View.VISIBLE
                        pb.visibility = View.GONE
                    },
                    onStart = {
                        // imgLogo.visibility = View.VISIBLE
                    })
            }
        }
    }

    private fun getProductDetail() {
        productViewModel.getProductDetails(productId) {
            when (it) {
                is APIResult.Success -> {
                    setPreData()
                    dismissDialog()
                }
                is APIResult.Failure -> {
                    showError(it.message ?: "")
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
            Helper.customSnackBar(productBinding.clParent, message, this, isSuccess)
        }
    }
}