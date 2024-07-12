package com.xerox.studyrays.cacheDb.pwCache.courseDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PwCourseEntity(
    @PrimaryKey
    val id: String,
    val queryClassValue: String,
    val slug: String,
    val classValue: String,
    val externalId: String,
    val name: String,
    val language: String,
    val imageUrl: String,
    val byName: String,
    val isOld: Boolean

)
