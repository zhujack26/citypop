package com.example.citypop

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyService {
    @GET("/v1/albums/{id}")
    fun getAlbum(
        @Path("id") albumId: String,
        @Header("Authorization") authHeader: String?
    ): Call<AlbumDetails>
    @FormUrlEncoded
    @POST("https://accounts.spotify.com/api/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): AccessTokenResponse

    companion object {
        private const val baseUrl = "https://api.spotify.com/v1/"

        fun create(): SpotifyService {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(SpotifyService::class.java)
        }
    }
}