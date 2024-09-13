package com.example.pexelsapp.presentation.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pexelsapp.domain.entities.Photo
import com.example.pexelsapp.domain.repository.PhotoRepository
import kotlinx.coroutines.launch

class MainViewModel(private val photoRepository: PhotoRepository) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _popularPhotos = mutableStateOf<List<Photo>>(emptyList())
    val popularPhotos: State<List<Photo>> = _popularPhotos

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _selectedCategory = mutableStateOf("Ice")
    val selectedCategory: State<String> = _selectedCategory

    private val _selectedBottomItem = mutableStateOf(0)
    val selectedBottomItem: State<Int> = _selectedBottomItem

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectBottomItem(index: Int) {
        _selectedBottomItem.value = index
    }


    fun fetchPopularPhotos(perPage: Int = 10) {
        viewModelScope.launch {
            try {
                val photosLiveData = photoRepository.fetchPopularPhotos(perPage)
                photosLiveData.observeForever { photos ->
                    _popularPhotos.value = photos ?: emptyList()
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    class MainViewModelFactory(private val photoRepository: PhotoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(photoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}