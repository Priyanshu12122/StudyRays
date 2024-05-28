package com.xerox.studyrays.model.khazanaModel.khazana

import androidx.annotation.Keep

@Keep
data class KhazanaItem(
    val id: String,
    val image: String,
    val name: String,
    val showorder: String,
    val slug: String,
    val status: String
)