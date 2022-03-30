package com.onefootball.domain.model

import com.onefootball.data.local.entity.NewsEntity

data class News(
    val id: Int,
    val title: String,
    val image_url: String,
    val resource_name: String,
    val resource_url: String,
    val news_link: String
){
    fun toNewsEntity() : NewsEntity{
        return NewsEntity(
            null,
            title,
            image_url,
            resource_name,
            resource_url,
            news_link
        )
    }

    override fun equals(other: Any?): Boolean {

        if(javaClass != other?.javaClass)
            return false

        other as News

        if(title != other.title
            || image_url != other.image_url
            || resource_name != other.resource_name
            || resource_url != resource_url
            || news_link != other.news_link)
                return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + image_url.hashCode()
        result = 31 * result + resource_name.hashCode()
        result = 31 * result + resource_url.hashCode()
        result = 31 * result + news_link.hashCode()
        return result
    }
}
