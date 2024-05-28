package com.xerox.studyrays.db.khazanaFavDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KhazanaFav::class],version = 1)
abstract class KhazanaFavDb: RoomDatabase() {
    abstract val dao: KhazanaFavDao
}