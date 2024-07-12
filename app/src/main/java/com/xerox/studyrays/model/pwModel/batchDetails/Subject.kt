package com.xerox.studyrays.model.pwModel.batchDetails

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Subject(
    @SerializedName("_id") val _id: String,
    @SerializedName("fileId") val fileId: FileId,
    @SerializedName("imageId") val imageId: ImageId?,
    @SerializedName("schedules") val schedules: List<Schedule>,
    @SerializedName("slug") val slug: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("subjectId") val subjectId: String,
    @SerializedName("tagCount") val tagCount: Int,
    @SerializedName("teacherIds") val teacherIds: List<TeacherId>
)
