package com.xerox.studyrays.cacheDb.akCache.videosDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AkVideoEntity(
    val bid: Int,
    val sid: Int,
    @PrimaryKey
    val tid: Int,
    val response: String
)
