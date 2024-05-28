package com.xerox.studyrays.db.videoDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

//    @Insert
//    suspend fun insert(videoWatchHistory: Video)
//
//    @Update
//    suspend fun update(videoWatchHistory: Video)
//
//    @Query("SELECT isCompleted FROM Video WHERE videoId = :videoId")
//    suspend fun getIsCompleted(videoId: String): Boolean
//
//    @Query("SELECT * FROM Video WHERE videoId = :videoId")
//    fun getVideoWatchHistory(videoId: String): Flow<Video?>
//
//    @Query("SELECT * FROM video")
//    fun getAllVideos(): Flow<List<Video>?>

    @Query("SELECT COUNT(*) FROM video WHERE videoId = :videoID")
    suspend fun checkIfPresentInDbVIdeo(videoID: String): Boolean


    @Query("SELECT * FROM Video")
    fun getAllVideoLectures(): Flow<List<Video?>?>

    @Upsert
    suspend fun upsertItem(item: Video)

    @Delete
    suspend fun deleteItem(item: Video)

}