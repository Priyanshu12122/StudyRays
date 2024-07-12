package com.xerox.studyrays.db.taskDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey
    val id: Int,
    val ipAddress: String,
    val timeStamp: Long,
)
