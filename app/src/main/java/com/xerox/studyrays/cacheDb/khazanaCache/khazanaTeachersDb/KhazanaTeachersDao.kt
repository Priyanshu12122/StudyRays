package com.xerox.studyrays.cacheDb.khazanaCache.khazanaTeachersDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface KhazanaTeachersDao {

    @Upsert
    suspend fun insertKhazanaTeacher(item: KhazanaTeachersEntity)

    @Query("SELECT * FROM khazanateachersentity WHERE id = :id")
    suspend fun getKhazanaTeacherString(id: String): KhazanaTeachersEntity?

    @Query("SELECT * FROM KhazanaTeachersEntity")
    suspend fun getAll(): List<KhazanaTeachersEntity>?

}