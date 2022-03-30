package com.onefootball.viewModel

import com.onefootball.domain.model.News
import com.onefootball.util.CustomExceptions
import java.io.IOException
import java.lang.Exception

data class NewsState(
    val newsItems: List<News> = emptyList(),
    val exception: CustomExceptions? = null,
    val loading: Boolean = false
)