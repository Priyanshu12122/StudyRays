package com.xerox.studyrays.model.pwModel.batchDetails

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BatchDetailsX(
    @SerializedName("batchCode") val batchCode: String,
    @SerializedName("batchPdf") val batchPdf: String,
    @SerializedName("byName") val byName: String,
    @SerializedName("class") val `class`: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("dataFrom") val dataFrom: String,
    @SerializedName("enableStudyMaterial") val enableStudyMaterial: String,
    @SerializedName("exam") val exam: String,
    @SerializedName("external_id") val external_id: String,
    @SerializedName("id") val id: String,
    @SerializedName("language") val language: String,
    @SerializedName("name") val name: String,
    @SerializedName("orientationClassBanner") val orientationClassBanner: OrientationClassBanner,
    @SerializedName("previewImage") val previewImage: PreviewImage?,
    @SerializedName("previewVideoId") val previewVideoId: String,
    @SerializedName("previewVideoType") val previewVideoType: String,
    @SerializedName("previewVideoUrl") val previewVideoUrl: String,
    @SerializedName("program") val program: String,
    @SerializedName("programId") val programId: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("status") val status: String
)
