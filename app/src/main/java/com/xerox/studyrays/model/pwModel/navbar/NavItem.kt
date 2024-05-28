package com.xerox.studyrays.model.pwModel.navbar

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NavItem(
    @SerializedName("description") val description: String,
    @SerializedName("dismissable") val dismissable: String,
    @SerializedName("email") val email: String,
    @SerializedName("image") val image: String,
    @SerializedName("redirect") val redirect: String,
    @SerializedName("share") val share: String,
    @SerializedName("status") val status: String,
    @SerializedName("telegram") val telegram: String,
    @SerializedName("website") val website: String
)
