package com.xerox.studyrays.db.downloadsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DownloadNumberEntity(
    @PrimaryKey
    val id: Int,
    val numberOfDownloads: Int
)
