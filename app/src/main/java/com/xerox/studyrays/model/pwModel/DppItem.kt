package com.xerox.studyrays.model.pwModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DppItem(
    @SerializedName("attachment_base_url") val attachment_base_url: String?,
    @SerializedName("attachment_id") val attachment_id: String,
    @SerializedName("attachment_key") val attachment_key: String?,
    @SerializedName("attachment_name") val attachment_name: String,
    @SerializedName("batch_subject_id") val batch_subject_id: String,
    @SerializedName("external_id") val external_id: String,
    @SerializedName("homework_id") val homework_id: String,
    @SerializedName("id") val id: String,
    @SerializedName("note") val note: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("solution_video_type") val solution_video_type: String,
    @SerializedName("topic") val topic: String?
)
