package com.xerox.studyrays.db.videoDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
    @PrimaryKey(autoGenerate = false)
    val videoId: String,
)
