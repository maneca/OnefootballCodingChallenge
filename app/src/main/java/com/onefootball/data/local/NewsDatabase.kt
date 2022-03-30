package com.onefootball.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onefootball.data.local.entity.NewsEntity

@Database(
    entities = [NewsEntity::class],
    version = 1
)
abstract class NewsDatabase: RoomDatabase() {

    abstract val dao: NewsDao
}