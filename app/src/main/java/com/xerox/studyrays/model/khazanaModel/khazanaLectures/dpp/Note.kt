package com.xerox.studyrays.model.khazanaModel.khazanaLectures.dpp

import androidx.annotation.Keep

@Keep
data class Note(
    val chapter_id: String,
    val chapter_name: String,
    val content_id: String,
    val createdAt: String,
    val external_id: String,
    val file_id: String,
    val id: String,
    val program_id: String,
    val program_slug: String,
    val subject_id: String,
    val subject_name: String,
    val title: String?,
    val topic_id: String,
    val topic_name: String,
    val url: String?
)