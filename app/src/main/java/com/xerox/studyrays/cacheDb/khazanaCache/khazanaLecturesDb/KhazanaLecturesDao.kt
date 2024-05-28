package com.xerox.studyrays.cacheDb.khazanaCache.khazanaLecturesDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KhazanaLecturesDao {

    @Upsert
    suspend fun insertKhazanaLecture(item: KhazanaLecturesEntity)

    @Query("SELECT * FROM khazanalecturesentity WHERE topicName = :topicName")
    suspend fun getKhazanaLectureString(topicName: String): KhazanaLecturesEntity?
}