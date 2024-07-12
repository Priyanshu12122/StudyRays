package com.xerox.studyrays.db.videoNotesDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoNoteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val timeStamp: Long,
    val videoId: String,
    val note: String,
)
