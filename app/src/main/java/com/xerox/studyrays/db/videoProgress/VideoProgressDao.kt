package com.xerox.studyrays.db.videoProgress

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface VideoProgressDao {


    @Upsert
    suspend fun upsert(item: VideoProgressEntity)

    @Query("SELECT * FROM VideoProgressEntity WHERE videoId = :videoId")
    suspend fun getById(videoId: String): VideoProgressEntity?




}