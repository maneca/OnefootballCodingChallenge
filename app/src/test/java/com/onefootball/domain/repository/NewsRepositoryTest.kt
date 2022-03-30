package com.onefootball.domain.repository

import com.google.common.truth.Truth
import com.onefootball.data.local.NewsDao
import com.onefootball.data.local.entity.NewsEntity
import com.onefootball.data.remote.NewsApi
import com.onefootball.data.repository.NewsRepositoryImp
import com.onefootball.domain.model.News
import com.onefootball.util.CustomExceptions
import com.onefootball.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NewsRepositoryTest {
    @MockK
    private lateinit var mockApi: NewsApi

    @MockK(relaxUnitFun = true)
    private lateinit var mockDao: NewsDao

    private lateinit var repository: NewsRepository

    companion object {
        val newsA = NewsEntity(
            id = 0,
            title = "How will Juventus line up this season?",
            image_url =  "https://oneftbl-cms.imgix.net/https%3A%2F%2Fwp-images.onefootball.com%2Fwp-content%2Fuploads%2Fsites%2F10%2F2019%2F08%2FFBL-ICC-2019-MADRID-JUVENTUS-1566054314-1024x768.jpg?crop=faces&fit=crop&h=810&q=25&w=1080&s=eecf0b15917c1c4af6bc16ecb63ccc1e",
            resource_name = "Onefootball",
            resource_url =  "https://images.onefootball.com/blogs_logos/circle_onefootball.png",
            news_link =  "https://onefootball.com/en/news/how-will-juventus-line-up-this-season-en-26881454?variable=20190822")
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = NewsRepositoryImp(mockApi, mockDao)
    }

    @Test
    fun getBreedsFromNetwork() = runBlocking {
        Assert.assertNotNull(mockDao)
        Assert.assertNotNull(mockApi)

        val apiResponse = "{\n" +
                "  \"news\": [\n" +
                "    {\n" +
                "      \"title\": \"The 5 players who could be the next Messi or Ronaldo\",\n" +
                "      \"image_url\": \"https://image-service.onefootball.com/crop/face?h=810&amp;image=https%3A%2F%2Fwp-images.onefootball.com%2Fwp-content%2Fuploads%2Fsites%2F10%2F2019%2F08%2FFIFA-Ballon-dOr-Gala-2014-1566312341-1024x683.jpg&amp;q=25&amp;w=1080\",\n" +
                "      \"resource_name\": \"Onefootball\",\n" +
                "      \"resource_url\": \"https://images.onefootball.com/blogs_logos/circle_onefootball.png\",\n" +
                "      \"news_link\": \"https://onefootball.com/en/news/the-5-players-who-could-be-the-next-messi-or-ronaldo-en-26880141?variable=20190822\"\n" +
                "    },\n" +
                "  ]\n" +
                "}"

        coEvery { mockDao.getNews() } coAnswers { ArrayList() }
        coEvery { mockApi.readFile("news.json") } coAnswers { apiResponse }

        val list: ArrayList<Resource<List<News>>> = ArrayList()
        repository.getNews().collect {
            list.add(it)
        }

        Truth.assertThat(list.size).isEqualTo(3)
        Truth.assertThat(list[0] is Resource.Loading).isTrue()
        Truth.assertThat(list[1] is Resource.Loading).isTrue()
        Truth.assertThat((list[2] as Resource.Success).data).isEqualTo(listOf<News>())
    }

    @Test
    fun getBreedsFromLocalDb() = runBlocking {
        Assert.assertNotNull(mockDao)
        Assert.assertNotNull(mockApi)

        coEvery { mockDao.getNews() } coAnswers { listOf(newsA) }
        coEvery { mockApi.readFile("news.json") } coAnswers { "{ news: [] }" }

        val list: ArrayList<Resource<List<News>>> = ArrayList()
        repository.getNews().collect {
            list.add(it)
        }

        Truth.assertThat(list.size).isEqualTo(3)
        Truth.assertThat(list[0] is Resource.Loading).isTrue()
        Truth.assertThat((list[1] as Resource.Loading).data?.size).isEqualTo(1)
        Truth.assertThat((list[2] as Resource.Success).data?.size).isEqualTo(1)
    }

    @Test
    fun getBreedsReturnsExceptions() = runBlocking {
        Assert.assertNotNull(mockDao)
        Assert.assertNotNull(mockApi)

        coEvery { mockDao.getNews() } coAnswers { listOf() }
        coEvery { mockApi.readFile("news.json") } throws IOException()

        val list: ArrayList<Resource<List<News>>> = ArrayList()
        repository.getNews().collect {
            list.add(it)
        }

        Truth.assertThat(list.size).isEqualTo(3)
        Truth.assertThat(list[0] is Resource.Loading).isTrue()
        Truth.assertThat((list[1] as Resource.Loading).data?.size).isEqualTo(0)
        Truth.assertThat((list[2] as Resource.Error).exception).isEqualTo(CustomExceptions.UnableToReadFile)
    }
}