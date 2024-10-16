package com.xerox.studyrays.ui.leaderBoard.leaderboard

import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItemWithRank

sealed class LeaderBoardEvent {

    data class SelectedUserChange(val user: LeaderBoardItemWithRank?) : LeaderBoardEvent()
    data class OnSearchTextChange(val searchText: String) : LeaderBoardEvent()
    data class SetIsSearching(val isSearching: Boolean) : LeaderBoardEvent()
    data class SetIsOpen(val isOpen: Boolean) : LeaderBoardEvent()
    data class SetIsUserInfoAlertDialogOpen(val isOpen: Boolean) : LeaderBoardEvent()
    data class SetIsUploadUserDialogOpen(val isOpen: Boolean) : LeaderBoardEvent()
    data class SetIsSortByDialogOpen(val isOpen: Boolean) : LeaderBoardEvent()
    data class SetExpanded(val expanded: Boolean) : LeaderBoardEvent()
    data class UpdateLeaderBoardList(val leaderBoardList: List<LeaderBoardItemWithRank>?) : LeaderBoardEvent()
    data class UpdateFilteredList(val leaderBoardList: List<LeaderBoardItemWithRank>?) : LeaderBoardEvent()
    data class SetSelectedIndex(val index: Int) : LeaderBoardEvent()
    data class SetCurrentUser(val userEntity: UserEntity?) : LeaderBoardEvent()
    data class OnFilterExamChanged(val list: List<LeaderBoardItemWithRank>,val exam: String) : LeaderBoardEvent()
}