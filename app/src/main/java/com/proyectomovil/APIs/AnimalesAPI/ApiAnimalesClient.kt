package com.proyectomovil.APIs.AnimalesAPI

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ApiAnimalesClient {
    private const val BASE_URL = "https://apiv3.iucnredlist.org/api/v3/"
    const val API_TOKEN = "FdaNZNFqjk4oWgaHNQ3M7PnWZfonY4YndK8A"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val tokenInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("token", API_TOKEN)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val http = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(tokenInterceptor)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: ApiAnimalesService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(http)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiAnimalesService::class.java)
    }
}