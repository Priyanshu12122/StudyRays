package com.xerox.studyrays.cacheDb.pwCache.lessonDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PwLessonEntity(
    @PrimaryKey
    val id: String,
    val subjectId: String,
    val name: String,
    val notes: String,
    val slug: String,
    val videos: String,
    val exercises: String
)