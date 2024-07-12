package com.xerox.studyrays.ui.studyfocus.sessionScreen

import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity

data class SessionState(
    val subjects: List<SubjectsEntity> = emptyList(),
    val sessions: List<SessionEntity> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: SessionEntity? = null
)
