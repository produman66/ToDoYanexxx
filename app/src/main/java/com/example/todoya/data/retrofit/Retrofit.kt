package com.example.todoya.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Singleton Retrofit instance for accessing Todo API.
 */
object RetrofitInstance {

    private const val BASE_URL = "https://hive.mrdekk.ru"

    val api: TodoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApiService::class.java)
    }
}