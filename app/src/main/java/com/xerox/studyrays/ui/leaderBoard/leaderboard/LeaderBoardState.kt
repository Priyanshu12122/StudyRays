package com.xerox.studyrays.ui.leaderBoard.leaderboard

import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItemWithRank

data class LeaderBoardState(
    val selectedUser: LeaderBoardItemWithRank? = null,
    val currentUser: UserEntity? = null,
    val searchText: String = "",
    val isSearching: Boolean = false,
    val isUserInfoAlertDialogOpen: Boolean = false,
    val isOpen: Boolean = false,
    val isUploadUserDialogOpen: Boolean = false,
    val isSortByDialogOpen: Boolean = false,
    val expanded: Boolean = false,
    val leaderBoardList: List<LeaderBoardItemWithRank>? = emptyList(),
    val filteredList: List<LeaderBoardItemWithRank>? = emptyList(),
    val examFilteredList: List<LeaderBoardItemWithRank>? = emptyList(),
    val allSortList: List<String> = listOf(
        "NONE", "IIT-JEE", "NEET", "CA", "UPSC", "NDA", "BOARD-EXAM",
        "COMMERCE", "GATE", "SARKARI-JOB", "FOUNDATION", "OLYMPIAD", "OTHERS"
    ),
    val selectedIndex: Int = 0,
)
