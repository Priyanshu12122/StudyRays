package com.xerox.studyrays.ui.studyfocus.subjectScreen

import androidx.compose.ui.graphics.Color
import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity
import com.xerox.studyrays.db.studyFocusDb.TaskEntityy

data class SubjectState(
    val currentSubjectId: Int? = null,
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = SubjectsEntity.subjectCardColors.random(),
    val studiedHours: Float = 0f,
    val progress: Float = 0f,
    val recentSessions: List<SessionEntity> = emptyList(),
    val upcomingTasks: List<TaskEntityy> = emptyList(),
    val completedTasks: List<TaskEntityy> = emptyList(),
    val session: SessionEntity? = null,
)
