package com.sports.crichunt.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sports.crichunt.R

class ViewHolderTitle(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

    fun bind(title: String) {
        tvTitle.text = title
    }
}