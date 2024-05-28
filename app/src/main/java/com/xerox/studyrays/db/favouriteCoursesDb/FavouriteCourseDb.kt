package com.xerox.studyrays.db.favouriteCoursesDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouriteCourse::class], version = 1, exportSchema = false)
abstract class FavouriteCourseDb: RoomDatabase() {
    abstract val dao: FavouriteCourseDao
}