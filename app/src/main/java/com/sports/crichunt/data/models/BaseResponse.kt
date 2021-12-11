package com.sports.crichunt.data.models

data class BaseResponse<T>(
    val message: Message,
    val data: T
)
