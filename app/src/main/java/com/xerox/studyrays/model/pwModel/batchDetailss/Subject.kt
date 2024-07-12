package com.xerox.studyrays.model.pwModel.batchDetailss

data class Subject(
    val schedules: List<Schedule>,
    val subjectId: String,
    val subjectImage: String,
    val subjectName: String,
    val subjectSlug: String,
    val tagCount: Int,
    val teachers: List<Teacher>
)