package com.xerox.studyrays.model.pwModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DppSolutionItem(
    @SerializedName("batch_id") val batch_id: String,
    @SerializedName("chapterId") val chapterId: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("external_id") val external_id: String,
    @SerializedName("id") val id: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("name") val name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("video_id") val video_id: String,
    @SerializedName("video_url") val video_url: String
)
