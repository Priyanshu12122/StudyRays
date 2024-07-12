package com.xerox.studyrays.db.videoNotesDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoNoteDao {

    @Upsert
    suspend fun insert(item: VideoNoteEntity)

    @Query("DELETE FROM VideoNoteEntity WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM VideoNoteEntity WHERE videoId = :videoId")
    fun getById(videoId: String): Flow<List<VideoNoteEntity>?>

    @Query("SELECT * FROM VideoNoteEntity")
    fun getAll(): Flow<List<VideoNoteEntity>?>

}