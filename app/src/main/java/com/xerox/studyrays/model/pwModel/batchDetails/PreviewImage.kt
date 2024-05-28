package com.xerox.studyrays.model.pwModel.batchDetails

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PreviewImage(
    @SerializedName("_id") val _id: String,
    @SerializedName("baseUrl") val baseUrl: String?,
    @SerializedName("key") val key: String?,
    @SerializedName("name") val name: String
)
