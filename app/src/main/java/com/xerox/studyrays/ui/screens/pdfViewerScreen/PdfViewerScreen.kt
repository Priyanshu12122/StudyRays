package com.xerox.studyrays.ui.screens.pdfViewerScreen

import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    url: String,
    name: String,
    onBackClicked: () -> Unit,
) {


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val baseUrl = "https://docs.google.com/gview?embedded=true&url="
    val mainUrl = baseUrl + url


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = { Text(text = name) }, navigationIcon = {
                IconButton(onClick = { onBackClicked() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            }, scrollBehavior = scrollBehavior)
        }
    ) {


        // State to track whether the web page is loading
        var isLoading by rememberSaveable {
            mutableStateOf(true)
        }

        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            // WebView component
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        // Configure WebView settings
                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true

                        // Set up WebViewClient to override url loading
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                // Page loading finished
                                isLoading = false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?,
                            ) {
                                super.onReceivedError(view, request, error)
                            }

                        }


                        // Set up WebChromeClient to track loading progress
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                // Progress changed
                                isLoading = newProgress != 100
                            }
                        }

                        // Load the URL
                        loadUrl(mainUrl)
                    }
                }
            )

            // Display CircularProgressIndicator while loading
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )
            }
        }


    }

}