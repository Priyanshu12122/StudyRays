package com.xerox.studyrays.db.keyDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KeyEntity(
    @PrimaryKey
    val id: Int,
    val timeStamp: Long,
    val timeToTriggerAt: Long
)
