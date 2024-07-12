package com.xerox.studyrays.db.watchLaterDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchLaterDao {

    @Upsert
    suspend fun upsert(item: WatchLaterEntity)

    @Query("SELECT COUNT(*) FROM WatchLaterEntity WHERE videoId = :videoID")
    suspend fun checkIfPresentInWatchLater(videoID: String): Boolean

    @Query("SELECT COUNT(*) FROM WatchLaterEntity WHERE videoId = :videoID")
    fun checkIfPresentInWatchLaterFlow(videoID: String): Flow<Boolean>

    @Query("DELETE FROM WatchLaterEntity WHERE videoId = :videoID")
    suspend fun deleteByVideoId(videoID: String)

    @Delete
    suspend fun delete(item: WatchLaterEntity)

    @Query("SELECT * FROM WatchLaterEntity WHERE videoId = :videoId")
    suspend fun getById(videoId: String): WatchLaterEntity?

    @Query("SELECT * FROM WatchLaterEntity")
    fun getAll(): Flow<List<WatchLaterEntity>>

}