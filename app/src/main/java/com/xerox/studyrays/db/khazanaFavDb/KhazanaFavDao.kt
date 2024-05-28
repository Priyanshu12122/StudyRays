package com.xerox.studyrays.db.khazanaFavDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface KhazanaFavDao {

    @Query("SELECT COUNT(*) FROM khazanafav WHERE externalId = :id")
    suspend fun checkIfPresent(id: String): Boolean

    @Query("SELECT * FROM khazanafav")
    fun getAllCourses(): Flow<List<KhazanaFav?>?>

    @Upsert
    suspend fun updateItem(item: KhazanaFav)

    @Delete
    suspend fun deleteItem(item: KhazanaFav)

}