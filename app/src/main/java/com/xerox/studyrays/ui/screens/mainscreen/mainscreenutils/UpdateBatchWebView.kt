package com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun UpdateBatchWebView(modifier: Modifier = Modifier) {
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var progress by rememberSaveable { mutableIntStateOf(0) }
    val taskUrl = "https://devjisu.com/update/"


    Box(modifier = modifier.fillMaxSize()){

        if (isLoading){
            Dialog(onDismissRequest = {

            }) {
                CircularProgressIndicator(
                    modifier = modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )
            }
        }
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?,
                        ): Boolean {
                            return super.shouldOverrideUrlLoading(view, request)
                        }
                    }


                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            // Progress changed
                            progress = newProgress
                            isLoading = newProgress != 100
                        }
                    }

                    settings.javaScriptEnabled = true
                    loadUrl(taskUrl)
                }
            },
            update = {
                it.loadUrl(taskUrl)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}