package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val showorder: String,
    val slug: String,
)