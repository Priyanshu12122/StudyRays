package com.xerox.studyrays.cacheDb.pwCache.notesDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PwNotesEntity(
    @PrimaryKey
    val slug: String,
    val topic: String?,
    val baseUrl: String?,
    val attachmentKey: String?
)