package com.xerox.studyrays.cacheDb.akCache.NotesDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AkNoteDao {
    @Upsert
    suspend fun upsert(item: AkNotesEntity)

    @Query("SELECT * FROM AkNotesEntity WHERE tid = :tid")
    suspend fun getById(tid: Int): AkNotesEntity?
}