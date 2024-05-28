package com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val searchText: String
)
