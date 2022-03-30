package com.onefootball

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.onefootball.databinding.ActivityMainBinding
import com.onefootball.ui.NewsAdapter
import com.onefootball.util.CustomExceptions
import com.onefootball.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyNewsActivity : AppCompatActivity() {
    private val viewModel by viewModels<NewsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val myAdapter = NewsAdapter()
        binding.newsRecyclerView.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(this@MyNewsActivity)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collectLatest { newsState ->

                    if(newsState.exception != null){
                        val message = when(newsState.exception){
                            is CustomExceptions.UnableToReadFile -> getString(R.string.unable_to_read)
                            else -> getString(R.string.something_went_wrong)
                        }

                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                    }else{
                        binding.progressBar.visibility = if (newsState.loading) View.VISIBLE else View.GONE
                        myAdapter.setNewsItems(newsState.newsItems)
                    }
                }
            }
        }
    }
}
