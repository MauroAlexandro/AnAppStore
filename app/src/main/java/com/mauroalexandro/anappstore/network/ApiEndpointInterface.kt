package com.mauroalexandro.anappstore.network

import com.mauroalexandro.anappstore.models.AppList
import retrofit2.Call
import retrofit2.http.GET

interface ApiEndpointInterface {
    @GET("listApps")
    fun getApps(): Call<AppList>
}