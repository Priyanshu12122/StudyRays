package com.xerox.studyrays.cacheDb.pwCache.notesDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PwNotesDao {

    @Upsert
    suspend fun insert(item: PwNotesEntity)

    @Query("SELECT * FROM PwNotesEntity WHERE slug = :slug")
    suspend fun getById(slug: String): List<PwNotesEntity>?
}