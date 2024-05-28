package com.xerox.studyrays.cacheDb.akCache.subjectsDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AkSubjectEntity(
    @PrimaryKey
    val id: Int,
    val response: String
)
