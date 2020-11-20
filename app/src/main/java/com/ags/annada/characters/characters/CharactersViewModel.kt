package com.ags.annada.characters.characters

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import com.ags.annada.characters.utils.Event
import kotlinx.coroutines.launch
import java.util.*

class CharactersViewModel @ViewModelInject constructor(
    private val repository: CharactersRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _selectItemEvent = MutableLiveData<Event<Int>>()
    val selectItemEvent: LiveData<Event<Int>> = _selectItemEvent

    private val _searchString: MutableLiveData<String> = MutableLiveData("")

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<CharacterItem>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                repository.refreshCharacters()
                _dataLoading.value = false
            }
        }

        repository.observeCharacters().distinctUntilChanged().switchMap { filterCharacters(it) }
    }

    val items: LiveData<List<CharacterItem>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<String>()
    val currentFilteringLabel: LiveData<String> = _currentFilteringLabel


    init {
        _currentFilteringLabel.value = SeasonNumber.ALL_SEASONS.toString()
        loadCharacters(true)
    }

    fun onClickItem(item: CharacterItem) {
        _selectItemEvent.value = Event(item.char_id)
    }

    private fun loadCharacters(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun filterCharacters(characters: List<CharacterItem>): LiveData<List<CharacterItem>> {
        val result = MutableLiveData<List<CharacterItem>>()

        viewModelScope.launch {
            result.value = filterItems(characters)
        }

        return result
    }

    private fun filterItems(characters: List<CharacterItem>): List<CharacterItem> {
        val filteredItems: List<CharacterItem>

        val seasonNumber = getSelectedSeasonNumber()
        val number = seasonNumber.ordinal

        val search = _searchString.value.toString()

        filteredItems = characters.filter { character ->
            search.isEmpty() || character.name.toLowerCase(Locale.ROOT)
                .startsWith(search.toLowerCase(Locale.ROOT))
        }.filter { character ->
            number == 0 || character.appearance.contains(number)
        }

        return filteredItems
    }

    fun setSearchString(search: String) {
        _searchString.value = search

        loadCharacters(false)
    }

    private fun getSelectedSeasonNumber(): SeasonNumber {
        return savedStateHandle.get(SELECTED_SEASON_NUMBER_KEY) ?: SeasonNumber.ALL_SEASONS
    }

    fun setSeasonNumber(seasonNumber: SeasonNumber) {
        savedStateHandle.set(SELECTED_SEASON_NUMBER_KEY, seasonNumber)
        _currentFilteringLabel.value = seasonNumber.toString()

        loadCharacters(false)
    }
}