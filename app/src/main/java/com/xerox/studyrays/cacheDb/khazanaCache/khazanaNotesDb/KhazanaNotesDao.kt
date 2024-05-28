package com.xerox.studyrays.cacheDb.khazanaCache.khazanaNotesDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KhazanaNotesDao {

    @Upsert
    suspend fun insert(item: KhazanaNotesEntity)

    @Query("SELECT * FROM khazananotesentity WHERE topicName = :topicName")
    suspend fun getById(topicName: String): KhazanaNotesEntity?
}