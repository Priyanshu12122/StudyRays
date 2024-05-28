package com.xerox.studyrays.model.khazanaModel.khazanaTeachersItem

import androidx.annotation.Keep

@Keep
data class KhazanaTeacherItem(
    val description: String,
    val displayOrder: String,
    val external_id: String,
    val id: String,
    val imageId_baseUrl: String,
    val imageId_id: String,
    val imageId_key: String,
    val imageId_name: String,
    val imageId_organization: String,
    val isSpecial: String,
    val name: String,
    val organizationId: String,
    val price: String,
    val programId: String,
    val slug: String,
    val status: String,
    val subjectId: String,
    val totalConcepts: String,
    val totalExercises: String,
    val totalFlashCards: String,
    val totalLectures: String,
    val totalSubTopics: String,
    val totalTopics: String
)