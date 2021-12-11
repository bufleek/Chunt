package com.sports.crichunt.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R

class PagerStateAdapter(
    private val reload: () -> Unit
) : LoadStateAdapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, loadState: LoadState) {
        if (loadState is LoadState.Error) {
            (holder as ViewHolderError).bind(loadState.error.message ?: "Request failed")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (loadState) {
            is LoadState.NotLoading -> ViewHolder(
                layoutInflater.inflate(
                    R.layout.item_empty,
                    parent,
                    false
                )
            )
            LoadState.Loading -> ViewHolder(
                layoutInflater.inflate(
                    R.layout.item_loading_section,
                    parent,
                    false
                )
            )
            is LoadState.Error -> ViewHolderError(
                layoutInflater.inflate(
                    R.layout.item_error_section,
                    parent,
                    false
                )
            ) { reload() }
        }
    }
}