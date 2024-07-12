package com.xerox.studyrays.ui.studyfocus.sessionScreen

import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity

sealed class SessionEvent {
    data class OnRelatedSubjectChange(val subject: SubjectsEntity) : SessionEvent()
    data class SaveSession(val duration: Long) : SessionEvent()
    data class OnDeleteSessionButtonClick(val session: SessionEntity) : SessionEvent()
    data object DeleteSession : SessionEvent()
    data object NotifyToUpdateSubject : SessionEvent()
    data class UpdateSubjectIdAndRelatedSubject(
        val subjectId: Int?,
        val relatedToSubject: String?
    ) : SessionEvent()
}
