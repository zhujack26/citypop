package com.example.citypop

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import java.io.FileInputStream
import java.util.Properties


class SpotifyViewModel(private val context: Context) : ViewModel() {
    private val spotifyService = SpotifyService.create()
    private val spotifyRepository = SpotifyRepository(spotifyService)

    private val clientId: String
    private val clientSecret: String
    init {
        val properties = Properties().apply {
            context.assets.open("secrets.properties").use { inputStream -> load(inputStream) }
        }
        clientId = properties.getProperty("spotifyClientId")
        clientSecret = properties.getProperty("spotifyClientSecret")
    }

    var albumDetails by mutableStateOf<AlbumDetails?>(null)

    fun getAlbumDetails(albumId: String) {
        //코루틴 실행
        viewModelScope.launch {
            val accessToken = getAccessToken()
            withContext(Dispatchers.IO) {
                //액세스 토큰은 withContext 블록 안에서 얻기
                //withContext 블록은 코루틴 스케줄러를 전환, 다른 스레드에서 코드를 실행할 수 있는 기능 제공
                // Dispatchers.IO 스케줄러를 사용하여 I/O 작업을 수행하는 별도의 스레드에서 코드를 실행
                spotifyRepository.getAlbumDetails(albumId, accessToken) { details ->
                    this@SpotifyViewModel.albumDetails = details
                }
            }
        }
    }
    private suspend fun getAccessToken(): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = spotifyService.getAccessToken(
                    grantType = "client_credentials",
                    clientId = clientId,
                    clientSecret = clientSecret
                )
                response.accessToken.also { Log.d("SpotifyViewModel", "Access Token: $it") }
            } catch (e: Exception) {
                Log.e("SpotifyViewModel", "Failed to get access token: $e")
                throw e
            }
        }
    }
}