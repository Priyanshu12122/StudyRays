package com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Upsert
    suspend fun insert(item: SearchEntity)

    @Delete
    suspend fun delete(item: SearchEntity)

    @Query("SELECT * FROM searchentity")
    fun getAll(): Flow<List<SearchEntity>?>

    @Query("SELECT EXISTS(SELECT 1 FROM searchentity WHERE searchText = :searchText LIMIT 1)")
    suspend fun exists(searchText: String): Boolean

}