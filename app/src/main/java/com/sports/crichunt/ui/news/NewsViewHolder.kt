package com.sports.crichunt.ui.news

import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.data.models.Article
import com.sports.crichunt.databinding.BindingItemArticle

class NewsViewHolder(
    private val binding: BindingItemArticle,
    private val onArticleClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        itemView.setOnClickListener {
            onArticleClicked(bindingAdapterPosition)
        }
    }

    fun bind(article: Article) {
        binding.article = article
    }
}