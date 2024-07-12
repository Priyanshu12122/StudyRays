package com.xerox.studyrays.model.pwModel

import com.google.gson.annotations.SerializedName

data class SearchOldItem(
    val id: String,
    val name: String,
    @SerializedName("class") val `class`: String,
    val previewImageUrl: String,
)
