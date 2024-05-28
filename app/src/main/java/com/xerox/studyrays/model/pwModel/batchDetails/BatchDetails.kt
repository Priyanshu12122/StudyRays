package com.xerox.studyrays.model.pwModel.batchDetails

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BatchDetails(
    @SerializedName("batch_details") val batch_details: BatchDetailsX,
    @SerializedName("subjects") val subjects: List<Subject>
)
