package com.xerox.studyrays.cacheDb.khazanaCache.khazanaChaptersDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KhazanaChaptersDao {

    @Upsert
    suspend fun insertKhazanaChapter(item: KhazanaChaptersEntity)

    @Query("SELECT * FROM khazanachaptersentity WHERE chapterId = :chapterId")
    suspend fun getKhazanaChapterString(chapterId: String): KhazanaChaptersEntity?

    @Query("SELECT * FROM KhazanaChaptersEntity")
    suspend fun getAll(): List<KhazanaChaptersEntity>?

}