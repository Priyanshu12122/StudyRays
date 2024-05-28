package com.xerox.studyrays.cacheDb.khazanaCache.khazanaSubjectsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaSubjectEntity(
    @PrimaryKey
    val id: String,
    val response: String
)
