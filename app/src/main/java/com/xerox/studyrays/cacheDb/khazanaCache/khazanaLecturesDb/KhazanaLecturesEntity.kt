package com.xerox.studyrays.cacheDb.khazanaCache.khazanaLecturesDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaLecturesEntity(
    val subjectId: String,
    @PrimaryKey
    val topicName: String,
    val chapterId: String,
    val topicId: String,
    val response: String
)
