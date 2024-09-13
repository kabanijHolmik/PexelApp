package com.example.pexelsapp.presentation.ui
import android.app.Application
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.example.pexelsapp.R
import com.example.pexelsapp.data.PhotoRepositoryImpl


@Composable
fun MainScreen() {

    val application = LocalContext.current.applicationContext as Application


    val photoRepository = PhotoRepositoryImpl(application)


    val viewModelFactory = MainViewModel.MainViewModelFactory(photoRepository)


    val viewModel: MainViewModel = remember {
        ViewModelProvider(ViewModelStore(), viewModelFactory)
            .get(MainViewModel::class.java)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchPopularPhotos(perPage = 1)
    }

    Scaffold(
        bottomBar = { CustomBottomBar(viewModel = viewModel) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CustomSearchBar(viewModel = viewModel)
            Spacer(modifier = Modifier.height(32.dp))
            ButtonCarousel(viewModel = viewModel)
            Spacer(modifier = Modifier.height(24.dp))
            CustomLazyVerticalStaggeredGrid(viewModel = viewModel)
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(viewModel: MainViewModel) {
    val searchQuery by viewModel.searchQuery

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 12.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = { /* Handle search action */ },
            active = false,
            onActiveChange = {},
            placeholder = { Text("Search", color = Color(0xFF868686)) },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_search_24),
                    contentDescription = "Search Icon",
                    tint = Color(0xFFB00020)
                )
            },
            modifier = Modifier.height(50.dp),
            colors = SearchBarDefaults.colors(
                containerColor = Color(0xFFF5F5F5)
            ),
            content = {}
        )
    }
}

@Composable
fun ButtonCarousel(viewModel: MainViewModel) {
    val selectedCategory by viewModel.selectedCategory

    LazyRow(
        modifier = Modifier.padding(start = 24.dp)
    ) {
        val categories = listOf("Ice", "Watches", "Drawing", "Brick Wall", "Waterfall", "Fire", "House")

        items(categories) { category ->
            Button(
                onClick = { viewModel.selectCategory(category) },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (category == selectedCategory) Color(0xFFB00020) else Color(0xFFF5F5F5),
                    contentColor = if (category == selectedCategory) Color.White else Color.Black
                ),
                modifier = Modifier
                    .height(39.dp)
                    .padding(end = 10.dp)
            ) {
                Text(category)
            }
        }
    }
}

@Composable
fun CustomBottomBar(viewModel: MainViewModel) {
    val selectedItem by viewModel.selectedBottomItem

    Surface(
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                iconRes = R.drawable.baseline_home_filled_24,
                isSelected = selectedItem == 0,
                onClick = { viewModel.selectBottomItem(0) }
            )
            BottomNavigationItem(
                iconRes = R.drawable.baseline_bookmark_border_24,
                isSelected = selectedItem == 1,
                onClick = { viewModel.selectBottomItem(1) }
            )
        }
    }
}

@Composable
fun BottomNavigationItem(
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = if (isSelected) Color(0xFFB00020) else Color.Gray
        )
    }
}

@Composable
fun CustomLazyVerticalStaggeredGrid(viewModel: MainViewModel) {
    val popularPhotos by viewModel.popularPhotos
    val isLoading = popularPhotos.isEmpty()


    val context = LocalContext.current

    val imageLoader = remember {
        createCustomImageLoader(context)
    }

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 17.dp,
        horizontalArrangement = Arrangement.spacedBy(17.dp),
        content = {
            if (isLoading) {

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {

                items(popularPhotos) { photo ->
                    Card(shape = RoundedCornerShape(20.dp)) {
                        AsyncImage(
                            model = photo.src.original,
                            imageLoader = imageLoader,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    }
                }
            }
        },
    )
}

fun createCustomImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
}