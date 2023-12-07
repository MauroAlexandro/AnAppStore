package com.mauroalexandro.anappstore.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASEURL = "http://ws2.aptoide.com/api/6/bulkRequest/api_list/"

class ApiClient {
    private lateinit var retrofit: Retrofit

    companion object {
        private var INSTANCE: ApiClient? = null
        fun getInstance(): ApiClient {
            if (INSTANCE == null) {
                INSTANCE = ApiClient()
            }
            return INSTANCE as ApiClient
        }
    }

    fun getClient(): ApiEndpointInterface? {
        if (!this::retrofit.isInitialized) {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        return retrofit.create(ApiEndpointInterface::class.java)
    }
}