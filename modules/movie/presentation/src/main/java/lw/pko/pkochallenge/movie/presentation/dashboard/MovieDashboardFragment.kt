package lw.pko.pkochallenge.movie.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import lw.pko.pkochallenge.core.theme.AutoCompleteTextField
import lw.pko.pkochallenge.core.theme.PKOChallengeTheme
import lw.pko.pkochallenge.core.theme.utils.toPx
import lw.pko.pkochallenge.movie.presentation.R

@AndroidEntryPoint
class MovieDashboardFragment : Fragment() {

    private val viewModel by viewModels<MovieDashboardViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PKOChallengeTheme {
                    PKOChallengeTheme {
                        val movies = viewModel.movies.collectAsLazyPagingItems()

                        SearchScreenSearchBarContent(
                            searchQuery = viewModel.searchQuery,
                            showAutoComplete = viewModel.showAutoComplete,
                            imagePagingResult = movies,
                            onUiEvent = remember(viewModel) { { viewModel.handleMovieDashboardUiEvent(it) } }
                        )
                    }

                    ObserveEffects()
                }

            }
        }
    }

    @Composable
    private fun ObserveEffects() {
        val context = LocalContext.current
        LaunchedEffect( viewModel.errorFlow) {
            viewModel.errorFlow.collectLatest { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
        LaunchedEffect(  viewModel.navigateToDetails) {
            viewModel.navigateToDetails.collectLatest { movieItemViewEntity ->
                findNavController().navigate(
                    MovieDashboardFragmentDirections
                        .actionMovieDashboardFragmentToMovieDetailFragment(movieItemViewEntity)
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SearchScreenSearchBarContent(
        searchQuery: String,
        showAutoComplete: Boolean,
        imagePagingResult: LazyPagingItems<MovieItemViewEntity>,
        onUiEvent: (MovieDashboardUiEvent) -> Unit
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { onUiEvent(MovieDashboardUiEvent.Search(it)) },
            onSearch = {},
            placeholder = {
                Text(text = stringResource(R.string.search_movie))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                val rotationState by animateFloatAsState(
                    targetValue = if (showAutoComplete) 180f else 0f
                )
                IconButton(
                    modifier = Modifier.rotate(rotationState),
                    onClick = { onUiEvent(MovieDashboardUiEvent.ToggleAutoComplete) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            },
            content = {
                MovieList(imagePagingResult, onUiEvent, showAutoComplete) {
                    onUiEvent(MovieDashboardUiEvent.SelectAutoComplete(it))
                }
            },
            active = true,
            onActiveChange = {},
            modifier = Modifier.fillMaxSize()
        )
    }

    @Composable
    private fun MovieList(
        movies: LazyPagingItems<MovieItemViewEntity>,
        onUiEvent: (MovieDashboardUiEvent) -> Unit,
        selectAutoComplete: Boolean,
        onAutoCompleteSelected: (String) -> Unit
    ) {
        Box {
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(25.dp)) {
                items(movies.itemCount) { index ->
                    val movie = movies[index]
                    movie?.let {
                        MovieItem(movie = movie, onUiEvent)
                    }
                }

                movies.apply {
                    when {
                        loadState.append is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem()
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                        loadState.refresh is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem()
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                        loadState.hasError -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .animateItem()
                                ) {
                                    Button(
                                        onClick = { movies.retry() },
                                        modifier = Modifier.align(Alignment.Center)
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            AutoCompleteTextField(
                itemList = movies.itemSnapshotList.items.take(5).map { it.title },
                modifier = Modifier.fillMaxWidth(),
                expanded = selectAutoComplete,
                onAutoCompleteSelected = onAutoCompleteSelected
            )
        }
    }

    @Composable
    private fun LazyItemScope.MovieItem(
        movie: MovieItemViewEntity,
        onUiEvent: (MovieDashboardUiEvent) -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
                .animateItem(),
            elevation = CardDefaults.elevatedCardElevation(),
            onClick = { onUiEvent(MovieDashboardUiEvent.OnMovieClicked(movie)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BoxWithConstraints {
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .crossfade(true)
                            .data(movie.imageUrl)
                            .size(Size(width = maxWidth.toPx().toInt(), Dimension.Undefined))
                            .build()
                    )

                    if (painter.state is AsyncImagePainter.State.Loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .wrapContentSize()
                                    .align(Alignment.Center)
                            )
                        }
                    } else {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Text(
                        text = movie.title,
                        style = typography.bodyLarge,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    ToggleImageButton(
                        isFavourite = movie.isFavourite,
                        onToggle = { onUiEvent(MovieDashboardUiEvent.ToggleFavourite(movie.id, movie.isFavourite)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

    @Composable
    fun ToggleImageButton(
        isFavourite: Boolean,
        onToggle: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val icon = if (isFavourite) Icons.Filled.Star else Icons.Outlined.StarBorder
        val tint = if (isFavourite) Color.Yellow else Color.Gray

        Image(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier
                .size(24.dp)
                .clickable { onToggle() },
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(tint)
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewSearchScreenSearchBarContent() {
        val mockMovies = flowOf(PagingData.empty<MovieItemViewEntity>())
        SearchScreenSearchBarContent(
            searchQuery = "Sample Query",
            showAutoComplete = true,
            imagePagingResult = mockMovies.collectAsLazyPagingItems(),
            onUiEvent = {}
        )
    }
}