package com.xerox.studyrays.ui.studyfocus.dashboardScreen

import androidx.compose.ui.graphics.Color
import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity

data class DashboardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHours: Float = 0f,
    val totalGoalStudyHours: Float = 0f,
    val subjects: List<SubjectsEntity> = emptyList(),
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = SubjectsEntity.subjectCardColors.random(),
    val session: SessionEntity? = null
)
