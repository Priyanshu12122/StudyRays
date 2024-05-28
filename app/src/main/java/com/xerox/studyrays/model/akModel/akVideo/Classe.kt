package com.xerox.studyrays.model.akModel.akVideo

data class Classe(
    val batchEndDate: String,
    val clsStatus: Int,
    val clsno: Int,
    val commentEnableStatus: Int,
    val commentOn: Int,
    val commentType: Int,
    val downloadStatus: String,
    val downloadUrl: List<Any>,
    val endDateTime: String,
    val id: Int,
    val isLive: String,
    val isPaid: Int,
    val lessonExt: String,
    val lessonName: String,
    val lessonUrl: String,
    val player: Int,
    val posterUrl: String,
    val startDateTime: String,
    val statusIcon: String,
    val timeDuration: String,
    val uniqueId: String
)