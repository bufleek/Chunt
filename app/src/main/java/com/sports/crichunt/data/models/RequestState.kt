package com.sports.crichunt.data.models

sealed class RequestState{
    object Loading: RequestState()
    data class Error(val error: String): RequestState()
    data class Success<T>(val result: T): RequestState()
}
