package com.xerox.studyrays.db.studyFocusDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntityy(
    val title: String,
    val description: String,
    val dueDate: Long,
    val priority: Int,
    val relatedToSubject: String,
    val isComplete: Boolean,
    val taskSubjectId: Int,
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null
)
