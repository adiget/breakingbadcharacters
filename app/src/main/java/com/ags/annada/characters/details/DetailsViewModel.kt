package com.ags.annada.characters.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ags.annada.characters.characters.CharactersRepository
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor(
    private val repository: CharactersRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val item: LiveData<CharacterItem> =
        repository.getCharacterWithId(savedStateHandle.get<Int>("charId") ?: 0)

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        loadCharacterDetails()
    }

    private fun loadCharacterDetails() {
        uiScope.launch {
            repository.getCharacterWithId(savedStateHandle.get<Int>("charId") ?: 0)
        }
    }

    override fun onCleared() {
        super.onCleared()

        viewModelJob.cancel()
    }
}