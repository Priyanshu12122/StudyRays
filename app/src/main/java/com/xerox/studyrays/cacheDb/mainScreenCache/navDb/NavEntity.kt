package com.xerox.studyrays.cacheDb.mainScreenCache.navDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NavEntity(
    @PrimaryKey
    val id: Int,
    val description: String,
    val dismissable: String,
    val email: String,
    val image: String,
    val redirect: String,
    val share: String,
    val status: String,
    val telegram: String,
    val website: String,
)
