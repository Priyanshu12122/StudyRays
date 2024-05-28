package com.xerox.studyrays.model.akModel.akLessons

data class Data(
    val allClsCount: Int,
    val batch_detail: BatchDetail,
    val batch_topic: List<BatchTopic>,
    val status_todayLive: String,
    val subjectId: String,
    val todayClsCount: Int
)