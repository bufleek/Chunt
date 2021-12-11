package com.sports.crichunt.data.repos

import com.sports.crichunt.data.api.ApiEndpoints
import com.sports.crichunt.data.api.ApiService

open class BaseRepo {
    var apiService = ApiService.buildService(ApiEndpoints::class.java)
    var newsService = ApiService.buildNewsService(ApiEndpoints::class.java)
}