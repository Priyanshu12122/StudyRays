package com.xerox.studyrays.cacheDb.khazanaCache.khazanaSubjectsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface KhazanaSubjectDao {
    @Upsert
    suspend fun insertKhazanaSubject(item: KhazanaSubjectEntity)

    @Query("SELECT * FROM khazanasubjectentity WHERE id = :id")
    suspend fun getKhazanaSubjectString(id: String): KhazanaSubjectEntity?

    @Query("SELECT * FROM KhazanaSubjectEntity")
    suspend fun getAll(): List<KhazanaSubjectEntity>?


}