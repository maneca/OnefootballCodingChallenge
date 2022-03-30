package com.onefootball.domain.repository

import com.onefootball.data.local.NewsDao
import com.onefootball.data.remote.NewsApi
import com.onefootball.domain.model.News
import com.onefootball.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(): Flow<Resource<List<News>>>
}