package lw.pko.pkochallenge.movie.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import lw.pko.pkochallenge.movie.domain.persistence.FavouriteMoviesRepository
import lw.pko.pkochallenge.movie.domain.usecases.GetNowPlayingMoviesUseCase
import lw.pko.pkochallenge.movie.domain.usecases.SearchMoviesUseCase
import lw.pko.pkochallenge.movie.presentation.dashboard.MovieItemViewEntity
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val favouriteMoviesRepository: FavouriteMoviesRepository
) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _errorFlow.emit(throwable.message ?: "Unknown error")
        }
    }

    private val _navigateBackEvent: MutableSharedFlow<Unit> = MutableSharedFlow()
    val navigateBackEvent = _navigateBackEvent.asSharedFlow()

    private var isFavouriteState by mutableStateOf(false)

    private var movieItem = MovieItemViewEntity.EMPTY

    private val _movieItemState = snapshotFlow { isFavouriteState }
        .map { movieItem.copy(isFavourite = isFavouriteState) }

    val movieItemState = _movieItemState.stateIn(viewModelScope, started = SharingStarted.Lazily, movieItem)

    fun setMovieItem(movieItem: MovieItemViewEntity) {
        this.movieItem = movieItem
        isFavouriteState = movieItem.isFavourite
    }

    fun handleMovieDetailsUiEvent(event: MovieDetailsUiEvent) {
        when (event) {
            MovieDetailsUiEvent.NavigateBack -> navigateBack()
            is MovieDetailsUiEvent.ToggleFavourite -> toggleFavourite(event.id, event.isFavourite)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _navigateBackEvent.emit(Unit)
        }
    }

    private fun toggleFavourite(id: Int, isFavourite: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            if (isFavourite) {
                favouriteMoviesRepository.remove(id)
            } else {
                favouriteMoviesRepository.add(id)
            }
            isFavouriteState = !isFavourite
        }
    }

}

sealed interface MovieDetailsUiEvent {
    data object NavigateBack : MovieDetailsUiEvent
    class ToggleFavourite(val id: Int, val isFavourite: Boolean) : MovieDetailsUiEvent
}