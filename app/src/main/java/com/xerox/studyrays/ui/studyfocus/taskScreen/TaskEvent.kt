package com.xerox.studyrays.ui.studyfocus.taskScreen

import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity
import com.xerox.studyrays.utils.Priority

sealed class TaskEvent {
    data class OnTitleChange(val title: String) : TaskEvent()
    data class OnDescriptionChange(val description: String) : TaskEvent()
    data class OnDateChange(val millis: Long?) : TaskEvent()
    data class OnPriorityChange(val priority: Priority) : TaskEvent()
    data class OnRelatedSubjectSelect(val subject: SubjectsEntity) : TaskEvent()
    data object OnIsCompleteChange : TaskEvent()
    data object SaveTask : TaskEvent()
    data object DeleteTask : TaskEvent()

}
