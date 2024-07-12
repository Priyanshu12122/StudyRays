package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LessonsEntityy(
    val response: String,
    val batchId: String,
    @PrimaryKey
    val slug: String
)
