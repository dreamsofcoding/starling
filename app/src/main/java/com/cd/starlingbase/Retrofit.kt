package com.cd.starlingbase

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Base Url
private const val BASE_URL = "https://api-sandbox.starlingbank.com/api/v2/"
//Access Token
private val BEARER_TOKEN = "Bearer "


class Retrofit (private val okHttpClient: OkHttpClient) {

    fun createStarlingService(): StarlingService {
        val client = okHttpClient.newBuilder()
            .addInterceptor(StarlingTokenInterceptor())
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build().create(StarlingService::class.java)
    }
}

private class StarlingTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url()
        val url = originalUrl.newBuilder().build()

        val requestBuilder = original.newBuilder()
            .url(url)
            .header("Authorization", BEARER_TOKEN)
            .header("User-Agent", "Starling Base")

        return chain.proceed(requestBuilder.build())
    }
}

interface StarlingService {


}