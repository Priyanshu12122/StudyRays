package com.xerox.studyrays.db.favouriteCoursesDb

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavouriteCourse(
    @PrimaryKey(autoGenerate = false)
    val externalId: String = "",
    val name: String,
    val language: String,
    val imageUrl: String,
    val byName: String,
    val slug: String,
    val classValue: String,
    val isOld: Boolean,
)
