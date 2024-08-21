package lw.pko.pkochallenge.movie.presentation.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import lw.pko.pkochallenge.movie.domain.persistence.FavouriteMoviesRepository
import lw.pko.pkochallenge.movie.domain.usecases.GetNowPlayingMoviesUseCase
import lw.pko.pkochallenge.movie.domain.usecases.SearchMoviesUseCase
import javax.inject.Inject

@HiltViewModel
class MovieDashboardViewModel @Inject constructor(
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val movieItemViewEntityMapper: MovieItemViewEntityMapper,
    private val favouriteMoviesRepository: FavouriteMoviesRepository
) : ViewModel() {

    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _errorFlow.emit(throwable.message ?: "Unknown error")
        }
    }

    private val _navigateToDetails = MutableSharedFlow<MovieItemViewEntity>()
    val navigateToDetails = _navigateToDetails.asSharedFlow()

    var searchQuery by mutableStateOf("")
        private set

    var showAutoComplete by mutableStateOf(false)
        private set

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val movies: Flow<PagingData<MovieItemViewEntity>> =
        snapshotFlow { searchQuery }
            .distinctUntilChanged()
            .debounce(300)
            .flatMapLatest { query ->
                if (query.isEmpty()) {
                    getNowPlayingMoviesUseCase.invoke()
                } else {
                    searchMoviesUseCase.invoke(query)
                }
            }
            .mapLatest { pagingData ->
                pagingData.map {
                    val isFavourite = favouriteMoviesRepository.isFavourite(it.id)
                    movieItemViewEntityMapper.map(it, isFavourite)
                }
            }
            .cachedIn(viewModelScope)

    fun handleMovieDashboardUiEvent(event: MovieDashboardUiEvent) {
        when (event) {
            is MovieDashboardUiEvent.Search -> searchQueryChange(event.query)
            is MovieDashboardUiEvent.SelectAutoComplete -> selectAutoComplete(event.query)
            is MovieDashboardUiEvent.Clear -> searchQueryChange("")
            is MovieDashboardUiEvent.OnMovieClicked -> navigateToDetails(event.movie)
            is MovieDashboardUiEvent.ToggleFavourite -> toggleFavourite(event.id, event.isFavourite)
            MovieDashboardUiEvent.ToggleAutoComplete -> toggleAutoComplete()
        }
    }

    private fun toggleAutoComplete() {
        showAutoComplete = !showAutoComplete
    }

    private fun searchQueryChange(query: String) {
        searchQuery = query
        showAutoComplete = query.isNotEmpty()
    }

    private fun navigateToDetails(movie: MovieItemViewEntity) {
        viewModelScope.launch {
            _navigateToDetails.emit(movie)
        }
    }

    private fun selectAutoComplete(query: String) {
        searchQuery = query
        showAutoComplete = false
    }

    private fun toggleFavourite(id: Int, isFavourite: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            favouriteMoviesRepository.toggleFavourite(id, isFavourite)
            getNowPlayingMoviesUseCase.invalidate()
            searchMoviesUseCase.invalidate()
        }
    }

}

sealed interface MovieDashboardUiEvent {
    class OnMovieClicked(val movie: MovieItemViewEntity) : MovieDashboardUiEvent
    class Search(val query: String) : MovieDashboardUiEvent
    class SelectAutoComplete(val query: String) : MovieDashboardUiEvent
    data object ToggleAutoComplete : MovieDashboardUiEvent
    data object Clear : MovieDashboardUiEvent
    class ToggleFavourite(val id: Int, val isFavourite: Boolean) : MovieDashboardUiEvent
}