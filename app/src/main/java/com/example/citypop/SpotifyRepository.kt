package com.example.citypop

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class SpotifyRepository(private val spotifyService: SpotifyService) {
    fun getAlbumDetails(albumId: String, accessToken: String, onResult: (AlbumDetails?) -> Unit) {
        spotifyService.getAlbum(albumId, "Bearer $accessToken")
            .enqueue(object : Callback<AlbumDetails> {
                override fun onResponse(call: Call<AlbumDetails>, response: Response<AlbumDetails>) {
                    if (response.isSuccessful) {
                        val albumDetails = response.body()
                        onResult(albumDetails)
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<AlbumDetails>, t: Throwable) {
                    onResult(null)
                }
            })
    }
}