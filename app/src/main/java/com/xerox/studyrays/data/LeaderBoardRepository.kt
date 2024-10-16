package com.xerox.studyrays.data

import com.xerox.studyrays.db.userDb.UserDao
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.model.userModel.GetUserItem
import com.xerox.studyrays.model.userModel.PostUserItem
import com.xerox.studyrays.model.userModel.leaderboard.LeaderBoardItem
import com.xerox.studyrays.network.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query
import javax.inject.Inject

class LeaderBoardRepository @Inject constructor(
    private val api: ApiService,
    private val userDao: UserDao
) {

    suspend fun postUserData(user: GetUserItem) {
        api.postUserData(user)
    }

    suspend fun updateUserData(user: PostUserItem): PostUserItem {
        return api.updateUser(user)
    }

    suspend fun getAllUserItem(): List<GetUserItem>? {
        return api.getUserData()
    }

    suspend fun insertUser(userItem: UserEntity) {
        userDao.insert(userItem)
    }

    suspend fun updateUser(
        userNumber: Int,
        name: String,
        exam: String,
        isBanned: String,
        gender: String
    ) {
        userDao.updateUser(
            userNumber = userNumber,
            name = name,
            exam = exam,
            isBanned = isBanned,
            gender = gender
        )
    }

    suspend fun getUserFromDb(userNumber: Int): UserEntity? {
        return userDao.getUser(userNumber)
    }

    fun getUserFromDbFlow(userNumber: Int): Flow<UserEntity?> {
        return userDao.getUserFlow(userNumber)
    }


//    Leaderboard

    suspend fun postLeaderBoardData(item: LeaderBoardItem) {
        api.postLeaderBoardData(item)
    }

    suspend fun getLeaderBoardData(): List<LeaderBoardItem>? {
        return api.getLeaderBoardData()
    }

    suspend fun getCurrentUserLeaderBoardData( userId: String): LeaderBoardItem?{
        return api.getCurrentUserLeaderBoardData(userId)
    }

}