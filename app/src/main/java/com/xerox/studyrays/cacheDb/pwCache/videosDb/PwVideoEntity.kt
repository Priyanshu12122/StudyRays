package com.xerox.studyrays.cacheDb.pwCache.videosDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PwVideoEntity(
    @PrimaryKey
    val slug: String,
    val response: String
)