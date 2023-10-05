package com.philbrick.ui.screen.web

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.philbrick.R
import com.philbrick.databinding.ActivityWebViewBinding
import com.philbrick.ui.customview.BaseActivity
import com.philbrick.util.constant.Extra
import com.philbrick.util.extension.setFullScreen
import com.philbrick.util.extension.setWindowBackground
import com.philbrick.util.helper.Helper
import java.io.File
import java.io.IOException
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

class WebViewActivity : BaseActivity() {


    private lateinit var webBinding : ActivityWebViewBinding
    private var link : String = ""
    var loadingFailed: Boolean = false

    private var mCM: String? = null
    private var mUMA: ValueCallback<Array<Uri>>? = null
    private val FCR = 1
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowBackground()
        setFullScreen()
        webBinding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(webBinding.root)
        init()
        addListener()
    }

    private fun init()
    {
        if (intent!=null){
            if (intent.hasExtra(Extra.TITLE)){
                title = intent.getStringExtra(Extra.TITLE).toString()
            }
            if (intent.hasExtra(Extra.LINK)){
                link = intent.getStringExtra(Extra.LINK).toString()
            }
        }
        webBinding.tbWeb.tvTitle.text = title
        if (title != "Privacy Policy") {
            refresh()
            webBinding.scroll.visibility = View.GONE;

        } else {
            webBinding.wbLink.visibility = View.GONE;

        }
    }


    private fun addListener(){
        webBinding.tbWeb.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun refresh(){
        //show progress
        showDialog()
        if (Helper.isNetworkAvailable(this)){
            loadWebView()
        }
        else{
            showAlert(resources.getString(R.string.network_error_msg))
            dismissDialog()
        }
    }


    private fun loadWebView() {
        webBinding.wbLink.apply {
            //set visibility
//            visibility = if (link.isNotEmpty()) View.GONE else View.GONE
            //init web view
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            settings.loadWithOverviewMode = true
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            isScrollbarFadingEnabled = false

            //set action in web view
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {

                    webBinding.wbLink?.loadUrl(url)

                    when {

                        url.startsWith("tel:") -> {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                                startActivity(intent)
                            } catch (e: Exception) {
                            }
                        }
                        url.startsWith("mailto:") -> {
                            try {
                                val emailIntent = Intent(Intent.ACTION_SENDTO)
                                emailIntent.data = Uri.parse("mailto:")
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(URLDecoder.decode(url.substring(7))))
                                startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.email)))
                            } catch (e: Exception) {
                            }
                        }
                        url.contains("youtube") && url.contains("v=") -> {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        "vnd.youtube://${
                                            url.substringAfter(
                                                "v="
                                            )
                                        }"
                                    )
                                )
                                startActivity(intent)
                                finish()
                            } catch (e: Exception) {

                            }
                        }
                    }
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    try {

                        if(view?.contentHeight == 0){
                            view?.reload()
                            return
                        }

                        dismissDialog()
                        Log.d("Error", "" + loadingFailed)
                        if (loadingFailed){
                            val summary = "<html><body><center><h1>Could not connect to the server.</h1></center></body></html>"
                            webBinding.wbLink.loadData(summary, "text/html", null)
                        }
                    } catch (e:Exception){
                        Log.d("Error",e.toString())
                    }
                    super.onPageFinished(view, url)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    loadingFailed = false
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    dismissDialog()
                    loadingFailed = true
                }
            }

            //set web chrome client
            webChromeClient = object : WebChromeClient() {
                override fun onShowFileChooser(
                    webView: WebView,
                    filePathCallback: ValueCallback<Array<Uri>>,
                    fileChooserParams: FileChooserParams
                ): Boolean {
                    mUMA?.onReceiveValue(null)
                    mUMA = filePathCallback
                    var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent?.resolveActivity(context.packageManager) != null) {
                        var photoFile: File? = null
                        try {
                            photoFile = createImageFile()
                            takePictureIntent.putExtra("PhotoPath", mCM)
                        } catch (ex: IOException) {

                        }
                        if (photoFile != null) {
                            mCM = "file:" + photoFile.absolutePath
                            takePictureIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile)
                            )
                        } else {
                            takePictureIntent = null
                        }
                    }
                    val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                    contentSelectionIntent.type = "*/*"
                    val intentArray: Array<Intent> = takePictureIntent?.let { arrayOf(it) } ?: arrayOf()
                    val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                    startActivityForResult(chooserIntent, FCR)
                    return true
                }
            }

            //webView?.loadDataWithBaseURL(url, null, null, null, null)
            loadUrl(link)
        }

    }

    // Create an image file
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("dd-MM-yyy_HH:mm:ss", Locale.ENGLISH).format(Date())
        val imageFileName = "img_" + timeStamp + "_"
        val storageDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun showAlert(message : String,isSuccess: Boolean = false) = this.let {
        Helper.customSnackBar(
            webBinding.clRoot,
            message,
            applicationContext,
            isSuccess
        )
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}