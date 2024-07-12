package com.xerox.studyrays.ui.studyfocus.subjectScreen

import androidx.compose.ui.graphics.Color
import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.TaskEntityy

sealed class SubjectEvent {
    data object UpdateSubject : SubjectEvent()
    data object DeleteSubject : SubjectEvent()
    data object DeleteSession : SubjectEvent()
    data object UpdateProgress : SubjectEvent()
    data class OnTaskIsCompleteChange(val task: TaskEntityy): SubjectEvent()
    data class OnSubjectCardColorChange(val color: List<Color>): SubjectEvent()
    data class OnSubjectNameChange(val name: String): SubjectEvent()
    data class OnGoalStudyHoursChange(val hours: String): SubjectEvent()
    data class OnDeleteSessionButtonClick(val session: SessionEntity): SubjectEvent()
}
