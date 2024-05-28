package com.xerox.studyrays.model.akModel.akVideo

data class ClassList(
    val batchDescription: String,
    val batchName: String,
    val btnBuyNowText: String,
    val classes: List<Classe>,
    val emergencyDescription: String,
    val extendedStatus: Int,
    val id: Int,
    val isFree: Int,
    val isPurchased: String,
    val paymentDescription: String,
    val paymentLinkStatus: String
)