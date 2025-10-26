package com.proyectomovil.APIs.AnimalesAPI

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ApiAnimalesClient {

    // URL base de IUCN Red List API
    private const val BASE_URL = "https://apiv3.iucnredlist.org/api/v3/"

    // TOKEN DE IUCN
    const val API_TOKEN = "FdaNZNFqjk4oWgaHNQ3M7PnWZfonY4YndK8A"

    // Interceptor para logs
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 🔑 INTERCEPTOR PARA AGREGAR TOKEN A TODAS LAS URLs
    private val tokenInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Agregar el token como query parameter
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("token", API_TOKEN)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    // Cliente HTTP con interceptors
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