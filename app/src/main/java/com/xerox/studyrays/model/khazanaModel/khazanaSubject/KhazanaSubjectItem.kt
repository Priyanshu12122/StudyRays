package com.xerox.studyrays.model.khazanaModel.khazanaSubject


import androidx.annotation.Keep

@Keep
data class KhazanaSubjectItem(
    val batchname: String,
    val displayOrder: String,
    val id: String,
    val keyId: String,
    val name: String,
    val organizationId: String,
    val price: String,
    val programId: String,
    val slug: String,
    val status: String,
    val totalChapters: String,
    val totalConcepts: String,
    val totalExercises: String,
    val totalFlashCards: String,
    val totalLectures: String,
    val totalSubTopics: String,
    val totalTopics: String
)