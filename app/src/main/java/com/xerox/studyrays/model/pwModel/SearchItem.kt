package com.xerox.studyrays.model.pwModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SearchItem(
    @SerializedName("class") val `class`: String,
    @SerializedName("batch_id") val batch_id: String,
    @SerializedName("batch_slug") val batch_slug: String,
    @SerializedName("batch_name") val batch_name: String,
    @SerializedName("previewImageUrl") val previewImageUrl: String
)
