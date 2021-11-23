package com.sports.crichunt.utils

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R

class ViewHolderError(
    itemView: View,
    private val reload: () -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val tvError: TextView = itemView.findViewById(R.id.tv_error)
    private val btnReload: Button = itemView.findViewById(R.id.btn_reload)

    init {
        btnReload.setOnClickListener { reload() }
    }

    fun bind(error: String) {
        tvError.text = error
    }
}