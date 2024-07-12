package com.xerox.studyrays.db.alarmDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmEntity(
    @PrimaryKey
    val id: Int,
    val timeToTriggerAt: Long,
    val scheduledTime: Long
)
