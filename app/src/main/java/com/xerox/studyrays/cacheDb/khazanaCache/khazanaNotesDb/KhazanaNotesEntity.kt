package com.xerox.studyrays.cacheDb.khazanaCache.khazanaNotesDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaNotesEntity(
    val subjectId: String,
    @PrimaryKey
    val topicName: String,
    val chapterId: String,
    val topicId: String,
    val response: String
)

