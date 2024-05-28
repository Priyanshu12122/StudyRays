package com.xerox.studyrays.model.pwModel.alertItem

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AlertItem(
    @SerializedName("description") val description: String,
    @SerializedName("dismissable") val dismissable: String,
    @SerializedName("image") val image: String,
    @SerializedName("redirect") val redirect: String,
    @SerializedName("status") val status: String,
    @SerializedName("versions") val versions: List<String>
)