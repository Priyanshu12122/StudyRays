package com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppLecturesDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KhazanaDppLecturesDao {

    @Upsert
    suspend fun insert(item: KhazanaDppLecturesEntity)

    @Query("SELECT * FROM khazanadpplecturesentity WHERE topicName = :topicName")
    suspend fun getById(topicName: String): KhazanaDppLecturesEntity?
}