package com.onefootball.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onefootball.domain.repository.NewsRepository
import com.onefootball.util.CustomExceptions
import com.onefootball.util.DispatcherProvider
import com.onefootball.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val repository: NewsRepository
): ViewModel() {

    private val _state = MutableStateFlow(NewsState())
    val state = _state.asStateFlow()

    init {
        getNews()
    }

    private fun getNews(){
        viewModelScope.launch {
            repository.getNews()
                .onEach { result ->
                    when(result){
                        is Resource.Success ->{
                            _state.value = state.value.copy(
                                newsItems = result.data ?: emptyList(),
                                exception = null,
                                loading = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                newsItems = result.data ?: emptyList(),
                                exception = result.exception ?: CustomExceptions.UnknownException,
                                loading = false
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                newsItems = result.data ?: emptyList(),
                                exception = null,
                                loading = true
                            )
                        }
                    }
                }
                .flowOn(dispatcher.io())
                .launchIn(this)
        }
    }
}