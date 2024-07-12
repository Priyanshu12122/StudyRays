package com.xerox.studyrays.cacheDb.keyGeneratorCache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KeyGenerateEntity::class], version = 1, exportSchema = false)
abstract class KeyGenerateDb: RoomDatabase() {
    abstract val dao: KeyGenerateDao
}