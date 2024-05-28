package com.xerox.studyrays.model.pwModel.batchDetails

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Schedule(
    @SerializedName("_id") val _id: String,
    @SerializedName("day") val day: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("startTime") val startTime: String
)
