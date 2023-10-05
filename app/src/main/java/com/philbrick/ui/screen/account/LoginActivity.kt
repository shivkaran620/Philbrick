package com.philbrick.ui.screen.account

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.philbrick.R
import com.philbrick.data.APIResult
import com.philbrick.databinding.ActivityLoginBinding
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.ui.screen.web.WebViewActivity
import com.philbrick.util.constant.Constant
import com.philbrick.util.constant.Extra
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground
import com.philbrick.util.helper.Helper


class LoginActivity : BaseActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    private lateinit var accountViewModel: AccountViewModel
    lateinit var mobileNo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        loginBinding.btnLogin.setOnClickListener {
            if (checkValidation()){
                mobileVerificationApi()
            }
        }
        init()
       setAgreementText()
    }

    private fun init() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        //loginBinding.edtPhoneNumber.setText("8849556784")
    }

    /**
     * separate click event of single textview privacy and policy
     * set spannable style...
     */
    private fun setAgreementText() {
        val privacyPolicy = SpannableString(
            HtmlCompat.fromHtml(
                resources.getString(R.string.privacy_policy),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        val ss = SpannableString(privacyPolicy)


        val clickableSpanPrivacyPolicy = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(
                    this@LoginActivity,
                    WebViewActivity::class.java
                ).apply {
                    putExtra(Extra.LINK, Constant.LINK_PRIVACY_AND_POLICY)
                    putExtra(Extra.TITLE, resources.getString(R.string.title_privacy_policy_))
                }
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.color_privacy_policy)

            }
        }

        ss.setSpan(clickableSpanPrivacyPolicy, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        loginBinding.apply {
            tvPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
            tvPrivacyPolicy.text = ss

        }
    }
    private fun setAgreementText1() {
        val privacyPolicy = SpannableString(
            HtmlCompat.fromHtml(
                resources.getString(R.string.privacy_policy),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        )
        val ss = SpannableString(privacyPolicy)
        val clickableSpanTermsAndCondition = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, WebViewActivity::class.java).apply {
                    putExtra(Extra.LINK, Constant.LINK_TERMS_AND_USE)
                    putExtra(Extra.TITLE, resources.getString(R.string.title_terms_of_use_))
                }
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.color_privacy_policy)
            }
        }

        val clickableSpanPrivacyPolicy = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(
                    this@LoginActivity,
                    WebViewActivity::class.java
                ).apply {
                    putExtra(Extra.LINK, Constant.LINK_PRIVACY_AND_POLICY)
                    putExtra(Extra.TITLE, resources.getString(R.string.title_privacy_policy_))
                }
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(this@LoginActivity, R.color.color_privacy_policy)

            }
        }

        ss.setSpan(clickableSpanPrivacyPolicy, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(clickableSpanTermsAndCondition, 17, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        loginBinding.apply {
            tvPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
            tvPrivacyPolicy.text = ss

        }
    }

    private fun mobileVerificationApi() {
        accountViewModel.mobileVerificationApi(mobileNo) {
            when (it) {
                is APIResult.Success -> {
                    dismissDialog()
                    startActivity(Intent(this,OtpActivity::class.java).putExtra(Extra.PHONE,mobileNo))
                    finish()
                }
                is APIResult.Failure -> {
                    dismissDialog()
                }
                APIResult.InProgress -> {
                    showDialog()
                }
            }
        }
    }

    private fun showSuccessFailedMessage(message: String, isSuccess: Boolean = false) {
        if (message.isNotEmpty()) {
            Helper.customSnackBar(loginBinding.clRoot, message, this, isSuccess)
        }
    }

    private fun checkValidation() : Boolean {
        var isValid: Boolean = true
        mobileNo = loginBinding.edtPhoneNumber.text.toString().trim()
        when {
            mobileNo.isEmpty() -> {
                loginBinding.edtPhoneNumber.error = resources.getString(R.string.err_mobile_no_required)
                isValid = false
            }
        }
        return isValid
    }
}