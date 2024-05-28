package com.xerox.studyrays.db.khazanaFavDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KhazanaFav(
    @PrimaryKey(autoGenerate = false)
    val externalId: String,
    val imageUrl: String,
    val subjectId: String,
    val chapterId: String,
    val courseName: String,
    val totalLectures: String,
)
