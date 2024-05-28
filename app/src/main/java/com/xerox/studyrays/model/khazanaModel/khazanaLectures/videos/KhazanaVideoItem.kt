package com.xerox.studyrays.model.khazanaModel.khazanaLectures.videos

import androidx.annotation.Keep

@Keep
data class KhazanaVideoItem(
    val chapter_id: String,
    val chapter_id_1: String,
    val chapter_id_2: String,
    val chapter_name: String,
    val external_id: String,
    val id: String,
    val program_id: String,
    val program_slug: String,
    val subject_id: String,
    val subject_name: String,
    val title: String,
    val topic_id: String,
    val topic_name: String,
    val type: String,
    val video_created_at: String?,
    val video_duration: String?,
    val video_id: String,
    val video_image: String?,
    val video_name: String?,
    val video_url: String?
)