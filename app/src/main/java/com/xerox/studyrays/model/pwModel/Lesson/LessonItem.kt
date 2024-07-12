package com.xerox.studyrays.model.pwModel.Lesson

data class LessonItem(
    val batch_id: String,
    val batch_name: String,
    val batch_slug: String,
    val displayOrder: String,
    val exercises: String,
    val id: String,
    val name: String,
    val notes: String,
    val slug: String,
    val subject_slug: String,
    val topic_id: String,
    val type: String,
    val typeId: String,
    val videos: String
)