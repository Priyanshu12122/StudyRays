package com.xerox.studyrays.downloadManager

interface Downloader {
    fun downLoadFile(url: String, name: String): Long
}