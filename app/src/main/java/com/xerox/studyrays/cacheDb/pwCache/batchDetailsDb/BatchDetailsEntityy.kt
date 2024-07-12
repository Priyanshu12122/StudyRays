package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BatchDetailsEntityy(
    @PrimaryKey
    val batchId: String,
    val classValue: String,
    val slug: String,
    val response: String,
)
