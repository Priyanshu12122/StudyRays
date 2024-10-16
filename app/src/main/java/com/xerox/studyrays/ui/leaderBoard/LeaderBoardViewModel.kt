package com.xerox.studyrays.ui.leaderBoard

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xerox.studyrays.data.LeaderBoardRepository
import com.xerox.studyrays.data.StudyFocusRepository
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.model.userModel.GetUserItem
import com.xerox.studyrays.model.userModel.PostUserItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItemWithRank
import com.xerox.studyrays.model.userModel.leaderboard.SubjectStudy
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.leaderboard.LeaderBoardEvent
import com.xerox.studyrays.ui.leaderBoard.leaderboard.LeaderBoardState
import com.xerox.studyrays.utils.toHours
import com.xerox.studyrays.utils.toReadableDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModel @Inject constructor(
    val repository: LeaderBoardRepository,
    private val studyRepository: StudyFocusRepository,
) : ViewModel() {

    val noInternetMsg = "Internet Unavailable."
    val nullErrorMsg = "Null"
    private val socketErrorMsg = "Socket timeout: Either slow internet or server issue."

    private fun getUserId(): String {
        return UUID.randomUUID().toString()
    }

    private val _postUser: MutableStateFlow<Response<GetUserItem>> =
        MutableStateFlow(Response.Loading())
    val postUser = _postUser.asStateFlow()


    fun postUserData(
        name: String,
        gender: String,
        exam: String,
    ) {
        viewModelScope.launch {
            try {
                _postUser.value = Response.Loading()
                val allUsers = repository.getAllUserItem()
                var userId = getUserId()
                allUsers?.let {
                    while (allUsers.any { it.user_id == userId }) {
                        userId = getUserId()
                    }
                }
                val user = GetUserItem(
                    user_id = userId,
                    gender = gender,
                    exam = exam,
                    isBanned = 0,
                    user_name = name,
                    date = System.currentTimeMillis().toReadableDate()
                )

                repository.postUserData(user)
                _postUser.value = Response.Success(user)
            } catch (e: SocketTimeoutException) {
                _postUser.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _postUser.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _postUser.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }

        }
    }

    private val _updateUser: MutableStateFlow<Response<PostUserItem>> =
        MutableStateFlow(Response.Loading())
    val updateUser: StateFlow<Response<PostUserItem>> = _updateUser.asStateFlow()

    fun updateUserData(userItem: PostUserItem) {
        viewModelScope.launch {
            try {
                _updateUser.value = Response.Loading()
                val response = repository.updateUserData(userItem)
                _updateUser.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _updateUser.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _updateUser.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _updateUser.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    private val _allUsers: MutableStateFlow<Response<List<GetUserItem>?>> =
        MutableStateFlow(Response.Loading())
    val allUsers: StateFlow<Response<List<GetUserItem>?>> = _allUsers.asStateFlow()


    fun getAllUsers() {
        viewModelScope.launch {
            try {
                _allUsers.value = Response.Loading()
                val response = repository.getAllUserItem()
                _allUsers.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _allUsers.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allUsers.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allUsers.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    fun insertUser(userEntity: UserEntity) {
        viewModelScope.launch {
            repository.insertUser(userEntity)
        }
    }

    suspend fun getUserByIdFromDb(userNumber: Int): UserEntity? {
        return repository.getUserFromDb(userNumber)
    }

    fun updateUser(
        userNumber: Int,
        name: String,
        exam: String,
        isBanned: String,
        gender: String
    ) {
        viewModelScope.launch {
            repository.updateUser(
                userNumber = userNumber,
                name = name,
                exam = exam,
                isBanned = isBanned,
                gender = gender
            )
        }
    }

    private val _isUserPresent: MutableStateFlow<Response<Boolean>> =
        MutableStateFlow(Response.Loading())
    val isUserPresent = _isUserPresent.asStateFlow()

    fun checkIfLoggedIn() {
        viewModelScope.launch {
            try {
                _isUserPresent.value = Response.Loading()
                val response = repository.getUserFromDb(1)
                if (response != null) {
                    _isUserPresent.value = Response.Success(true)
                } else {
                    _isUserPresent.value = Response.Success(false)
                }

            } catch (e: Exception) {
                _isUserPresent.value = Response.Error(e.localizedMessage ?: "An Error Occurred!")
            }
        }
    }

    private val _userFromDb: MutableStateFlow<Response<UserEntity?>> =
        MutableStateFlow(Response.Loading())
    val userFromDb: StateFlow<Response<UserEntity?>> = _userFromDb.asStateFlow()


    fun getUserFromDbFlow(userNumber: Int) {
        viewModelScope.launch {
            try {
                _userFromDb.value = Response.Loading()
                val response = repository.getUserFromDb(userNumber)
                _userFromDb.value = Response.Success(response)
            } catch (e: Exception) {
                _userFromDb.value = Response.Error(e.localizedMessage ?: "An Error occurred")
            }
        }
    }

    suspend fun getUserFromDb(userNumber: Int): UserEntity? {
        return repository.getUserFromDb(userNumber)
    }


    fun showToast(context: Context, msg: String, isLong: Boolean) {
        Toast.makeText(context, msg, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

//    Leaderboard

    private val _postLeaderboard: MutableStateFlow<Response<LeaderBoardItem>> =
        MutableStateFlow(Response.Loading())
    val postLeaderboard = _postLeaderboard.asStateFlow()

    fun postLeaderBoardData(item: LeaderBoardItem) {
        viewModelScope.launch {
            try {
                _postLeaderboard.value = Response.Loading()
                repository.postLeaderBoardData(item)
                _postLeaderboard.value = Response.Success(item)
            } catch (e: SocketTimeoutException) {
                _postLeaderboard.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _postLeaderboard.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _postLeaderboard.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    private val _getLeaderboard: MutableStateFlow<Response<List<LeaderBoardItemWithRank>?>> =
        MutableStateFlow(Response.Loading())
    val getLeaderboard = _getLeaderboard.asStateFlow()

    fun getLeaderBoardData() {
        viewModelScope.launch {
            try {
                _getLeaderboard.value = Response.Loading()
                val response = repository.getLeaderBoardData()
                val itemsWithRank = response?.let { rankUsers(it) }
                onEvent(LeaderBoardEvent.UpdateLeaderBoardList(itemsWithRank))
                Log.d("TAG", "getLeaderBoardData: users = $itemsWithRank")
                _getLeaderboard.value = Response.Success(itemsWithRank)
            } catch (e: SocketTimeoutException) {
                _getLeaderboard.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _getLeaderboard.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _getLeaderboard.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    private val _currentLeaderBoardUser: MutableStateFlow<Response<LeaderBoardItem?>> =
        MutableStateFlow(Response.Loading())
    val currentLeaderBoardUser: StateFlow<Response<LeaderBoardItem?>> =
        _currentLeaderBoardUser.asStateFlow()

    fun getCurrentLeaderBoardUser(userId: String) {
        viewModelScope.launch {
            try {
                _currentLeaderBoardUser.value = Response.Loading()
                val response = repository.getCurrentUserLeaderBoardData(userId)
                _currentLeaderBoardUser.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _currentLeaderBoardUser.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _currentLeaderBoardUser.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _currentLeaderBoardUser.value =
                    Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    data class LeaderBoardUploadState(
        val user: UserEntity? = null,
        val subjects: List<SubjectStudy> = emptyList(),
        val totalStudyTime: Float = 0f
    )

    private val _leaderBoardUpload: MutableStateFlow<Response<LeaderBoardUploadState>> =
        MutableStateFlow(Response.Loading())
    val leaderBoardUpload = _leaderBoardUpload.asStateFlow()
//    val leaderBoardUpload = combine(
//        _leaderBoardUpload,
//        repository.getUserFromDbFlow(1),
//        studyRepository.getAllSubjects()
//    ) { leaderBoardState, user, subjects ->
//        leaderBoardState.copy(
//            user = user,
//            subjects = subjects
//        )
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(),
//        LeaderBoardState()
//    )

    private val _totalDuration: MutableStateFlow<Long> = MutableStateFlow(0)
    val totalDuration: StateFlow<Long> = _totalDuration.asStateFlow()


    fun getTotalSessionsDurationNormal() {
        viewModelScope.launch {
             studyRepository.getTotalSessionsDuration().collect{
                 _totalDuration.value = it
             }
        }
    }

    fun getLeaderboardItemsToUpload() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _leaderBoardUpload.value = Response.Loading()
                val userDeferred = async { repository.getUserFromDb(1) }
                val subjectsDeferred = async { studyRepository.getAllSubjectsNormal() }
                val totalStudiedHoursDeferred =
                    async { studyRepository.getTotalSessionsDurationNormal() }

                val user = userDeferred.await()
                val subjects = subjectsDeferred.await()
                val totalStudiedHours = totalStudiedHoursDeferred.await()

                val studySubjectsList = mutableListOf<SubjectStudy>()

                if (subjects.isEmpty() || totalStudiedHours <= 0) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    subjects.forEach {
                        val subjectStudyTime =
                            async { studyRepository.getTotalSessionsDurationBySubjectNormal(it.subjectId!!) }
                        val time = subjectStudyTime.await()

                        if (time > 0) {
                            studySubjectsList.add(
                                SubjectStudy(
                                    subject = it.name,
                                    study_time = time.toHours()
                                )
                            )
                        }

                        _leaderBoardUpload.value = Response.Success(
                            LeaderBoardUploadState(
                                user = user,
                                subjects = studySubjectsList,
                                totalStudyTime = totalStudiedHours.toHours()
                            )
                        )


                    }
                }


            } catch (e: SocketTimeoutException) {
                _leaderBoardUpload.value = Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _leaderBoardUpload.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _leaderBoardUpload.value =
                    Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    fun getUserRank(userId: String, userList: List<LeaderBoardItem>): Int {
        val index = userList.indexOfFirst { it.user_id == userId }
        return if (index != -1) index + 1 else 0
    }

    fun getSearchUser(query: String, userList: List<LeaderBoardItem>): List<LeaderBoardItem>? {
        return userList.filter { it.user_name == query || it.user_id == query }
    }


    private fun LeaderBoardItem.toLeaderBoardItemWithRank(rank: Int): LeaderBoardItemWithRank {
        return LeaderBoardItemWithRank(
            date = this.date,
            exam = this.exam,
            isBanned = this.isBanned,
            subject_studies = this.subject_studies,
            today_study_hours = this.today_study_hours,
            user_id = this.user_id,
            user_name = this.user_name,
            gender = this.gender,
            rank = rank
        )
    }

    private fun LeaderBoardItemWithRank.toLeaderBoardItem(): LeaderBoardItem {
        return LeaderBoardItem(
            date = this.date,
            exam = this.exam,
            isBanned = this.isBanned,
            subject_studies = this.subject_studies,
            today_study_hours = this.today_study_hours,
            user_id = this.user_id,
            user_name = this.user_name,
            gender = this.gender,
        )
    }

    private fun rankUsers(usersList: List<LeaderBoardItem>): List<LeaderBoardItemWithRank> {
        val rankedUsers = usersList.sortedByDescending { it.today_study_hours }
        return rankedUsers.mapIndexed { index, leaderBoardItem ->
            leaderBoardItem.toLeaderBoardItemWithRank(index)
        }
//        _getLeaderboard.value = rankedUsers
    }


    private val _state = MutableStateFlow(LeaderBoardState())
    val state = _state.asStateFlow()

    fun onEvent(event: LeaderBoardEvent) {
        when (event) {
            is LeaderBoardEvent.SelectedUserChange -> {
                _state.update { it.copy(selectedUser = event.user) }
            }

            is LeaderBoardEvent.OnSearchTextChange -> {
                _state.update { it.copy(searchText = event.searchText) }
            }

            is LeaderBoardEvent.SetIsSearching -> {
                _state.update { it.copy(isSearching = event.isSearching) }
            }

            is LeaderBoardEvent.SetIsUserInfoAlertDialogOpen -> {
                _state.update { it.copy(isUserInfoAlertDialogOpen = event.isOpen) }
            }

            is LeaderBoardEvent.SetIsUploadUserDialogOpen -> {
                _state.update { it.copy(isUploadUserDialogOpen = event.isOpen) }
            }

            is LeaderBoardEvent.SetIsSortByDialogOpen -> {
                _state.update { it.copy(isSortByDialogOpen = event.isOpen) }
            }

            is LeaderBoardEvent.SetExpanded -> {
                _state.update { it.copy(expanded = event.expanded) }
            }

            is LeaderBoardEvent.UpdateLeaderBoardList -> {
                _state.update { it.copy(leaderBoardList = event.leaderBoardList) }
            }

            is LeaderBoardEvent.SetSelectedIndex -> {
                _state.update { it.copy(selectedIndex = event.index) }
            }

            is LeaderBoardEvent.SetIsOpen -> {
                _state.update { it.copy(isOpen = event.isOpen) }
            }

            is LeaderBoardEvent.SetCurrentUser -> {
                _state.update { it.copy(currentUser = event.userEntity) }
            }

            is LeaderBoardEvent.UpdateFilteredList -> {
                _state.update { it.copy(filteredList = event.leaderBoardList) }
            }

            is LeaderBoardEvent.OnFilterExamChanged -> {


                _state.update {
                    val normalItem: List<LeaderBoardItem> = event.list.filter {
                        it.exam.equals(
                            event.exam,
                            true
                        )
                    }.map {
                        it.toLeaderBoardItem()
                    }

                    val rankedLeaderBoardItem =
                        normalItem.sortedByDescending { it.today_study_hours }
                            .mapIndexed { index, leaderBoardItem ->
                                leaderBoardItem.toLeaderBoardItemWithRank(index)
                            }
                    it.copy(examFilteredList = rankedLeaderBoardItem)
                }
            }


        }

    }


}