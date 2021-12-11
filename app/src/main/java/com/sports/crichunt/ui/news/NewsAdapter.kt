package com.sports.crichunt.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Article
import com.sports.crichunt.data.models.Feed
import com.sports.crichunt.databinding.BindingItemArticle

class NewsAdapter(
    private val onArticleClicked: (Article) -> Unit
) : RecyclerView.Adapter<NewsViewHolder>() {
    private val articles = ArrayList<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            BindingItemArticle.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            onArticleClicked(articles[it])
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    fun updateData(feed: Feed) {
        this.articles.clear()
        this.articles.addAll(feed.articleList ?: ArrayList())
        notifyDataSetChanged()
    }
}