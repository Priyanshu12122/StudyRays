package com.xerox.studyrays.cacheDb.akCache.subjectsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AkSubjectDao {

    @Upsert
    suspend fun upsert(item: AkSubjectEntity)

    @Query("SELECT * FROM AKSUBJECTENTITY WHERE ID = :id")
    suspend fun getById(id: Int): AkSubjectEntity?
}