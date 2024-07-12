package com.xerox.studyrays.model.downloads

data class DownloadItem(
    val task_final_url: String,
    val download_url: String,
    val task_url: String,
    val download_limit: String,
    val visible: String,
    val master_url: String,
    val mpd_url: String,
    val stream_type: String
)