package com.xerox.studyrays.model.pwModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LessonItem(
    @SerializedName("created_at") val created_at: String,
    @SerializedName("dataFrom") val dataFrom: String,
    @SerializedName("displayOrder") val displayOrder: String,
    @SerializedName("exercises") val exercises: String,
    @SerializedName("external_id") val external_id: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("notes") val notes: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("type") val type: String,
    @SerializedName("typeId") val typeId: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("videos") val videos: String
)
