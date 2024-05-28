package com.xerox.studyrays.cacheDb.akCache.indexDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IndexEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val response: String
)