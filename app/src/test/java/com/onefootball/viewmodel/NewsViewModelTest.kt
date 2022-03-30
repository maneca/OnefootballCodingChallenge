package com.onefootball.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.onefootball.domain.model.News
import com.onefootball.domain.repository.NewsRepository
import com.onefootball.util.CustomExceptions
import com.onefootball.util.DispatcherProvider
import com.onefootball.util.Resource
import com.onefootball.utils.MainCoroutineRule
import com.onefootball.viewModel.NewsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NewsViewModelTest {

    companion object {
        val newsA = News(
            id = 0,
            title = "How will Juventus line up this season?",
            image_url =  "https://oneftbl-cms.imgix.net/https%3A%2F%2Fwp-images.onefootball.com%2Fwp-content%2Fuploads%2Fsites%2F10%2F2019%2F08%2FFBL-ICC-2019-MADRID-JUVENTUS-1566054314-1024x768.jpg?crop=faces&fit=crop&h=810&q=25&w=1080&s=eecf0b15917c1c4af6bc16ecb63ccc1e",
            resource_name = "Onefootball",
            resource_url =  "https://images.onefootball.com/blogs_logos/circle_onefootball.png",
            news_link =  "https://onefootball.com/en/news/how-will-juventus-line-up-this-season-en-26881454?variable=20190822")
    }

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var mockRepo: NewsRepository

    private lateinit var viewModel: NewsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testDispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = testDispatcher
        override fun io(): CoroutineDispatcher = testDispatcher
        override fun main(): CoroutineDispatcher = testDispatcher
        override fun unconfined(): CoroutineDispatcher = testDispatcher
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get news, successful`() = runTest (testDispatcher){

        Truth.assertThat(mockRepo).isNotNull()
        coEvery { mockRepo.getNews() } returns flowOf(Resource.Success(listOf(newsA)))

        viewModel = NewsViewModel(testDispatcherProvider, mockRepo)
        Truth.assertThat(viewModel.state.value.newsItems).isEqualTo(listOf(newsA))
        Truth.assertThat(viewModel.state.value.exception).isNull()
        Truth.assertThat(viewModel.state.value.loading).isFalse()
    }

    @Test
    fun `get news, failed`() = runTest (testDispatcher){

        Truth.assertThat(mockRepo).isNotNull()
        coEvery { mockRepo.getNews() } returns flowOf(Resource.Error( exception = CustomExceptions.UnableToReadFile,
            data = listOf()))

        viewModel = NewsViewModel(testDispatcherProvider, mockRepo)
        Truth.assertThat(viewModel.state.value.newsItems).isEqualTo(listOf<News>())
        Truth.assertThat(viewModel.state.value.exception).isEqualTo(CustomExceptions.UnableToReadFile)
        Truth.assertThat(viewModel.state.value.loading).isFalse()
    }

    @Test
    fun `get news, loading`() = runTest (testDispatcher){

        Truth.assertThat(mockRepo).isNotNull()
        coEvery { mockRepo.getNews() } returns flowOf(Resource.Loading(listOf(newsA)))

        viewModel = NewsViewModel(testDispatcherProvider, mockRepo)
        Truth.assertThat(viewModel.state.value.newsItems).isEqualTo(listOf(newsA))
        Truth.assertThat(viewModel.state.value.exception).isNull()
        Truth.assertThat(viewModel.state.value.loading).isTrue()
    }

}