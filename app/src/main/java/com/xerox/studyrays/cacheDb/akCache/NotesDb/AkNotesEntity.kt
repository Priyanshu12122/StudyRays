package com.xerox.studyrays.cacheDb.akCache.NotesDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AkNotesEntity(
    val bid: Int,
    val sid: Int,
    @PrimaryKey
    val tid: Int,
    val response: String
)
