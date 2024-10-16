package com.xerox.studyrays.db.watchLaterDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchLaterEntity(
    val imageUrl: String,
    val title: String,
    val dateCreated: String,
    val duration: String,
    @PrimaryKey
    val videoId: String,
    val videoUrl: String,
    val externalId: String,
    val embedCode: String,
    val time: Long,
    val isKhazana: Boolean,
    val isPw: Boolean,
    val isOld: Boolean
    )
