package com.xerox.studyrays.db.keyDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KeyEntity::class], version = 1, exportSchema = false)
abstract class KeyDb: RoomDatabase() {
    abstract val dao: KeyDao
}