package com.onefootball.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.onefootball.R
import com.onefootball.domain.model.News
import okhttp3.HttpUrl

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsItems : List<News> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount() = newsItems.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsItems[position]

        holder.titleView.text = news.title
        holder.newsView.load(url = HttpUrl.parse(news.image_url))
        holder.resourceImage.load(url = HttpUrl.parse(news.resource_url))
        holder.resourceName.text = news.resource_name
        holder.itemView.setOnClickListener {
            Navigator(it.context).openNews(news)
        }
    }

    fun setNewsItems(newListOfNewsItems: List<News>) {
        val oldList = newsItems
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            NewsItemDiffCallback(oldList, newListOfNewsItems)
        )

        newsItems = newListOfNewsItems
        diffResult.dispatchUpdatesTo(this)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleView: TextView = itemView.findViewById(R.id.news_title)
        var newsView: ImageView = itemView.findViewById(R.id.news_view)
        var resourceImage: ImageView = itemView.findViewById(R.id.resource_icon)
        var resourceName: TextView = itemView.findViewById(R.id.resource_name)
    }
}
