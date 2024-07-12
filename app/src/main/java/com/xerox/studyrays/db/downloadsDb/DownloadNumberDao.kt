package com.xerox.studyrays.db.downloadsDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface DownloadNumberDao {

    @Upsert
    suspend fun upsert(item: DownloadNumberEntity)

    @Query("SELECT * FROM DownloadNumberEntity WHERE id= :id")
    suspend fun getByID(id: Int): DownloadNumberEntity?

    @Delete
    suspend fun delete(item: DownloadNumberEntity)
}