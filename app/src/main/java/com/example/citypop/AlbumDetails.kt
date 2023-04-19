package com.example.citypop

import com.google.gson.annotations.SerializedName

data class AlbumDetails(
    @SerializedName("album_type") val albumType: String,
    @SerializedName("images") val images: List<Image>,
    @SerializedName("name") val name: String,
    @SerializedName("release_date") val releaseDate: String
)

data class Image(
    @SerializedName("url") val url: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int
)