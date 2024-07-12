package com.xerox.studyrays.model.pwModel.batchDetailss

data class BatchDetailsssItem(
    val batchCode: String,
    val batch_id: String,
    val batch_name: String,
    val batch_slug: String,
    val byName: String,
    val `class`: String,
    val dRoomId: String,
    val defaultFbt: String,
    val description: String,
    val faqCat: String,
    val id: Int,
    val khazanaProgramId: String,
    val language: String,
    val markedAsNew: String,
    val orientationClassBanner: String,
    val previewImage: PreviewImage,
    val previewVideoDetails: String,
    val previewVideoType: String,
    val previewVideoUrl: String,
    val shortDescription: String,
    val subjects: List<Subject>
)