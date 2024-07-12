package com.xerox.studyrays.model.pwModel.batchDetails

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TeacherId(
    @SerializedName("_id") val _id: String,
    @SerializedName("companyId") val companyId: Any,
    @SerializedName("experience") val experience: String,
    @SerializedName("featuredLine") val featuredLine: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("imageId") val imageId: ImageId?,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("qualification") val qualification: String
)
