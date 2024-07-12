package com.xerox.studyrays.ui.screens.videoPlayerScreen.videoDownloaderTask

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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.xerox.studyrays.alarmManager.AlarmScheduler
import com.xerox.studyrays.db.alarmDb.AlarmEntity
import com.xerox.studyrays.db.taskDb.TaskEntity
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoDownloaderTaskScreen(
    modifier: Modifier = Modifier,
    vm: VideoViewModel = hiltViewModel(),
    taskUrl: String,
    shortenedUrl: String,
    onBackClick: () -> Unit,
    onCompleted: () -> Unit,
) {
    val contextt = LocalContext.current
    val alarmScheduler = AlarmScheduler(contextt)

    var isLoading by rememberSaveable { mutableStateOf(true) }
    var progress by rememberSaveable { mutableIntStateOf(0) }
    var backEnabled by rememberSaveable { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    Box(modifier = modifier.fillMaxSize()) {

        if (isLoading) {
            Dialog(onDismissRequest = {

            }) {
                CircularProgressIndicator(
//                progress = { (progress/100).toFloat() },
                    modifier = modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )
            }
        }
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object:  WebViewClient(){


                        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                            backEnabled = view.canGoBack()
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            backEnabled = view?.canGoBack() == true
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            request?.url?.let { url ->
                                 if (url.toString().contains(shortenedUrl, true)){
                                    vm.insertTask(TaskEntity(
                                        id = 1,
                                        ipAddress = vm.getIPAddress(true) ?: "No Ip address found",
                                        timeStamp = System.currentTimeMillis()
                                    ))
                                    vm.insertAlarm(AlarmEntity(
                                        id = 1,
                                        timeToTriggerAt = (24 * 60 * 60 * 1000) + (System.currentTimeMillis()),
                                        scheduledTime = System.currentTimeMillis()
                                    ))
                                    try{
                                        alarmScheduler.schedule((24 * 60 * 60 * 1000) + (System.currentTimeMillis()))
                                    } catch (e: Exception){
                                        Log.d("TAG", "shouldOverrideUrlLoading: ${e.localizedMessage}")
                                    }
                                    onCompleted()
                                     return true
                                } else {
                                     return false
                                }
                            }
                            return super.shouldOverrideUrlLoading(view, request)
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
            modifier = modifier.fillMaxSize()
        )

        BackHandler {
            if (backEnabled){
                webView?.goBack()
            } else {
                onBackClick()
            }
        }

    }

}