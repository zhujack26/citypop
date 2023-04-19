package com.example.citypop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter


@Composable
fun DetailScreen(imageResourceId: Int, onBack: () -> Unit, viewModel: SpotifyViewModel) {
    val albumId = "4aawyAB9vmqN3uQ7FjRGTy"
    if (viewModel.albumDetails == null) {
        viewModel.getAlbumDetails(albumId)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.albumDetails?.let { details ->
            Image(
                painter = rememberImagePainter(data = details.images.first().url),
                contentDescription = "Album Image",
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Crop
            )
            Text("Album Name: ${details.name}", fontSize = 24.sp)
            Text("Album Type: ${details.albumType}", fontSize = 24.sp)
            Text("Release Date: ${details.releaseDate}", fontSize = 24.sp)
        } ?: Text("Loading album details...", fontSize = 24.sp)
    }
}