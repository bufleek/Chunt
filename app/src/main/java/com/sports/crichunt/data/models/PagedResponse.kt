package com.sports.crichunt.data.models

data class PagedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: ArrayList<T>
)
