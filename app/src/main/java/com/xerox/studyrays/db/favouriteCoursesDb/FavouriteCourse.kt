package com.xerox.studyrays.db.favouriteCoursesDb

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavouriteCourse(
    @PrimaryKey(autoGenerate = false)
    val externalId: String = "",
)
