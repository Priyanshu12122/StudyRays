package com.xerox.studyrays.model.pwModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SearchItem(
    @SerializedName("class") val `class`: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("previewImageUrl") val previewImageUrl: String
)
