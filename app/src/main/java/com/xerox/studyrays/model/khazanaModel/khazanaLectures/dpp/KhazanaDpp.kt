package com.xerox.studyrays.model.khazanaModel.khazanaLectures.dpp

import androidx.annotation.Keep

@Keep
data class KhazanaDpp(
    val dpps: List<Dpp>,
    val notes: List<Note>
)