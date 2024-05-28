package com.xerox.studyrays.model.khazanaModel.khazanaChapters

import androidx.annotation.Keep

@Keep
data class KhazanaChaptersItem(
    val chapter_description: String,
    val chapter_displayOrder: String,
    val chapter_id: String,
    val chapter_imageId: String,
    val chapter_name: String,
    val chapter_organizationId: String,
    val chapter_programId: String,
    val chapter_slug: String,
    val chapter_status: String,
    val chapter_subjectId: String,
    val chapter_totalConcepts: String,
    val chapter_totalExercises: String,
    val chapter_totalExperience: String,
    val chapter_totalFlashCards: String,
    val chapter_totalLectures: String,
    val chapter_totalPodcast: String,
    val chapter_totalSubTopics: String,
    val chapter_totalTopics: String,
    val displayOrder: String,
    val external_id: String,
    val id: String,
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
    val totalPodcast: String,
    val totalSubTopics: String
)