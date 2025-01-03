package com.xerox.studyrays.downloadManager

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AndroidDownloader(
    context: Context,
): Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downLoadFile(url: String, name: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("$name.pdf")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$name.pdf")

        return downloadManager.enqueue(request)
    }



}

