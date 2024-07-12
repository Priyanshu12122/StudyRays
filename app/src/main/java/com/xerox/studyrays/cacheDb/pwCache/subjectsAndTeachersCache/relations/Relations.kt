package com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.BatchDetailsEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.ImageIdEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.SubjectEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.TeacherIdEntity

data class BatchDetailsWithSubjects(
    @Embedded val batchDetails: BatchDetailsEntity,
    @Relation(
        entity = SubjectEntity::class,
        parentColumn = "id",
        entityColumn = "batchDetailsId"
    )
    val subjects: List<SubjectWithTeachersAndImageId>
)


data class SubjectWithTeachersAndImageId(
    @Embedded val subject: SubjectEntity,
    @Relation(
        entity = TeacherIdEntity::class,
        parentColumn = "id",
        entityColumn = "subjectId"
    )
    val teacherIds: List<TeacherIdWithImage>,
    @Relation(
        entity = ImageIdEntity::class,
        parentColumn = "imageId",
        entityColumn = "id"
    )
    val image: ImageIdEntity?
)

data class TeacherIdWithImage(
    @Embedded val teacherId: TeacherIdEntity?,
    @Relation(
        entity = ImageIdEntity::class,
        parentColumn = "imageId",
        entityColumn = "id"
    )
    val imageId: ImageIdEntity?
)
