package com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NotesDao {

    @Upsert
    suspend fun insert(item: NotesEntityy)

    @Query("SELECT * FROM NotesEntityy WHERE topicSlug = :topicSlug")
    suspend fun getById(topicSlug: String): NotesEntityy?

    @Query("SELECT * FROM NotesEntityy")
    suspend fun getAll(): List<NotesEntityy>?

}