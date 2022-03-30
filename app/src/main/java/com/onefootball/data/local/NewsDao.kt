package com.onefootball.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.onefootball.data.local.entity.NewsEntity

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsEntity>)

    @Query("DELETE FROM news")
    suspend fun deleteNews()

    @Query("SELECT * FROM news")
    suspend fun getNews(): List<NewsEntity>
}