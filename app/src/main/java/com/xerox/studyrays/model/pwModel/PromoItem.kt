package com.xerox.studyrays.model.pwModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PromoItem(
    @SerializedName("description") val description: String?,
    @SerializedName("id") val id: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("redirection_link") val redirection_link: String?,
    @SerializedName("title") val title: String
)
