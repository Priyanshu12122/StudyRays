package com.xerox.studyrays.cacheDb.keyGeneratorCache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KeyGenerateEntity(
    @PrimaryKey
    val id: Int,
    val task1_url: String,
    val task1_final_url: String,
    val telegram: String,
    val visible: String
)

//"task1_url": "https://adrinolinks.com/task1",
//    "task2_url": "https://link.vipurl.in/task2",
//    "task1_final_url": "https://devjisu.com/task1",
//    "task2_final_url": "https://devjisu.com/task2",
//    "visible": "0",
//    "telegram": "https://telegram.dog/+PQRH3MJfeacxZWU1"