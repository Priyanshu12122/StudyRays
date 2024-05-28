package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaDppEntity(
    val subjectId: String,
    @PrimaryKey
    val topicName: String,
    val chapterId: String,
    val topicId: String,
    val response: String
)
