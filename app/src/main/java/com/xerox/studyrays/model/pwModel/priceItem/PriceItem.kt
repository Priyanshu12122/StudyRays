package com.xerox.studyrays.model.pwModel.priceItem

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PriceItem(
    @SerializedName("total_amount") val total_amount: Int
)