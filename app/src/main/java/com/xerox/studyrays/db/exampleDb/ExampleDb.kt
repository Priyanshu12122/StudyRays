package com.xerox.studyrays.db.exampleDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Example::class], version = 1)
abstract class ExampleDb : RoomDatabase() {
    abstract val dao: ExampleDao
}