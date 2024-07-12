package com.xerox.studyrays.db.videoProgress

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoProgressEntity(
    @PrimaryKey
    val videoId: String,
    val progress: Float,
    val position: Long,

)
