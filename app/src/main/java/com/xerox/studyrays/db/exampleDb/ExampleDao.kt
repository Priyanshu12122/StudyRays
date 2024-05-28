package com.xerox.studyrays.db.exampleDb

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ExampleDao {

    @Upsert
    suspend fun insertExample(ex: Example)

    @Query("SELECT * FROM Example")
    suspend fun getAllExamples(): List<Example>

}