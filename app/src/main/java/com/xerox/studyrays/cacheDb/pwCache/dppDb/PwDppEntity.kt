package com.xerox.studyrays.cacheDb.pwCache.dppDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PwDppEntity(
    @PrimaryKey
    val slug: String,
    val topic: String?,
    val baseUrl: String?,
    val attachmentKey: String?
)
