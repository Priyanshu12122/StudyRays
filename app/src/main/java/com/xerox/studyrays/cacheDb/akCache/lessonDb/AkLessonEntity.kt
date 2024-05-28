package com.xerox.studyrays.cacheDb.akCache.lessonDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AkLessonEntity(
    val bid: Int,
    @PrimaryKey
    val sid: Int,
    val response: String
)
