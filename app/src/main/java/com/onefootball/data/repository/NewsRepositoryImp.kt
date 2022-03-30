package com.onefootball.data.repository

import com.google.gson.Gson
import com.onefootball.data.local.NewsDao
import com.onefootball.data.remote.NewsApi
import com.onefootball.domain.model.News
import com.onefootball.domain.repository.NewsRepository
import com.onefootball.util.CustomExceptions
import com.onefootball.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class NewsRepositoryImp(
    private val api: NewsApi,
    private val dao: NewsDao
): NewsRepository {

    override fun getNews(): Flow<Resource<List<News>>> = flow {
        emit(Resource.Loading())

        val news = dao.getNews().map { it.toNews() }
        emit(Resource.Loading(news))

        try{
            val remoteNews = parseJsonString(api.readFile("news.json"))
            dao.deleteNews()
            dao.insertNews(remoteNews.map { it.toNewsEntity() })
            val newNews = dao.getNews().map { it.toNews() }
            emit(Resource.Success(newNews))
        }catch (e: IOException){
            emit(Resource.Error(
                exception = CustomExceptions.UnableToReadFile,
                data = news
            ))
        }catch (e: Exception){
            emit(Resource.Error(
                exception = CustomExceptions.UnknownException,
                data = news
            ))
        }
    }

    private fun parseJsonString(jsonString: String): List<News> {
        val mainObject = JSONObject(jsonString)
        val newsItems = mutableListOf<News>()
        val newsArray = mainObject.getJSONArray("news")
        newsArray.forEach { newsObject ->
            val newsItem = Gson().fromJson(newsObject.toString(), News::class.java)
            newsItems.add(newsItem)
        }
        return newsItems
    }

    private fun JSONArray.forEach(jsonObject: (JSONObject) -> Unit) {
        for (index in 0 until this.length()) jsonObject(this[index] as JSONObject)
    }
}