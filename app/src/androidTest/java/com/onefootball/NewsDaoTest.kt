package com.onefootball

import androidx.room.Room
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.onefootball.data.local.NewsDao
import com.onefootball.data.local.NewsDatabase
import com.onefootball.data.local.entity.NewsEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@SmallTest
class NewsDaoTest {

    private lateinit var database: NewsDatabase
    private lateinit var dao: NewsDao

    companion object {
        val newsA = NewsEntity(
            title = "The 5 players who could be the next Messi or Ronaldo",
            image_url = "https://image-service.onefootball.com/crop/face?h=810&amp;image=https%3A%2F%2Fwp-images.onefootball.com%2Fwp-content%2Fuploads%2Fsites%2F10%2F2019%2F08%2FFIFA-Ballon-dOr-Gala-2014-1566312341-1024x683.jpg&amp;q=25&amp;w=1080",
            resource_name = "Onefootball",
            resource_url = "https://images.onefootball.com/blogs_logos/circle_onefootball.png",
            news_link = "https://onefootball.com/en/news/the-5-players-who-could-be-the-next-messi-or-ronaldo-en-26880141?variable=20190822")

        val newsB = NewsEntity(
            title = "How will Juventus line up this season?",
            image_url =  "https://oneftbl-cms.imgix.net/https%3A%2F%2Fwp-images.onefootball.com%2Fwp-content%2Fuploads%2Fsites%2F10%2F2019%2F08%2FFBL-ICC-2019-MADRID-JUVENTUS-1566054314-1024x768.jpg?crop=faces&fit=crop&h=810&q=25&w=1080&s=eecf0b15917c1c4af6bc16ecb63ccc1e",
            resource_name = "Onefootball",
            resource_url =  "https://images.onefootball.com/blogs_logos/circle_onefootball.png",
            news_link =  "https://onefootball.com/en/news/how-will-juventus-line-up-this-season-en-26881454?variable=20190822")
    }

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            NewsDatabase::class.java)
            .build()

        dao = database.dao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getNews() = runBlocking{
        dao.insertNews(listOf(newsA, newsB))

        val news = dao.getNews()
        Assert.assertEquals(2, news.size)
        Assert.assertEquals(newsA.title, news[0].title)
        Assert.assertEquals(newsB.title, news[1].title)
    }

    @Test
    fun deleteNews() = runBlocking{
        dao.insertNews(listOf(newsA, newsB))

        var news = dao.getNews()
        Assert.assertEquals(2, news.size)

        dao.deleteNews()
        news = dao.getNews()

        Assert.assertEquals(0, news.size)
    }

}