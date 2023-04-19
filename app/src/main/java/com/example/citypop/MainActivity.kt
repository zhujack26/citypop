package com.example.citypop

import android.os.Bundle
import android.os.Build.VERSION.SDK_INT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable


val customColor = Color(0xFF002355)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel = SpotifyViewModel()
                MusicApp(viewModel)
            }
        }
    }
}

@Composable
fun MusicApp(viewModel: SpotifyViewModel) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("city pop") },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(painter = painterResource(id = R.drawable.navigation_icon), contentDescription = "Menu")

                    }
                }
            )
        },
        drawerContent = {
            Column {
                DrawerButton(
                    text = "홈",
                    onClick = {
                        navController.navigate("home")
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
                DrawerButton(
                    text = "♥ 좋아요 한 음악",
                    onClick = {
                        navController.navigate("gallery")
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
                DrawerButton(
                    text = "미정",
                    onClick = {
                        navController.navigate("slideshow")
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        NavHost(navController, startDestination = "home") {
            composable("home") { HomeScreen(navController, viewModel) }
            composable("gallery") { GalleryScreen(navController, viewModel) }
            composable("slideshow") { SlideshowScreen() }
            composable("detail/{imageResourceId}") { backStackEntry ->
                val imageResourceId =
                    backStackEntry.arguments?.getInt("imageResourceId") ?: return@composable
                DetailScreen(
                    imageResourceId = imageResourceId,
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel)
            }
        }
    }
}
@Composable
fun DrawerButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text, fontSize = 24.sp)
    }
}

@Composable
fun HomeScreen(navController: NavHostController, viewModel: SpotifyViewModel) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val gifPainter = rememberAsyncImagePainter(R.drawable.main, imageLoader)

    Surface(color = customColor) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = gifPainter,
                contentDescription = stringResource(R.string.main_gif_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            GalleryContent(navController, viewModel)
        }
    }
}

@Composable
fun GalleryContent(navController: NavHostController, viewModel: SpotifyViewModel) {
    val yourListOfImages = listOf(
        R.drawable.image2,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5,
        R.drawable.image6
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = yourListOfImages.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { image ->
                    ImageThumbnail(imageResourceId = image) {
                        navController.navigate("detail/${image}")
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryScreen(navController: NavHostController, viewModel: SpotifyViewModel) {
    Surface(color = customColor) {
        GalleryContent(navController, viewModel)
    }
}

@Composable
fun ImageThumbnail(imageResourceId: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = imageResourceId),
        contentDescription = "Music Thumbnail",
        modifier = Modifier
            .size(156.dp)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SlideshowScreen() {
}