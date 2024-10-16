package com.xerox.studyrays.model.userModel

data class GetUserItem(
    val user_id: String,
    val user_name: String,
    val isBanned: Int,
    val exam: String,
    val date: String,
    val gender: String,
)

data class PostUserItem(
    val user_id: String,
    val user_name: String,
    val isBanned: Int,
    val exam: String,
//    val date: String,
    val gender: String,
)
