package com.xerox.studyrays.model.pwModel

data class DppItem(
    val id: Int,
    val batch_name: String,
    val batch_id: String,
    val batch_slug: String,
    val subject_slug: String,
    val external_id: String,
    val date: String,
    val homework_id: String,
    val homework_topic_name: String,
    val homework_note: String,
    val attachment_id: String,
    val attachment_name: String,
    val attachment_url: String,
    val batchSubjectId: String,
    val solutionVideoType: String,
    val dRoomId: String,
    val chapter_slug: String
)
