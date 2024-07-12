package com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class BatchDetailsEntity(
    @PrimaryKey val id: String,
    val courseId: String,
//    val batchCode: String,
//    val batchPdf: String,
    val byName: String?,
    val baseUrl: String?,
    val key: String?,
//    val className: String,
//    val created_at: String,
//    val dataFrom: String,
//    val enableStudyMaterial: String,
//    val exam: String,
    val externalId: String,
    val language: String?,
    val name: String?,
//    val previewVideoId: String,
//    val previewVideoType: String,
//    val previewVideoUrl: String,
//    val program: String,
//    val programId: String,
//    val slug: String,
//    val status: String
)

@Entity
data class PreviewImageEntity(
    @PrimaryKey val id: String,
    val baseUrl: String?,
    val key: String?,
    val name: String?
)

@Entity
data class SubjectEntity(
    @PrimaryKey val id: String,
    val batchDetailsId: String, // Foreign key to BatchDetailsEntity
    val imageId: String?,
    val slug: String?,
    val subject: String?,
    val subjectId: String?,
    val tagCount: Int?
)


@Entity
data class TeacherIdEntity(
    @PrimaryKey val id: String,
    val subjectId: String, // Foreign key to SubjectEntity
    val experience: String?,
    val featuredLine: String?,
    val firstName: String?,
    val imageId: String?, // Foreign key to ImageIdEntity
    val lastName: String,
    val qualification: String?
)

@Entity
data class ImageIdEntity(
    @PrimaryKey val id: String,
    val baseUrl: String?,
    val key: String?,
    val name: String?
)

