package com.xerox.studyrays.cacheDb.mainScreenCache.promoDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Promo Cache Db")
data class PromoEntity(
    val description: String?,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageUrl: String,
    val redirectionLink: String?,
    val title: String,
)
