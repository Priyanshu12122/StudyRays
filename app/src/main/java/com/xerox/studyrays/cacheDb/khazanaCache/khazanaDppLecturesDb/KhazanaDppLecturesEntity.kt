package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppLecturesDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaDppLecturesEntity(
    val subjectId: String,
    @PrimaryKey
    val topicName: String,
    val chapterId: String,
    val topicId: String,
    val response: String
)
