package com.xerox.studyrays.ui.screens.keyGeneratorScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.db.keyDb.KeyEntity
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.ui.theme.MainPurple

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = hiltViewModel(),
    onCompleted: () -> Unit,
    taskUrl: String,
    shortenedUrl: String,
    onBackClick: () -> Unit,
) {

//    val taskkUrl = "https://sharedisklinks.com/nQJrquR"
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var progress by rememberSaveable { mutableIntStateOf(0) }
    var backEnabled by rememberSaveable { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    Scaffold(

        topBar = {

            if (isLoading) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = modifier.fillMaxWidth(),
                    color = MainPurple
                )
            }
        }

    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = object : WebViewClient() {


                            override fun onPageStarted(
                                view: WebView,
                                url: String?,
                                favicon: Bitmap?,
                            ) {
                                backEnabled = view.canGoBack()
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                backEnabled = view?.canGoBack() == true
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?,
                            ): Boolean {
                                request?.url?.let { url ->
                                    if (url.toString().contains(shortenedUrl, true)) {
                                        Log.d("TAG", "contains = $url")
                                        vm.insertKey(
                                            KeyEntity(
                                                id = 1,
                                                timeStamp = System.currentTimeMillis(),
                                                timeToTriggerAt = (23 * 60 * 60 * 1000) + (System.currentTimeMillis())
                                            )
                                        )
                                        onCompleted()
                                        return true
                                    }
                                }
                                return false
                            }
                        }


                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                progress = newProgress
                                isLoading = newProgress != 100
                            }
                        }

                        settings.javaScriptEnabled = true
                        loadUrl(taskUrl)
                        webView = this
                    }
                },
                update = {
                    it.loadUrl(taskUrl)
                },
                modifier = Modifier.fillMaxSize()
            )

            BackHandler {
                if (backEnabled) {
                    webView?.goBack()
                } else {
                    onBackClick()
                }
            }

        }
    }


}