package com.xerox.studyrays.model.videoPlayerModel

data class TimelineItem(
    val batchId: String,
    val batchSubjectId: String,
    val external_id: String,
    val homeworkIds: String,
    val id: Int,
    val slide_image_url: String,
    val slide_slug: String,
    val slide_timestamp: String,
    val slides_id: String,
    val slides_serial_number: Int,
    val slug: String,
    val teachers_id: String,
    val topic: String,
    val video_external_id: String,
    val video_id: String
)