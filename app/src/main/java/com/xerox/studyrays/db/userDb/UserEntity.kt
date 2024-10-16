package com.xerox.studyrays.db.userDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    val userId: String,
    @PrimaryKey
    val userNumber: Int,
    val name: String,
    val isBanned: Int,
    val exam: String,
    val gender: String,
    val date: String?
)
