package com.xerox.studyrays.db.userDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDb: RoomDatabase() {
    abstract val dao: UserDao
}