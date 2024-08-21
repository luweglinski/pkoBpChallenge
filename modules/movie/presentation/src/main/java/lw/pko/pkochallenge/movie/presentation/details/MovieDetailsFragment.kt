package lw.pko.pkochallenge.movie.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import lw.pko.pkochallenge.core.theme.PKOChallengeTheme
import lw.pko.pkochallenge.core.theme.utils.toPx
import lw.pko.pkochallenge.movie.presentation.R
import lw.pko.pkochallenge.movie.presentation.dashboard.MovieItemViewEntity

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PKOChallengeTheme {
                    LaunchedEffect(true) {
                        viewModel.setMovieItem(args.movieItem)
                    }

                    val movieItemState by viewModel.movieItemState.collectAsStateWithLifecycle()

                    MovieDetailsScreen(
                        movie = movieItemState,
                        onUiEvent = remember(viewModel) { { viewModel.handleMovieDetailsUiEvent(it) } }
                    )
                }

                ObserveEffects()
            }
        }
    }

    @Composable
    private fun ObserveEffects() {
        val context = LocalContext.current
        LaunchedEffect(viewModel.navigateBackEvent) {
            viewModel.navigateBackEvent.collectLatest {
                findNavController().navigateUp()
            }
        }
        LaunchedEffect(viewModel.errorFlow) {
            viewModel.errorFlow.collectLatest { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MovieDetailsScreen(
        movie: MovieItemViewEntity,
        onUiEvent: (MovieDetailsUiEvent) -> Unit
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = movie.title, style = MaterialTheme.typography.titleMedium) },
                    navigationIcon = {
                        ToolbarButton(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            onClick = { onUiEvent(MovieDetailsUiEvent.NavigateBack) }
                        )
                    },
                    actions = {
                        ToolbarButton(
                            imageVector = if (movie.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            onClick = { onUiEvent(MovieDetailsUiEvent.ToggleFavourite(movie.id, movie.isFavourite)) }
                        )
                    }
                )
            },
        ) { contentPadding ->
            MovieDetailsContent(movie, Modifier.padding(contentPadding))
        }
    }

    @Composable
    private fun MovieDetailsContent(movie: MovieItemViewEntity, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
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
            Text(text = stringResource(R.string.release_date, movie.releaseDate))
            Text(text = stringResource(R.string.rate, movie.voteAverage))
            Text(text = movie.overview)
        }
    }

    @Composable
    fun ToolbarButton(imageVector: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier, contentDescription: String? = null) {
        IconButton(onClick = { onClick.invoke() }) {
            Icon(
                imageVector = imageVector,
                modifier = modifier,
                contentDescription = contentDescription ?: "",
            )
        }
    }
}