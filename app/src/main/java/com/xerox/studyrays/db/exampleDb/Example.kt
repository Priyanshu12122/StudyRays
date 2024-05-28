package com.xerox.studyrays.db.exampleDb

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Example(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val msg: String,
    val time: String
)
