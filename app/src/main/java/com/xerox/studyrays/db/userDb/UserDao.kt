package com.xerox.studyrays.db.userDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun insert(userEntity: UserEntity)

    @Query("UPDATE UserEntity SET name = :name, exam = :exam, gender = :gender, isBanned = :isBanned WHERE userNumber = :userNumber")
    suspend fun updateUser(userNumber: Int,name: String, exam: String, isBanned: String,gender: String)


    @Query("SELECT * FROM UserEntity WHERE userNumber = :userNumber")
    suspend fun getUser(userNumber: Int): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE userNumber = :userNumber")
    fun getUserFlow(userNumber: Int): Flow<UserEntity?>



}