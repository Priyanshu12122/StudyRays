package com.xerox.studyrays.ui.studyfocus.dashboardScreen

import androidx.compose.ui.graphics.Color
import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.TaskEntityy

sealed class DashboardEvent {
    data object SaveSubject : DashboardEvent()
    data object DeleteSession : DashboardEvent()
    data class OnDeleteSessionButtonClick(val session: SessionEntity): DashboardEvent()
    data class OnTaskIsCompleteChange(val task: TaskEntityy): DashboardEvent()
    data class OnSubjectCardColorChange(val colors: List<Color>): DashboardEvent()
    data class OnSubjectNameChange(val name: String): DashboardEvent()
    data class OnGoalStudyHoursChange(val hours: String): DashboardEvent()
}
