package com.xerox.studyrays.cacheDb.khazanaCache.khazanaTeachersDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaTeachersEntity(
    @PrimaryKey
    val id: String,
    val response: String
)
