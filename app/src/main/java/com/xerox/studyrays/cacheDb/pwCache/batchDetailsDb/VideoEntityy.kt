package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoEntityy(
    val response: String,
    val batchId: String,
    val subjectSlug: String,
    @PrimaryKey
    val topicSlug: String
)