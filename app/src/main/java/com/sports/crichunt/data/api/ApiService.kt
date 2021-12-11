package com.sports.crichunt.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object ApiService {
    private const val BASE_URL = "https://cricket.sportmonks.com"
    val apiToken = "H0exq7wcbyyNS6T6yDnHi9miphBT0l7VcZfVTLNHwjgZ3dzr17NZu3xAfNI3"
    private const val NEWS_BASE_URL = "https://hindi.news18.com"

    fun <T> buildService(service: Class<T>): T {
        val client = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(service)
    }

    fun <T> buildNewsService(service: Class<T>): T {
        val client = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .client(client)
            .addConverterFactory((SimpleXmlConverterFactory.create()))
            .build()
        return retrofit.create(service)
    }
}