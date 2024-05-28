package com.xerox.studyrays.cacheDb.khazanaCache.khazanaChaptersDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaChaptersEntity(
    val subjectId: String,
    @PrimaryKey
    val chapterId: String,
    val response: String
)
