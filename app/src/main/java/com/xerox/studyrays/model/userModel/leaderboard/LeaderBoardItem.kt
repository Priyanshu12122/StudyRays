package com.xerox.studyrays.model.userModel.leaderboard

data class LeaderBoardItem(
    val date: String,
    val exam: String,
    val isBanned: String,
    val subject_studies: List<SubjectStudy>,
    val today_study_hours: String,
    val user_id: String,
    val user_name: String?,
    val gender: String,
)


data class LeaderBoardItemWithRank(
    val date: String,
    val exam: String,
    val isBanned: String,
    val subject_studies: List<SubjectStudy>,
    val today_study_hours: String,
    val user_id: String,
    val user_name: String?,
    val gender: String,
    val rank: Int,
)