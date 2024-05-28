package com.xerox.studyrays.model.akModel.akSubjects

data class BatchDetail(
    val batchDescription: String,
    val batchName: String,
    val btnBuyNowText: String,
    val emergencyDescription: String,
    val extendedStatus: Int,
    val id: Int,
    val isFree: Int,
    val isPurchased: String,
    val paymentDescription: String,
    val paymentLinkStatus: String,
    val shareDeeplink: String
)