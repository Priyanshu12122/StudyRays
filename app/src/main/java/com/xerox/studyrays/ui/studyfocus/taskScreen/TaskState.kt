package com.xerox.studyrays.ui.studyfocus.taskScreen

import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity
import com.xerox.studyrays.utils.Priority

data class TaskState(
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val isTaskComplete: Boolean = false,
    val priority: Priority = Priority.LOW,
    val relatedToSubject: String? = null,
    val subjects: List<SubjectsEntity> = emptyList(),
    val subjectId: Int? = null,
    val currentTaskId: Int? = null
)
