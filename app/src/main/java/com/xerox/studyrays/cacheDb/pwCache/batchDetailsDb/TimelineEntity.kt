package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimelineEntity(
    @PrimaryKey
    val externalId: String,
    val response: String,
)
