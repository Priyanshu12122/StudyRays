package com.xerox.studyrays.cacheDb.mainScreenCache.priceDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PriceEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val totalAmount: Int,
    val timestamp: Long
)
