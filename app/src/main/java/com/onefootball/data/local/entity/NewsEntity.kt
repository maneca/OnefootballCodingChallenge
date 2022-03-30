package com.onefootball.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onefootball.domain.model.News

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id : Int? = null,
    val title: String,
    val image_url: String,
    val resource_name: String,
    val resource_url: String,
    val news_link: String
){
    fun toNews(): News {
        return News(
            id ?: 0,
            title,
            image_url,
            resource_name,
            resource_url,
            news_link
        )
    }
}
