package com.xerox.studyrays.db.favouriteCoursesDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCourseDao {

    @Query("SELECT COUNT(*) FROM favouritecourse WHERE externalId = :favourite")
    suspend fun checkIfPresent(favourite: String): Boolean

    @Query("SELECT * FROM FavouriteCourse")
    fun getAllCourses(): Flow<List<FavouriteCourse?>?>

    @Upsert
    suspend fun updateItem(item: FavouriteCourse)

    @Delete
    suspend fun deleteItem(item: FavouriteCourse)
}